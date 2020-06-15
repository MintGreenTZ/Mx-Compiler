package Compiler.IR;

import Compiler.AST.*;
import Compiler.IR.Inst.*;
import Compiler.IR.Operand.*;
import Compiler.SemanticAnalysis.ASTBaseVisitor;
import Compiler.SymbolTable.ClassSymbol;
import Compiler.SymbolTable.FunctionSymbol;
import Compiler.SymbolTable.Scope.GlobalScope;
import Compiler.SymbolTable.Symbol;
import Compiler.SymbolTable.Type.ArrayType;
import Compiler.SymbolTable.VariableSymbol;
import Compiler.Utils.IRError;
import Compiler.Utils.Location;

import java.util.ArrayList;
import java.util.Stack;

import static Compiler.Configuration.POINTER_SIZE;
import static Compiler.Configuration.REGISTER_SIZE;
import static Compiler.SemanticAnalysis.GlobalVisitor.*;

public class IRBuilder extends ASTBaseVisitor {
    private GlobalScope globalScope;
    private BasicBlock curBB;
    private Function curFunction;
    private ClassSymbol curClassSymbol;
    private IR ir = new IR();
    private Stack<BasicBlock> breakStack = new Stack<>();
    private Stack<BasicBlock> continueStack = new Stack<>();
    private ArrayList<Return> curFuncReturnInsList = new ArrayList<>();

    public IRBuilder(GlobalScope globalScope) {
        this.globalScope = globalScope;
        builtinFunctionInitializer();
    }

    public IR getIr() {
        return ir;
    }

    @Override
    public void visit(ProgramNode node) {
        BasicBlock BB = new BasicBlock();
        curBB = BB;
        Function globalFunction = new Function("__global_init");
        ir.addFunction(globalFunction);
        globalFunction.setEntryBB(BB);

        // set function
        for (DeclNode decl: node.getDecl())
            if (decl instanceof FunctionDeclNode) {
                ((FunctionDeclNode) decl).getFunctionSymbol().setFunction(new Function(((FunctionDeclNode) decl).getFunctionSymbol().getName()));
            } else if (decl instanceof ClassDeclNode) {
                for (FunctionDeclNode funcDecl : ((ClassDeclNode) decl).getFunctionDecl()) {
                    funcDecl.getFunctionSymbol().setFunction(new Function(funcDecl.getFunctionSymbol().getName()));
                }
            }

        for (DeclNode decl: node.getDecl())
            if (decl instanceof VariableDeclNode)
                decl.accept(this);

        RegValue initRet = new RegValue("__init_ret");
        curBB.addInst(new FuncCall(((FunctionSymbol) globalScope.resolveSymbol("main", new Location(0, 0))).getFunction(), null, new ArrayList<>(), initRet));
        curBB.addInst(new Return(initRet));
        globalFunction.setExitBB(curBB);

        for (DeclNode decl: node.getDecl())
            if (!(decl instanceof VariableDeclNode))
                decl.accept(this);
    }

    @Override
    public void visit(ClassDeclNode node) {
        curClassSymbol = node.getClassSymbol();
        for (FunctionDeclNode functionDeclNode: node.getFunctionDecl())
            functionDeclNode.accept(this);
        curClassSymbol = null;
    }

    @Override
    public void visit(FunctionDeclNode node) {
        FunctionSymbol functionSymbol = node.getFunctionSymbol();
        Function function = node.getFunctionSymbol().getFunction();
        curFunction = function;
        ir.addFunction(function);
        curFuncReturnInsList.clear();

        if (functionSymbol.isMemberFunction())
            function.setReferenceToThisClass(new RegValue("this"));

        // add parameters
        for (ParameterNode parameterNode: node.getParameterList()) {
            parameterNode.accept(this);
            function.addArg(parameterNode.getVariable().getVariableSymbol().getVariableReg());
        }

        BasicBlock entryBB = new BasicBlock("entry");
        curBB = entryBB;
        function.setEntryBB(entryBB);
        node.getFuncBody().accept(this);

        // add return in case of without return
        if (!(curBB.getLastInst() instanceof Return)) {
            if (functionSymbol.getType().getTypeName().equals("void"))
                curBB.addInst(new Return(null));
            else
                curBB.addInst(new Return(new Imm(0)));
            curFuncReturnInsList.add((Return) curBB.getLastInst());
        }

        // merge all return blocks to exitBB
        if (curFuncReturnInsList.size() == 0) throw new IRError("WTF");
        if (curFuncReturnInsList.size() > 1) {
            BasicBlock exitBlock = new BasicBlock("func_exit");
            function.setExitBB(exitBlock);
            boolean hasReturn = !(functionSymbol.getType() == voidType || functionSymbol.getType() == nullType);
            RegValue retValue = hasReturn ? new RegValue() : null;
            for (Return ret : curFuncReturnInsList) {
                ret.removeSelf();
                if (hasReturn)
                    ret.getCurBlock().sudoAddInst(new Move(ret.getRet(), retValue));
                ret.getCurBlock().sudoAddInst(new Jump(exitBlock));
            }
            exitBlock.addInst(new Return(retValue));
        } else
            function.setExitBB(curFuncReturnInsList.get(0).getCurBlock());
    }

    @Override
    public void visit(ParameterNode node) {
        node.getVariable().accept(this);
    }

    @Override
    public void visit(BlockNode node) {
        for (StmtNode stmtNode: node.getStmtList())
            stmtNode.accept(this);
    }

    @Override
    public void visit(VariableDeclStmtNode node) {
        for (VariableNode variableNode: node.getVariableDecl().getVariables())
            variableNode.accept(this);
    }

    @Override
    public void visit(VariableDeclNode node) {
        for (VariableNode variableNode: node.getVariables())
            variableNode.accept(this);
    }

    @Override
    public void visit(IfNode node) {
        BasicBlock thenBB = new BasicBlock("if_then");
        BasicBlock elseBB = new BasicBlock("if_else");
        BasicBlock exitBB = new BasicBlock("if_exit");

        if (node.getElseStmt() != null) {
            node.getCond().accept(this);
            curBB.addInst(new Branch(operand2Val(node.getCond().getIRResult()), thenBB, elseBB));

            curBB = thenBB;
            node.getThenStmt().accept(this);
            curBB.addInst(new Jump(exitBB));

            curBB = elseBB;
            node.getElseStmt().accept(this);
            curBB.addInst(new Jump(exitBB));
        } else {
            node.getCond().accept(this);
            curBB.addInst(new Branch(operand2Val(node.getCond().getIRResult()), thenBB, exitBB));

            curBB = thenBB;
            node.getThenStmt().accept(this);
            curBB.addInst(new Jump(exitBB));
        }

        curBB = exitBB;
    }

    @Override
    public void visit(WhileNode node) {
        BasicBlock condBB = new BasicBlock("while_cond");
        BasicBlock loopBB = new BasicBlock("while_loop");
        BasicBlock exitBB = new BasicBlock("while_exit");

        breakStack.push(exitBB);
        continueStack.push(loopBB);

        curBB.addInst(new Jump(condBB));

        curBB = condBB;
        node.getCond().accept(this);
        curBB.addInst(new Branch(operand2Val(node.getCond().getIRResult()), loopBB, exitBB));

        curBB = loopBB;
        node.getLoop().accept(this);
        curBB.addInst(new Jump(condBB));

        curBB = exitBB;

        breakStack.pop();
        continueStack.pop();
    }

    @Override
    public void visit(ForNode node) {
        BasicBlock condBB = new BasicBlock("for_cond");
        BasicBlock loopBB = new BasicBlock("for_loop");
        BasicBlock stepBB = new BasicBlock("for_step");
        BasicBlock exitBB = new BasicBlock("for_exit");

        breakStack.push(exitBB);
        continueStack.push(stepBB);

        if (node.getInit() != null) node.getInit().accept(this);

        curBB.addInst(new Jump(condBB));

        curBB = condBB;
        if (node.getCond() != null) {
            node.getCond().accept(this);
            curBB.addInst(new Branch(operand2Val(node.getCond().getIRResult()), loopBB, exitBB));
        } else
            curBB.addInst(new Branch(new Imm(1), loopBB, exitBB));

        curBB = loopBB;
        node.getLoopBody().accept(this);
        curBB.addInst(new Jump(stepBB));

        curBB = stepBB;
        if (node.getStep() != null) node.getStep().accept(this);
        curBB.addInst(new Jump(condBB));

        curBB = exitBB;

        breakStack.pop();
        continueStack.pop();
    }

    @Override
    public void visit(ReturnNode node) {
        if (node.getRetValue() == null)
            curBB.addLastInst(new Return(null));
        else {
            node.getRetValue().accept(this);
            curBB.addLastInst(new Return(operand2Val(node.getRetValue().getIRResult())));
        }
        if (curBB.getLastInst() instanceof Return)
            curFuncReturnInsList.add((Return) curBB.getLastInst());
    }

    @Override
    public void visit(BreakNode node) {
        curBB.addInst(new Jump(breakStack.peek()));
    }

    @Override
    public void visit(ContinueNode node) {
        curBB.addInst(new Jump(continueStack.peek()));
    }

    @Override
    public void visit(ExprStmtNode node) {
        node.getExpr().accept(this);
    }

    @Override
    public void visit(ThisNode node) {
        node.setIRResult(curFunction.getReferenceToThisClass());
    }

    @Override
    public void visit(IdNode node) {
        Symbol symbol = node.getSymbol();
        if (symbol instanceof VariableSymbol) {
            if (symbol.getScope() == curClassSymbol) {
                // class member variable
                RegPtr ptr = new RegPtr();
                curBB.addInst(new BinaryOp(curFunction.getReferenceToThisClass(), new Imm(((VariableSymbol) symbol).getOffset()), ptr, BinaryOp.Op.ADD));
                node.setIRResult(ptr);
            } else {
                // not class member variable
                node.setIRResult(((VariableNode) ((VariableSymbol) symbol).getDefinition()).getVariableSymbol().getVariableReg());
                if (node.getIRResult() == null) throw new IRError("No IR result in idnode");
            }
        }
    }

    @Override
    public void visit(SuffixExprNode node) {
        node.getExpr().accept(this);
        Operand exprResult = node.getExpr().getIRResult();
        Operand dest = new RegValue();
        BinaryOp.Op op = node.getOp() == SuffixExprNode.Op.sufADD ? BinaryOp.Op.ADD : BinaryOp.Op.SUB;
        if (exprResult instanceof RegValue) {
            curBB.addInst(new Move(exprResult, dest));
            curBB.addInst(new BinaryOp(exprResult, new Imm(1), exprResult, op));
        } else {
            curBB.addInst(new Load(dest, exprResult));
            RegValue valueAfterOp = new RegValue();
            curBB.addInst(new BinaryOp(dest, new Imm(1), valueAfterOp, op));
            curBB.addInst(new Store(valueAfterOp, exprResult));
        }
        node.setIRResult(dest);
    }

    @Override
    public void visit(MemberAccessNode node) {
        node.getObj().accept(this);
         if (node.getSymbol() instanceof VariableSymbol) { // class member access, in the case of Array.size() node.getSymbol() is null
             RegValue base_addr = (RegValue) operand2Val(node.getObj().getIRResult());
             int offset = ((VariableSymbol) node.getSymbol()).getOffset();
             node.setIRResult(new RegPtr());
             curBB.addInst(new BinaryOp(base_addr, new Imm(offset), node.getIRResult(), BinaryOp.Op.ADD));
         } else {
             // Array.size()
             node.setIRResult(node.getObj().getIRResult());
         }
    }

    @Override
    public void visit(SubscriptNode node) {
        node.getArray().accept(this);
        node.getSubscript().accept(this);
        ArrayType arrayType = (ArrayType) node.getArray().getType();
        Operand arrayOpe = operand2Val(node.getArray().getIRResult());
        Operand subscriptOpe = operand2Val(node.getSubscript().getIRResult());
        RegValue dest_det_num = new RegValue();
        RegValue dest_det_addr = new RegValue();
        RegPtr dest = new RegPtr();
        node.setIRResult(dest);
        curBB.addInst(new BinaryOp(subscriptOpe, new Imm(1), dest_det_num, BinaryOp.Op.ADD));
        curBB.addInst(new BinaryOp(dest_det_num, new Imm(arrayType.getDim() == 1 ? arrayType.getBaseType().getTypeSize() : POINTER_SIZE), dest_det_addr, BinaryOp.Op.MUL));
        curBB.addInst(new BinaryOp(arrayOpe, dest_det_addr, dest, BinaryOp.Op.ADD));
    }

    @Override
    public void visit(FuncCallNode node) {
        node.getFuncObj().accept(this);
        FunctionSymbol functionSymbol = node.getFuncObj().getFunctionSymbol();
        Function function = functionSymbol.getFunction();

        Operand obj;
        if (!functionSymbol.isMemberFunction())
            obj = null;
        else {
            if (node.getFuncObj() instanceof MemberAccessNode) // outside class
                obj = operand2Val(((MemberAccessNode) node.getFuncObj()).getObj().getIRResult());
            else // inside class
                obj = curFunction.getReferenceToThisClass();
        }

        ArrayList<Operand> paraList = new ArrayList<>();
        for (ExprNode exprNode: node.getExprList()) {
            exprNode.accept(this);
            paraList.add(operand2Val(exprNode.getIRResult()));
        }

        node.setIRResult(node.getType().getTypeName().equals("void") ? null : new RegValue());

        curBB.addInst(new FuncCall(function, obj, paraList, node.getIRResult()));
    }

    @Override
    public void visit(PrefixExprNode node) {
        node.getExpr().accept(this);
        Operand exprResult = node.getExpr().getIRResult();
        switch (node.getOp()) {
            case INV:
                node.setIRResult(new RegValue());
                curBB.addInst(new UnaryOp(exprResult, node.getIRResult(), UnaryOp.Op.INV));
                break;
            case LogicINV:
                BasicBlock trueBB = new BasicBlock();
                BasicBlock falseBB = new BasicBlock();
                BasicBlock exitBB = new BasicBlock();
                Operand dest = new RegValue();
                node.setIRResult(dest);
                curBB.addInst(new Branch(exprResult, trueBB, falseBB));
                trueBB.addInst(new Move(new Imm(0), dest));
                trueBB.addInst(new Jump(exitBB));
                falseBB.addInst(new Move(new Imm(1), dest));
                falseBB.addInst(new Jump(exitBB));
                curBB = exitBB;
                break;
            case preADD:
            case preSUB:
                BinaryOp.Op op = node.getOp() == PrefixExprNode.Op.preADD ? BinaryOp.Op.ADD : BinaryOp.Op.SUB;
                node.setIRResult(exprResult);
                if (exprResult instanceof RegValue) {
                    curBB.addInst(new BinaryOp(exprResult, new Imm(1), exprResult, op));
                } else {
                    RegValue tmp = new RegValue();
                    curBB.addInst(new Load(tmp, exprResult));
                    curBB.addInst(new BinaryOp(tmp, new Imm(1), tmp, op));
                    curBB.addInst(new Store(tmp, exprResult));
                }
                break;
            case POS:
                node.setIRResult(exprResult);
                break;
            case NEG:
                node.setIRResult(new RegValue());
                curBB.addInst(new UnaryOp(exprResult, node.getIRResult(), UnaryOp.Op.NEG));
                break;
        }
    }

    @Override
    public void visit(NewNode node) {
        node.setIRResult(new RegValue());
        if (node.getType() instanceof ClassSymbol) {
            curBB.addInst(new Alloc(new Imm(((ClassSymbol) node.getType()).getObjSize()), node.getIRResult()));
            FunctionSymbol constructorFunctionSymbol = ((ClassSymbol) node.getType()).getConstructor();
            if (constructorFunctionSymbol != null)
                curBB.addInst(new FuncCall(constructorFunctionSymbol.getFunction(), node.getIRResult(), new ArrayList<>(), null));
        } else { // ArrayType
            for (ExprNode exprNode: node.getExprList())
                exprNode.accept(this);
            newArray(node, 0, node.getIRResult());
        }
    }

    @Override
    public void visit(BinaryOpNode node) {
        switch (node.getOp()) {
            case ADD: {
                node.getLhs().accept(this);
                node.getRhs().accept(this);
                Operand lhsVal = operand2Val(node.getLhs().getIRResult());
                Operand rhsVal = operand2Val(node.getRhs().getIRResult());
                Operand dest = new RegValue();
                if (node.getLhs().isString()) {
                    // string + string
                    node.setIRResult(dest);
                    curBB.addInst(new FuncCall(builtinStrAdd, null, new ArrayList<>(){{add(lhsVal);add(rhsVal);}}, dest));
                } else {
                    // int + int
                    node.setIRResult(dest);
                    curBB.addInst(new BinaryOp(lhsVal, rhsVal, dest, BinaryOp.Op.ADD));
                }
                break;
            }
            case SUB:
            case MUL:
            case DIV:
            case MOD:
            case SHL:
            case SHR:
            case AND:
            case OR:
            case XOR: {
                node.getLhs().accept(this);
                if (node.getLhs().getIRResult() == null) throw new IRError("Lhs IR result missing.");
                node.getRhs().accept(this);
                Operand lhsVal = operand2Val(node.getLhs().getIRResult());
                Operand rhsVal = operand2Val(node.getRhs().getIRResult());
                Operand dest = new RegValue();
                node.setIRResult(dest);
                curBB.addInst(new BinaryOp(lhsVal, rhsVal, dest, BinaryOp.Op.valueOf(node.getOp().toString())));
                break;
            }
            case LT:
            case LE:
            case GT:
            case GE:
            case EQ:
            case NEQ: {
                node.getLhs().accept(this);
                node.getRhs().accept(this);
                Operand lhsVal = operand2Val(node.getLhs().getIRResult());
                Operand rhsVal = operand2Val(node.getRhs().getIRResult());
                Operand dest = new RegValue();
                node.setIRResult(dest);
                if (node.getLhs().isString()) {
                    // string op string
                    switch (node.getOp()) {
                        case LT: curBB.addInst(new FuncCall(builtinStrLT, null, new ArrayList<>(){{add(lhsVal);add(rhsVal);}}, dest)); break;
                        case LE: curBB.addInst(new FuncCall(builtinStrLE, null, new ArrayList<>(){{add(lhsVal);add(rhsVal);}}, dest)); break;
                        case GT: curBB.addInst(new FuncCall(builtinStrGT, null, new ArrayList<>(){{add(lhsVal);add(rhsVal);}}, dest)); break;
                        case GE: curBB.addInst(new FuncCall(builtinStrGE, null, new ArrayList<>(){{add(lhsVal);add(rhsVal);}}, dest)); break;
                        case EQ: curBB.addInst(new FuncCall(builtinStrEQ, null, new ArrayList<>(){{add(lhsVal);add(rhsVal);}}, dest)); break;
                        case NEQ: curBB.addInst(new FuncCall(builtinStrNEQ, null, new ArrayList<>(){{add(lhsVal);add(rhsVal);}}, dest)); break;
                    }
                } else {
                    // int op int
                    curBB.addInst(new BinaryOp(lhsVal, rhsVal, dest, BinaryOp.Op.valueOf(node.getOp().toString())));
                }
                break;
            }
            case LOR: {
                BasicBlock trueBB = new BasicBlock("or_true");
                BasicBlock falseBB = new BasicBlock("or_false");
                BasicBlock exitBB = new BasicBlock("or_exit");

                node.getLhs().accept(this);
                curBB.addInst(new Branch(operand2Val(node.getLhs().getIRResult()), trueBB, falseBB));

                // trueBB
                Operand dest = new RegValue();
                node.setIRResult(dest);
                trueBB.addInst(new Move(new Imm(1), dest));
                trueBB.addInst(new Jump(exitBB));

                // falseBB
                curBB = falseBB;
                node.getRhs().accept(this);
                falseBB.addInst(new Move(operand2Val(node.getRhs().getIRResult()), dest));
                falseBB.addInst(new Jump(exitBB));

                curBB = exitBB;
                break;
            }
            case LAND: {
                BasicBlock trueBB = new BasicBlock("or_true");
                BasicBlock falseBB = new BasicBlock("or_false");
                BasicBlock exitBB = new BasicBlock("or_exit");

                node.getLhs().accept(this);
                curBB.addInst(new Branch(operand2Val(node.getLhs().getIRResult()), trueBB, falseBB));

                // falseBB
                Operand dest = new RegValue();
                node.setIRResult(dest);
                falseBB.addInst(new Move(new Imm(0), dest));
                falseBB.addInst(new Jump(exitBB));

                // trueBB
                curBB = trueBB;
                node.getRhs().accept(this);
                trueBB.addInst(new Move(operand2Val(node.getRhs().getIRResult()), dest));
                trueBB.addInst(new Jump(exitBB));

                curBB = exitBB;
                break;
            }
            case ASS: {
                node.getLhs().accept(this);
                node.getRhs().accept(this);
                Operand dest = node.getLhs().getIRResult();
                Operand src = node.getRhs().getIRResult();
                Operand srcVal = operand2Val(src);
                if (dest instanceof RegValue)
                    curBB.addInst(new Move(srcVal, dest));
                else
                    curBB.addInst(new Store(srcVal, dest));
                break;
            }
        }
    }

    @Override
    public void visit(VariableNode node) {
        VariableSymbol variableSymbol = node.getVariableSymbol();
        if (node.isGlobal()) {
            RegPtr ptr = new RegPtr(node.getIdentifier());
            ptr.markGlobal();
            curBB.addInst(new Alloc(new Imm(node.getType().isReferenceType() ? POINTER_SIZE : REGISTER_SIZE), ptr));
            variableSymbol.setVariableReg(ptr);
            if (node.getInitExpr() != null) {
                node.getInitExpr().accept(this);
                curBB.addInst(new Store(operand2Val(node.getInitExpr().getIRResult()), ptr));
            }
            ir.addGlobalVariable(ptr);
        } else {
            RegValue variableReg = new RegValue(node.getIdentifier());
            variableSymbol.setVariableReg(variableReg);
            if (node.getInitExpr() != null) {
                node.getInitExpr().accept(this);
                curBB.addInst(new Move(operand2Val(node.getInitExpr().getIRResult()), variableReg));
            }
        }
    }

    @Override
    public void visit(ConstBoolNode node) {
        node.setIRResult(new Imm(node.isValue() ? 1 : 0));
    }

    @Override
    public void visit(ConstIntNode node) {
        node.setIRResult(new Imm(node.getValue()));
    }

    @Override
    public void visit(ConstStringNode node) {
        StaticStr staticStr = new StaticStr(node.getValue());
        node.setIRResult(staticStr);
        ir.addStaticStr(staticStr);
    }

    @Override
    public void visit(ConstNullNode node) {
        node.setIRResult(new Imm(0));
    }

    public Operand operand2Val(Operand operand) {
        if (operand instanceof RegPtr) {
            RegValue val = new RegValue();
            curBB.addInst(new Load(val, operand));
            return val;
        }
        else return operand;
    }

    public static Function builtinPrint        = new Function("print");
    public static Function builtinPrintln      = new Function("println");
    public static Function builtinPrintInt     = new Function("printInt");
    public static Function builtinPrintlnInt   = new Function("printlnInt");
    public static Function builtinGetString    = new Function("getString");
    public static Function builtinGetInt       = new Function("getInt");
    public static Function builtinToString     = new Function("toString");
    public static Function builtinStrLength    = new Function("string.length");
    public static Function builtinStrSubString = new Function("string.substring");
    public static Function builtinStrParseInt  = new Function("string.parseInt");
    public static Function builtinStrOrd       = new Function("string.ord");
    public static Function builtinStrAdd       = new Function("string.add");
    public static Function builtinStrEQ        = new Function("string.eq");
    public static Function builtinStrNEQ       = new Function("string.neq");
    public static Function builtinStrGT        = new Function("string.gt");
    public static Function builtinStrLT        = new Function("string.lt");
    public static Function builtinStrLE        = new Function("string.le");
    public static Function builtinStrGE        = new Function("string.ge");
    public static Function builtinArraySize    = new Function("array.size");

    private void builtinFunctionInitializer() {
        builtinPrint.markBuiltin();
        builtinPrintln.markBuiltin();
        builtinPrintInt.markBuiltin();
        builtinPrintlnInt.markBuiltin();
        builtinGetString.markBuiltin();
        builtinGetInt.markBuiltin();
        builtinToString.markBuiltin();
        builtinStrLength.markBuiltin();
        builtinStrSubString.markBuiltin();
        builtinStrParseInt.markBuiltin();
        builtinStrOrd.markBuiltin();
        builtinStrAdd.markBuiltin();
        builtinStrEQ.markBuiltin();
        builtinStrNEQ.markBuiltin();
        builtinStrGT.markBuiltin();
        builtinStrLT.markBuiltin();
        builtinStrLE.markBuiltin();
        builtinStrGE.markBuiltin();

        ir.addFunction(builtinPrint);
        ir.addFunction(builtinPrintln);
        ir.addFunction(builtinPrintInt);
        ir.addFunction(builtinPrintlnInt);
        ir.addFunction(builtinGetString);
        ir.addFunction(builtinGetInt);
        ir.addFunction(builtinToString);
        ir.addFunction(builtinStrLength);
        ir.addFunction(builtinStrSubString);
        ir.addFunction(builtinStrParseInt);
        ir.addFunction(builtinStrOrd);
        ir.addFunction(builtinStrAdd);
        ir.addFunction(builtinStrEQ);
        ir.addFunction(builtinStrNEQ);
        ir.addFunction(builtinStrGT);
        ir.addFunction(builtinStrLT);
        ir.addFunction(builtinStrLE);
        ir.addFunction(builtinStrGE);
        ir.addFunction(builtinArraySize);

        ((FunctionSymbol)globalScope.resolveSymbol("print")).setFunction(builtinPrint);
        ((FunctionSymbol)globalScope.resolveSymbol("println")).setFunction(builtinPrintln);
        ((FunctionSymbol)globalScope.resolveSymbol("printInt")).setFunction(builtinPrintInt);
        ((FunctionSymbol)globalScope.resolveSymbol("printlnInt")).setFunction(builtinPrintlnInt);
        ((FunctionSymbol)globalScope.resolveSymbol("getString")).setFunction(builtinGetString);
        ((FunctionSymbol)globalScope.resolveSymbol("getInt")).setFunction(builtinGetInt);
        ((FunctionSymbol)globalScope.resolveSymbol("toString")).setFunction(builtinToString);
        ((FunctionSymbol)StringType.resolveSymbol("length")).setFunction(builtinStrLength);
        ((FunctionSymbol)StringType.resolveSymbol("substring")).setFunction(builtinStrSubString);
        ((FunctionSymbol)StringType.resolveSymbol("parseInt")).setFunction(builtinStrParseInt);
        ((FunctionSymbol)StringType.resolveSymbol("ord")).setFunction(builtinStrOrd);

        RegValue ret = new RegValue(), obj = new RegValue();
        BasicBlock BB = new BasicBlock(); //{{addInst(new Load(ret, obj)); addInst(new Return(ret));}}; //strange
        BB.addInst(new Load(ret, obj));
        BB.addInst(new Return(ret));
        builtinArraySize.setReferenceToThisClass(obj);
        builtinArraySize.setEntryBB(BB);
        builtinArraySize.setExitBB(BB);
        getArraySizeSymbol.setFunction(builtinArraySize);
    }

    // new array [expr_1][expr_2]...[expr_k][][]...[] recursively
    private void newArray(NewNode node, int depth, Operand basePtr) {
        if (depth == node.getExprList().size()) return;
        ExprNode curExpr = node.getExprList().get(depth);
        Operand size_num = operand2Val(curExpr.getIRResult());
        Operand size = new RegValue();
        Operand size_plus_1 = new RegValue();

        if (node.getExprList().size() - 1 == depth)
            curBB.addInst(new BinaryOp(size_num, new Imm(node.getType().getTypeSize()), size, BinaryOp.Op.MUL));
        else
            curBB.addInst(new BinaryOp(size_num, new Imm(POINTER_SIZE), size, BinaryOp.Op.MUL));

        curBB.addInst(new BinaryOp(size, new Imm(REGISTER_SIZE), size_plus_1, BinaryOp.Op.ADD));

        // Allocation
        if (basePtr instanceof RegValue) {
            curBB.addInst(new Alloc(size_plus_1, basePtr));
            curBB.addInst(new Store(size_num, basePtr));
        } else {
            Operand allocatedMemoryPtr = new RegValue();
            curBB.addInst(new Alloc(size_plus_1, allocatedMemoryPtr));
            curBB.addInst(new Store(size_num, allocatedMemoryPtr));
            curBB.addInst(new Store(allocatedMemoryPtr, basePtr));
        }

        if (node.getExprList().size() - 1 != depth) {
            BasicBlock condBB = new BasicBlock();
            BasicBlock loopBB = new BasicBlock();
            BasicBlock exitBB = new BasicBlock();

            Operand curIdx = new RegValue();
            Operand curPtr = new RegPtr();

            curBB.addInst(new Move(new Imm(0), curIdx));
            curBB.addInst(new BinaryOp(basePtr, new Imm(REGISTER_SIZE), curPtr, BinaryOp.Op.ADD));
            curBB.addInst(new Jump(condBB));

            Operand cmp = new RegValue();
            condBB.addInst(new BinaryOp(curIdx, size_num, cmp, BinaryOp.Op.LT));
            condBB.addInst(new Branch(cmp, loopBB, exitBB));

            curBB = loopBB;
            newArray(node, depth + 1, curPtr);
            loopBB.addInst(new BinaryOp(curIdx, new Imm(1), curIdx, BinaryOp.Op.ADD));
            loopBB.addInst(new BinaryOp(curPtr, new Imm(POINTER_SIZE), curPtr, BinaryOp.Op.ADD));
            loopBB.addInst(new Jump(condBB));

            curBB = exitBB;
        }
    }

    public static int calc(BinaryOp.Op op, int a, int b) {
        switch (op) {
            case ADD:   return a + b;
            case SUB:   return a - b;
            case MUL:   return a * b;
            case DIV:   return a / b;
            case MOD:   return a % b;
            case SHL:   return a << b;
            case SHR:   return a >> b;
            case GT:    return a > b ? 1 : 0;
            case GE:    return a >= b ? 1 : 0;
            case LT:    return a < b ? 1 : 0;
            case LE:    return a <= b ? 1 : 0;
            case EQ:    return a == b ? 1 : 0;
            case NEQ:   return a != b ? 1 : 0;
            case AND:   return a & b;
            case OR:    return a | b;
            case XOR:   return a ^ b;
        }
        return 19260817;
    }
}

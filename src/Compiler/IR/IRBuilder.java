package Compiler.IR;

import Compiler.AST.*;
import Compiler.IR.Inst.*;
import Compiler.IR.Operand.Imm;
import Compiler.IR.Operand.Operand;
import Compiler.IR.Operand.RegPtr;
import Compiler.IR.Operand.RegValue;
import Compiler.SemanticAnalysis.ASTBaseVisitor;
import Compiler.SymbolTable.FunctionSymbol;
import Compiler.SymbolTable.Scope.GlobalScope;
import Compiler.SymbolTable.Type.ArrayType;
import Compiler.SymbolTable.VariableSymbol;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static Compiler.Configuration.POINTER_SIZE;
import static Compiler.Configuration.REGISTER_SIZE;

public class IRBuilder extends ASTBaseVisitor {
    private GlobalScope globalScope;
    private BasicBlock curBB;
    private Function curFunction;
    private IR ir;
    private Stack<BasicBlock> breakStack = new Stack<>();
    private Stack<BasicBlock> continueStack = new Stack<>();

    public IRBuilder(GlobalScope globalScope) {
        this.globalScope = globalScope;
        builtinFunctionInitializer();
    }

    @Override
    public void visit(ProgramNode node) {
        BasicBlock BB = new BasicBlock();
        curBB = BB;
        Function globalFunction = new Function("__global_init");
        globalFunction.setEntryBB(BB);

        // set global variable
        for (DeclNode decl: node.getDecl())
            if (decl instanceof VariableDeclNode) {
                VariableDeclNode variableDeclNode = (VariableDeclNode) decl;
                RegPtr ptr = new RegPtr(variableDeclNode.getType().getIdentifier()) {{markGlobal();}};
                curBB.addInst(new HeapAlloc(ptr, new Imm(variableDeclNode.getDetailedType().isReferenceType() ? POINTER_SIZE : REGISTER_SIZE)));
                List<VariableNode> variableList = variableDeclNode.getVariables();
                for (VariableNode variableNode: variableList) {
                    // TODO add to IR and sth
                }
            }

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
            if (decl instanceof FunctionDeclNode || decl instanceof ClassDeclNode)
                decl.accept(this);
    }

    @Override
    public void visit(ClassDeclNode node) {
        for (FunctionDeclNode functionDeclNode: node.getFunctionDecl())
            functionDeclNode.accept(this);
    }

    @Override
    public void visit(FunctionDeclNode node) {
        FunctionSymbol functionSymbol = node.getFunctionSymbol();
        Function function = node.getFunctionSymbol().getFunction();
        curFunction = function;
        ir.addFunction(function);
        if (functionSymbol.isMemberFunction())
            function.setReferenceToCurClass(new RegValue("this"));

        // add return in case of without return
        if (!(curBB.getLastInst() instanceof Return)) {
            if (functionSymbol.getType().getTypeName().equals("void"))
                curBB.addInst(new Return(null));
            else
                curBB.addInst(new Return(new Imm(0)));
            curFunction.addReturnInst((Return) curBB.getLastInst());
        }

        // add parameters
        for (String arg: functionSymbol.getArguments().keySet())
            function.addArg(new RegValue(arg));

        // merge all return blocks to exitBB
        // TODO
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
            curBB.addInst(new Branch(Operand2Val(node.getCond().getIRResult()), thenBB, exitBB));

            curBB = thenBB;
            node.getThenStmt().accept(this);
            curBB.addInst(new Jump(exitBB));

            curBB = elseBB;
            node.getElseStmt().accept(this);
            curBB.addInst(new Jump(exitBB));
        } else {
            node.getCond().accept(this);
            curBB.addInst(new Branch(Operand2Val(node.getCond().getIRResult()), thenBB, exitBB));

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
        curBB.addInst(new Branch(Operand2Val(node.getCond().getIRResult()), loopBB, exitBB));

        curBB = loopBB;
        node.getLoop().accept(this);
        condBB.addInst(new Jump(condBB));

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

        curBB.addInst(new Jump(condBB));

        if (node.getCond() != null) {
            curBB = condBB;
            node.getCond().accept(this);
            curBB.addInst(new Branch(Operand2Val(node.getCond().getIRResult()), loopBB, exitBB));
        } else {
            curBB = condBB;
            curBB.addInst(new Branch(new Imm(1), loopBB, exitBB));
        }

        curBB = loopBB;
        node.getLoopBody().accept(this);
        curBB.addInst(new Jump(stepBB));

        if (node.getStep() != null) {
            curBB = stepBB;
            node.getStep().accept(this);
            curBB.addInst(new Jump(condBB));
        }

        curBB = exitBB;

        breakStack.pop();
        continueStack.pop();
    }

    @Override
    public void visit(ReturnNode node) {
        if (node.getRetValue() == null)
            curBB.addInst(new Return(null));
        else {
            node.getRetValue().accept(this);
            curBB.addInst(new Return(Operand2Val(node.getRetValue().getIRResult())));
        }
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
        node.setIRResult(curFunction.getReferenceToCurClass());
    }

    @Override
    public void visit(IdNode node) {

    }

    @Override
    public void visit(SuffixExprNode node) {
        node.getExpr().accept(this);
        Operand exprResult = node.getIRResult();
        Operand dest = new RegValue();
        BinaryOp.Op op = node.getOp() == SuffixExprNode.Op.sufADD ? BinaryOp.Op.ADD : BinaryOp.Op.SUB;
        if (exprResult instanceof RegValue) {
            curBB.addInst(new Move(exprResult, dest));
            curBB.addInst(new BinaryOp(exprResult, new Imm(1), exprResult, op));
        } else {
            curBB.addInst(new Load(dest, exprResult));
            RegValue valueAfterOp = new RegValue();
            curBB.addInst(new BinaryOp(exprResult, new Imm(1), valueAfterOp, op));
            curBB.addInst(new Store(valueAfterOp, exprResult));
        }
        node.setIRResult(dest);
    }

    @Override
    public void visit(MemberAccessNode node) {

    }

    @Override
    public void visit(SubscriptNode node) {
        node.getArray().accept(this);
        node.getSubscript().accept(this);
        ArrayType arrayType = (ArrayType) node.getArray().getType();
        Operand arrayOpe = node.getArray().getIRResult();
        Operand subscriptOpe = node.getSubscript().getIRResult();
        RegValue dest_det_num = new RegValue();
        RegValue dest_det_addr = new RegValue();
        RegValue dest = new RegValue();
        node.setIRResult(dest);
        curBB.addInst(new BinaryOp(subscriptOpe, new Imm(1), dest_det_num, BinaryOp.Op.ADD));
        curBB.addInst(new BinaryOp(subscriptOpe, new Imm(arrayType.getDim() == 1 ? arrayType.getBaseType().getTypeSize() : POINTER_SIZE), dest_det_addr, BinaryOp.Op.MUL));
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
            if (node.getFuncObj() instanceof MemberAccessNode)
                obj = Operand2Val(((MemberAccessNode) node.getFuncObj()).getObj().getIRResult());
            else
                obj = curFunction.getMemberObj()
        }

        ArrayList<Operand> paraList = new ArrayList<>();
        for (ExprNode exprNode: node.getExprList()) {
            exprNode.accept(this);
            paraList.add(Operand2Val(exprNode.getIRResult()));
        }

        node.setIRResult(node.getType().getTypeName().equals("void") ? null : new RegValue());

        curBB.addInst(new FuncCall(function, obj, paraList, node.getIRResult()));
    }

    @Override
    public void visit(PrefixExprNode node) {
        node.getExpr().accept(this);
        Operand exprResult = node.getExpr().setIRResult();
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

    }

    @Override
    public void visit(BinaryOpNode node) {
        switch (node.getOp()) {
            case ADD: {
                node.getLhs().accept(this);
                node.getRhs().accept(this);
                Operand lhsVal = Operand2Val(node.getLhs().getIRResult());
                Operand rhsVal = Operand2Val(node.getRhs().getIRResult());
                Operand dest = new RegValue();
                if (node.getLhs().isString()) {
                    // string + string
                    node.setIRResult(dest);
                    curBB.addInst(new FuncCall(builtinStrAdd, null, new ArrayList<>(){{add(lhsVal);add(rhsVal)}}, dest));
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
                node.getRhs().accept(this);
                Operand lhsVal = Operand2Val(node.getLhs().getIRResult());
                Operand rhsVal = Operand2Val(node.getRhs().getIRResult());
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
                Operand lhsVal = Operand2Val(node.getLhs().getIRResult());
                Operand rhsVal = Operand2Val(node.getRhs().getIRResult());
                Operand dest = new RegValue();
                node.setIRResult(dest);
                if (node.getLhs().isString()) {
                    // string op string
                    switch (node.getOp()) {
                        case LT: curBB.addInst(new FuncCall(builtinStrLT, null, new ArrayList<>(){{add(lhsVal);add(rhsVal)}}, dest)); break;
                        case LE: curBB.addInst(new FuncCall(builtinStrLE, null, new ArrayList<>(){{add(lhsVal);add(rhsVal)}}, dest)); break;
                        case GT: curBB.addInst(new FuncCall(builtinStrGT, null, new ArrayList<>(){{add(lhsVal);add(rhsVal)}}, dest)); break;
                        case GE: curBB.addInst(new FuncCall(builtinStrGE, null, new ArrayList<>(){{add(lhsVal);add(rhsVal)}}, dest)); break;
                        case EQ: curBB.addInst(new FuncCall(builtinStrEQ, null, new ArrayList<>(){{add(lhsVal);add(rhsVal)}}, dest)); break;
                        case NEQ: curBB.addInst(new FuncCall(builtinStrNEQ, null, new ArrayList<>(){{add(lhsVal);add(rhsVal)}}, dest)); break;
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
                curBB.addInst(new Branch(Operand2Val(node.getLhs().getIRResult()), trueBB, falseBB));

                // trueBB
                Operand dest = new RegValue();
                node.setIRResult(dest);
                trueBB.addInst(new Move(new Imm(1), dest));
                trueBB.addInst(new Jump(exitBB));

                // falseBB
                curBB = falseBB;
                node.getRhs().accept(this);
                falseBB.addInst(new Move(Operand2Val(node.getRhs().getIRResult()), dest));
                falseBB.addInst(new Jump(exitBB));

                curBB = exitBB;
                break;
            }
            case LAND: {
                BasicBlock trueBB = new BasicBlock("or_true");
                BasicBlock falseBB = new BasicBlock("or_false");
                BasicBlock exitBB = new BasicBlock("or_exit");

                node.getLhs().accept(this);
                curBB.addInst(new Branch(Operand2Val(node.getLhs().getIRResult()), trueBB, falseBB));

                // falseBB
                Operand dest = new RegValue();
                node.setIRResult(dest);
                falseBB.addInst(new Move(new Imm(0), dest));
                falseBB.addInst(new Jump(exitBB));

                // trueBB
                curBB = trueBB;
                node.getRhs().accept(this);
                trueBB.addInst(new Move(Operand2Val(node.getRhs().getIRResult()), dest));
                trueBB.addInst(new Jump(exitBB));

                curBB = exitBB;
                break;
            }
            case ASS: {
                node.getLhs().accept(this);
                node.getRhs().accept(this);
                Operand dest = node.getLhs().getIRResult();
                Operand src = node.getRhs().getIRResult();
                if (src instanceof RegValue)
                    curBB.addInst(new Move(Operand2Val(src), dest));
                else
                    curBB.addInst(new Store(Operand2Val(src), dest));
                break;
            }
        }
    }

    @Override
    public void visit(VariableNode node) {
        VariableSymbol variableSymbol = node.getVariableSymbol();
        RegValue variableReg = new RegValue(node.getIdentifier());
        variableSymbol.setVariableReg(variableReg);
        if (node.isParameter() && curFunction != null)
            curFunction.addArg(variableReg);
        if (node.getInitExpr() != null) {
            node.getInitExpr().accept(this);
            curBB.addInst(new Move(Operand2Val(node.getInitExpr().getIRResult()), variableReg));
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

    }

    @Override
    public void visit(ConstNullNode node) {
        node.setIRResult(new Imm(0));
    }

    public Operand Operand2Val(Operand ptr) {
        if (ptr instanceof RegPtr) {
            RegValue val = new RegValue();
            curBB.addInst(new Load(val, ptr));
            return val;
        }
        else return ptr; //TODO doubt
    }

    public static Function builtinPrint        = new Function("builtinPrint");
    public static Function builtinPrintln      = new Function("builtinPrintln");
    public static Function builtinPrintInt     = new Function("builtinPrintInt");
    public static Function builtinPrintlnInt   = new Function("builtinPrintlnInt");
    public static Function builtinGetString    = new Function("builtinGetString");
    public static Function builtinGetInt       = new Function("builtinGetInt");
    public static Function builtinToString     = new Function("builtinToString");
    public static Function builtinStrLength    = new Function("builtinStrLength");
    public static Function builtinStrSubString = new Function("builtinStrSubString");
    public static Function builtinStrParseString = new Function("builtinStrParseString");
    public static Function builtinStrOrd       = new Function("builtinStrOrd");
    public static Function builtinStrAdd       = new Function("builtinStrAdd");
    public static Function builtinStrEQ        = new Function("builtinStrEQ");
    public static Function builtinStrNEQ       = new Function("builtinStrNEQ");
    public static Function builtinStrGT        = new Function("builtinStrGT");
    public static Function builtinStrLT        = new Function("builtinStrLT");
    public static Function builtinStrLE        = new Function("builtinStrLE");
    public static Function builtinStrGE        = new Function("builtinStrGE");

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
        builtinStrParseString.markBuiltin();
        builtinStrOrd.markBuiltin();
        builtinStrAdd.markBuiltin();
        builtinStrEQ.markBuiltin();
        builtinStrNEQ.markBuiltin();
        builtinStrGT.markBuiltin();
        builtinStrLT.markBuiltin();
        builtinStrLE.markBuiltin();
        builtinStrGE.markBuiltin();
    }
}

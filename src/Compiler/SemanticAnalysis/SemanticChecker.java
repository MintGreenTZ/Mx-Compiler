package Compiler.SemanticAnalysis;

import Compiler.AST.*;
import Compiler.SymbolTable.*;
import Compiler.SymbolTable.Scope.GlobalScope;
import Compiler.SymbolTable.Type.ArrayType;
import Compiler.SymbolTable.Type.NullType;
import Compiler.SymbolTable.Type.Type;
import Compiler.Utils.Location;
import Compiler.Utils.SemanticError;

import java.util.Iterator;

import static Compiler.SemanticAnalysis.GlobalVisitor.*;

public class SemanticChecker extends ASTBaseVisitor {
    private static GlobalScope globalScope;

    public SemanticChecker(GlobalScope globalScope) {
        this.globalScope = globalScope;
    }

    @Override
    public void visit(ProgramNode node) {
        for (DeclNode decl: node.getDecl())
            decl.accept(this);
    }

    @Override
    public void visit(ClassDeclNode node) {
        for (var funcDeclNode: node.getFunctionDecl())
            funcDeclNode.accept(this);
    }

    @Override
    public void visit(FunctionDeclNode node) {
        node.getFuncBody().accept(this);
    }

    /*
     * ProgramNode -> ClassDecl, FuncDecl
     * ClassDecl -> FuncDecl
     * FuncDecl -> FuncBody (BlockNode)
     * SO FAR WE ARE ALREADY IN EVERY FUNCTION BODY !
     * */

    /**************************Statement Begin*****************************/
    @Override
    public void visit(BlockNode node) {
        for (var stmt : node.getStmtList())
            stmt.accept(this);
    }

    @Override
    public void visit(VariableDeclStmtNode node) {
        node.getVariableDecl().accept(this);
    }

    @Override
    public void visit(VariableDeclNode node) {
        for (var variable: node.getVariables()) {
            variable.setType(typeResolver(node.getType(), globalScope));
            variable.accept(this);
        }
    }

    @Override
    public void visit(IfNode node) {
        preAssign(boolType, node.getCond().getType(), node.getLocation());
        node.getCond().accept(this);
        node.getThenStmt().accept(this);
        if (node.getElseStmt() != null)
            node.getElseStmt().accept(this);
    }

    @Override
    public void visit(WhileNode node) {
        node.getCond().accept(this);
        preAssign(boolType, node.getCond().getType(), node.getLocation());
        node.getLoop().accept(this);
    }

    @Override
    public void visit(ForNode node) {
        if (node.getInit() != null) node.getInit().accept(this);
        if (node.getCond() != null) {
            node.getCond().accept(this);
            preAssign(boolType, node.getCond().getType(), node.getLocation());
        }
        if (node.getStep() != null) node.getStep().accept(this);
        node.getLoopBody().accept(this);
    }

    @Override
    public void visit(ReturnNode node) {
        Type returnType = node.getFunctionSymbol().getType();
        if (node.getRetValue() != null) {
            if (returnType == null || returnType == voidType)
                throw new SemanticError("Unexpected return value.", node.getLocation());
            node.getRetValue().accept(this);
            preAssign(returnType, node.getRetValue().getType(), node.getLocation());
        } else if (returnType != null && returnType != voidType)
            throw new SemanticError("Missing return value.", node.getLocation());
    }

    @Override
    public void visit(ExprStmtNode node) {
        node.getExpr().accept(this);
    }
    /**************************Statement End*****************************/

    /**************************Expression Begin*****************************/
    @Override
    public void visit(ThisNode node) {
        node.setType((ClassSymbol) node.getScope());
    }

    @Override
    public void visit(IdNode node) {
        Symbol symbol = node.getSymbol();
        node.setType(symbol.getType());
        if (symbol instanceof VariableSymbol) {
            node.setCategory(ExprNode.Category.LVALUE);
            node.setType(symbol.getType());
        } else if (symbol instanceof FunctionSymbol) {
            node.setCategory(ExprNode.Category.FUNCTION);
            node.setType(symbol.getType());
            node.setFunctionSymbol((FunctionSymbol) symbol);
        } else if (symbol instanceof  ClassSymbol) {
            node.setCategory(ExprNode.Category.CLASS);
            node.setType((ClassSymbol) symbol);
        } else
            throw new SemanticError("Unexpected identifier " + symbol.getName(), node.getLocation());
    }

    @Override
    public void visit(SuffixExprNode node) {
        node.getExpr().accept(this);
        preAssign(intType, node.getExpr().getType(), node.getLocation());
        if (!node.getExpr().isLvalue())
            throw new SemanticError(node.getExpr().getType().getTypeName() + "is not Lvalue", node.getLocation());
        node.setType(intType);
    }

    @Override
    public void visit(MemberAccessNode node) {
        node.getObj().accept(this);
        Type type = node.getObj().getType();
        if (type instanceof ClassSymbol) {
            Symbol memberSymbol = ((ClassSymbol) node.getObj().getType()).accessMember(node.getMember(), node.getLocation());
            node.setType(memberSymbol.getType());
            node.setSymbol(memberSymbol);
            if (memberSymbol instanceof FunctionSymbol) {
                node.setCategory(ExprNode.Category.FUNCTION);
                node.setFunctionSymbol((FunctionSymbol) memberSymbol);
            }
            else
                node.setCategory(ExprNode.Category.LVALUE);
        } else if (type instanceof ArrayType) {
            if (!node.getMember().equals("size"))
                throw new SemanticError("Array has no such a member.", node.getLocation());
            node.setType(intType);
            node.setCategory(ExprNode.Category.FUNCTION);
            node.setFunctionSymbol(getArraySizeSymbol);
        } else
            throw new SemanticError("Invalid type to member access.", node.getLocation());
    }

    @Override
    public void visit(SubscriptNode node) {
        node.getBody().accept(this);
        node.getSubscript().accept(this);
        if (!(node.getBody().getType() instanceof ArrayType))
            throw new SemanticError("Non-Array type cannot be indexed.", node.getLocation());
        if (!node.getSubscript().isInt())
            throw new SemanticError("Index should be int.", node.getLocation());
        ArrayType type = (ArrayType) node.getBody().getType();
        if (type.getDim() == 1)
            node.setType(type.getBaseType());
        else
            node.setType(new ArrayType(type.getBaseType(), type.getDim() - 1));
        node.setCategory(ExprNode.Category.LVALUE);
    }

    @Override
    public void visit(FuncCallNode node) {
        ExprNode function = node.getFuncObj();
        function.accept(this);
        for (var expr: node.getExprList())
            expr.accept(this);
        if (function.getCategory() != ExprNode.Category.FUNCTION)
            throw new SemanticError("No such a function.", node.getLocation());
        if (node.getExprList().size() != function.getFunctionSymbol().getArguments().size())
            throw new SemanticError("The number of arguments not matched.", node.getLocation());
        Iterator<ExprNode> exprNodeIterator = node.getExprList().iterator();
        for (var entry: function.getFunctionSymbol().getArguments().entrySet()) {
            ExprNode exprNode = exprNodeIterator.next();
            VariableSymbol variableSymbol = entry.getValue();
            preAssign(variableSymbol.getType(), exprNode.getType(), node.getLocation());
        }
        node.setFunctionSymbol(node.getFuncObj().getFunctionSymbol());
        node.setType(node.getFunctionSymbol().getType());
    }

    @Override
    public void visit(PrefixExprNode node) {
        node.getExpr().accept(this);
        switch (node.getOp()) {
            case INV: case ADD: case SUB:
                preAssign(intType, node.getExpr().getType(), node.getLocation());
                node.setType(intType);
                break;
            case LogicINV:
                preAssign(boolType, node.getExpr().getType(), node.getLocation());
                node.setType(boolType);
                break;
            case SelfADD: case SelfSUB:
                preAssign(intType, node.getExpr().getType(), node.getLocation());
                if (!node.getExpr().isLvalue())
                    throw new SemanticError(node.getExpr().getType().getTypeName() + "is not Lvalue", node.getLocation());
                node.setType(intType);
                node.setCategory(ExprNode.Category.LVALUE);
                break;
        }
    }

    @Override
    public void visit(NewNode node) {
        for (var expr: node.getExprList()) {
            expr.accept(this);
            if (!expr.isInt())
                throw new SemanticError("Index should be int.", node.getLocation());
        }
        Type type = node.getDetailedBaseType();
        if (node.getDim() == 0) {
            if (type instanceof ClassSymbol) {
                if (((ClassSymbol) type).getConstructor() != null)
                    node.setFunctionSymbol(((ClassSymbol) type).getConstructor());
            }
            node.setType(type);
        } else
            node.setType(new ArrayType(type, node.getDim()));
    }

    @Override
    public void visit(BinaryOpNode node) {
        ExprNode LHS = node.getLhs();
        ExprNode RHS = node.getRhs();
        LHS.accept(this);
        RHS.accept(this);
        switch (node.getOp()) {
            case MUL: case DIV: case MOD: case SHL: case SHR: case AND: case OR: case XOR: case SUB:
                preAssign(intType, LHS.getType(), node.getLocation());
                preAssign(intType, LHS.getType(), node.getLocation());
                node.setType(intType);
                break;
            case LAND: case LOR:
                preAssign(boolType, LHS.getType(), node.getLocation());
                preAssign(boolType, LHS.getType(), node.getLocation());
                node.setType(boolType);
                break;
            case ADD:
                if (LHS.isInt() && RHS.isInt())
                    node.setType(intType);
                else if (LHS.isString() && RHS.isString())
                    node.setType(StringType);
                else
                    throw new SemanticError("Invalid operation between " + LHS.getType().getTypeName() + " and " + RHS.getType().getTypeName(), node.getLocation());
                break;
            case GE: case GT: case LE: case LT:
                if (!((LHS.isInt() && RHS.isInt()) || (LHS.isString() && RHS.isString())))
                    throw new SemanticError("Invalid operation between " + LHS.getType().getTypeName() + " and " + RHS.getType().getTypeName(), node.getLocation());
                node.setType(boolType);
                break;
            case EQ: case NEQ:
                preEqual(LHS.getType(), RHS.getType(), node.getLocation());
                preEqual(LHS.getType(), RHS.getType(), node.getLocation());
                node.setType(boolType);
            case ASS:
                preAssign(LHS.getType(), RHS.getType(), node.getLocation());
                if (!LHS.isLvalue())
                    throw new SemanticError("Cannot assign to Rvalue " + LHS.getType().getTypeName(), node.getLocation());
                node.setType(LHS.getType());
        }
    }

    @Override
    public void visit(VariableNode node) {
        Type type = node.getType();
        if (node.getInitExpr() != null) {
            node.getInitExpr().setType(type);
            node.getInitExpr().accept(this);
            preAssign(type, node.getInitExpr().getType(), node.getLocation());
        }
    }

    @Override
    public void visit(ConstBoolNode node) {
        node.setType(boolType);
    }

    @Override
    public void visit(ConstIntNode node) {
        node.setType(intType);
    }

    @Override
    public void visit(ConstStringNode node) {
        node.setType(StringType);
    }

    @Override
    public void visit(ConstNullNode node) {
        node.setType(nullType);
    }

    /**************************Expression End*****************************/

    private void preAssign(Type LHS, Type RHS, Location location) {
        if (RHS.getTypeName().equals("void"))
            throw new SemanticError("Void cannot be assigned.", location);
        if (RHS.getTypeName().equals("null")) {
            if (LHS.getTypeName().equals("string"))
                throw new SemanticError("Null cannot assigned to string.", location);
            else
                return;
        }
        if (LHS instanceof NullType) {
            throw new SemanticError("Null is not assignable.", location);
        } else if (LHS instanceof PrimitiveSymbol || LHS instanceof ClassSymbol) {
            if (!LHS.getTypeName().equals(RHS.getTypeName()))
                throw new SemanticError(RHS.getTypeName() + " cannot be assign to " + LHS.getTypeName(), location);
        } else if (LHS instanceof ArrayType) {
            if (RHS instanceof ArrayType) {
                preAssign(((ArrayType) LHS).getBaseType(), ((ArrayType) RHS).getBaseType(), location);
                if (((ArrayType) LHS).getDim() != ((ArrayType) RHS).getDim())
                    throw new SemanticError("Array dimensions don't match, get " + ((ArrayType) RHS).getDim() + " but expect " + ((ArrayType) LHS).getDim(), location);
            } else
                throw new SemanticError("Array type " + LHS.getTypeName() + " cannot be assigned by non-array type " + RHS.getTypeName(), location);
        }
    }

    private void preEqual(Type LHS, Type RHS, Location location) {
        if (RHS.getTypeName().equals("void"))
            throw new SemanticError("Void cannot be assigned.", location);
        if (RHS.getTypeName().equals("null")) {
            if (LHS.getTypeName().equals("string"))
                throw new SemanticError("Null cannot assigned to string.", location);
            else
                return;
        }
        if (LHS instanceof NullType) {
            throw new SemanticError("Null is not assignable.", location);
        } else if (LHS instanceof PrimitiveSymbol || LHS instanceof ClassSymbol) {
            if (!LHS.getTypeName().equals(RHS.getTypeName()))
                throw new SemanticError(RHS.getTypeName() + " cannot be assign to " + LHS.getTypeName(), location);
        } else if (LHS instanceof ArrayType) {
            if (RHS instanceof ArrayType) {
                preAssign(((ArrayType) LHS).getBaseType(), ((ArrayType) RHS).getBaseType(), location);
                if (((ArrayType) LHS).getDim() != ((ArrayType) RHS).getDim())
                    throw new SemanticError("Array dimensions don't match, get " + ((ArrayType) RHS).getDim() + " but expect " + ((ArrayType) LHS).getDim(), location);
            } else
                throw new SemanticError("Array type " + LHS.getTypeName() + " cannot be assigned by non-array type " + RHS.getTypeName(), location);
        }
    }
}

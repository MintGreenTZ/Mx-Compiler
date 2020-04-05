package Compiler.SemanticAnalysis;

import Compiler.AST.*;
import Compiler.SymbolTable.Scope.GlobalScope;
import Compiler.SymbolTable.Type.ArrayType;
import Compiler.SymbolTable.Type.Type;

public class ASTBaseVisitor implements ASTVisitor {
    @Override
    public void visit(ArrayTypeNode node) {

    }

    @Override
    public void visit(AssignNode node) {

    }

    @Override
    public void visit(BinaryOpNode node) {

    }

    @Override
    public void visit(BlockNode node) {

    }

    @Override
    public void visit(BreakNode node) {

    }

    @Override
    public void visit(ClassDeclNode node) {

    }

    @Override
    public void visit(ConstBoolNode node) {

    }

    @Override
    public void visit(ConstIntNode node) {

    }

    @Override
    public void visit(ConstStringNode node) {

    }

    @Override
    public void visit(ContinueNode node) {

    }

    @Override
    public void visit(ExprListNode node) {

    }

    @Override
    public void visit(ExprStmtNode node) {

    }

    @Override
    public void visit(ForNode node) {

    }

    @Override
    public void visit(FuncCallNode node) {

    }

    @Override
    public void visit(FunctionDeclNode node) {

    }

    @Override
    public void visit(IdNode node) {

    }

    @Override
    public void visit(IfNode node) {

    }

    @Override
    public void visit(MemberAccessNode node) {

    }

    @Override
    public void visit(NewNode node) {

    }

    @Override
    public void visit(NonArrayTypeNode node) {

    }

    @Override
    public void visit(ParameterListNode node) {

    }

    @Override
    public void visit(ParameterNode node) {

    }

    @Override
    public void visit(PrefixExprNode node) {

    }

    @Override
    public void visit(ProgramNode node) {

    }

    @Override
    public void visit(ReturnNode node) {

    }

    @Override
    public void visit(SubscriptNode node) {

    }

    @Override
    public void visit(SuffixExprNode node) {

    }

    @Override
    public void visit(ThisNode node) {

    }

    @Override
    public void visit(UnaryOpNode node) {

    }

    @Override
    public void visit(VariableDeclNode node) {

    }

    @Override
    public void visit(VariableDeclStmtNode node) {

    }

    @Override
    public void visit(VariableNode node) {

    }

    @Override
    public void visit(VoidNode node) {

    }

    @Override
    public void visit(WhileNode node) {

    }

    protected Type typeResolver(TypeNode typeNode, GlobalScope globleScope) {
        Type type = globleScope.resolveType(typeNode.getIdentifier(), typeNode.getLocation());
        if (type instanceof ArrayType)
            return new ArrayType(((ArrayType) type).getBaseType(), ((ArrayType) type).getDim());
        else
            return type;
        // No need to create a independent type for ArrayType
    }
}

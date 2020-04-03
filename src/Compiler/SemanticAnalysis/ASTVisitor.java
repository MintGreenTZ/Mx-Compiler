package Compiler.SemanticAnalysis;

import Compiler.AST.*;

public interface ASTVisitor {
    void visit(ArrayTypeNode node);
    void visit(AssignNode node);
    void visit(BinaryOpNode node);
    void visit(BlockNode node);
    void visit(BreakNode node);
    void visit(ClassDeclNode node);
    void visit(ConstBoolNode node);
    void visit(ConstIntNode node);
    void visit(ConstructorNode node);
    void visit(ConstStringNode node);
    void visit(ContinueNode node);
    void visit(ExprListNode node);
    void visit(ExprStmtNode node);
    void visit(ForNode node);
    void visit(FuncCallNode node);
    void visit(FunctionDeclNode node);
    void visit(IdNode node);
    void visit(IfNode node);
    void visit(MemberAccessNode node);
    void visit(NewNode node);
    void visit(NonArrayTypeNode node);
    void visit(ParameterListNode node);
    void visit(ParameterNode node);
    void visit(PrefixExprNode node);
    void visit(ProgramNode node);
    void visit(ReturnNode node);
    void visit(SubscriptNode node);
    void visit(SuffixExprNode node);
    void visit(ThisNode node);
    void visit(UnaryOpNode node);
    void visit(VariableDeclNode node);
    void visit(VariableDeclStmtNode node);
    void visit(VariableNode node);
    void visit(VoidNode node);
    void visit(WhileNode node);
}

package Compiler.SemanticAnalysis;

import Compiler.AST.*;
import Compiler.SymbolTable.ClassSymbol;
import Compiler.SymbolTable.FunctionSymbol;
import Compiler.SymbolTable.Scope.GlobalScope;
import Compiler.SymbolTable.Scope.LocalScope;
import Compiler.SymbolTable.Scope.Scope;
import Compiler.SymbolTable.VariableSymbol;
import Compiler.Utils.SemanticError;

/* This class is used to deal with everything in function body
 * · IdNode, NewNode, VariableDeclNode Symbol recording
 * · this check by curClassSymbol
 * · return check by curFunctionSymbol
 * · break & continue check by LoopDepth
 */

public class FunctionBodyVisitor extends ASTBaseVisitor{
    private GlobalScope globalScope;
    private Scope curScope;
    private ClassSymbol curClassSymbol;
    private FunctionSymbol curFunctionSymbol;
    private int LoopDepth;

    public FunctionBodyVisitor(GlobalScope globalScope) {
        this.globalScope = globalScope;
        curScope = globalScope;
        curClassSymbol = null;
        curFunctionSymbol = null;
        LoopDepth = 0;
    }

    @Override
    public void visit(ProgramNode node) {
        for (DeclNode decl: node.getDecl())
            if (decl instanceof ClassDeclNode || decl instanceof FunctionDeclNode)
                decl.accept(this);
    }

    @Override
    public void visit(ClassDeclNode node) {
        ClassSymbol classSymbol = node.getClassSymbol();
        curClassSymbol = classSymbol;
        curScope = classSymbol;
        for (var funcDeclNode: node.getFunctionDecl())
            funcDeclNode.accept(this);
        curScope = curScope.getUpperScope();
    }

    @Override
    public void visit(FunctionDeclNode node) {
        if (node.getFunctionSymbol() == null)
            node.setFunctionSymbol(new FunctionSymbol(node.getIdentifier(), typeResolver(node.getType(), globalScope), node, curScope));
        FunctionSymbol functionSymbol = node.getFunctionSymbol();
        curFunctionSymbol = functionSymbol;
        curScope = functionSymbol;
        node.getFuncBody().accept(this);
        curScope = curScope.getUpperScope();
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
        curScope = new LocalScope("block scope", curScope);
        for (var stmt : node.getStmtList())
            stmt.accept(this);
        curScope = curScope.getUpperScope();
    }

    @Override
    public void visit(VariableDeclStmtNode node) {
        node.getVariableDecl().accept(this);
    }

    @Override
    public void visit(VariableDeclNode node) {
        node.setDetailedType(typeResolver(node.getType(), globalScope));
        for (var variable: node.getVariables()) {
            variable.setDetailedType(typeResolver(node.getType(), globalScope));
            variable.accept(this);
            curScope.defineVariable(new VariableSymbol(variable.getIdentifier(), typeResolver(node.getType(), globalScope), variable));
        }
    }

    @Override
    public void visit(VariableNode node) {
        if (node.getInitExpr() != null) {
            node.getInitExpr().accept(this);
        }
    }

    @Override
    public void visit(IfNode node) {
        node.getCond().accept(this);
        node.getThenStmt().accept(this);
        if (node.getElseStmt() != null)
            node.getElseStmt().accept(this);
    }

    @Override
    public void visit(WhileNode node) {
        LoopDepth++;
        node.getCond().accept(this);
        node.getLoop().accept(this);
        LoopDepth--;
    }

    @Override
    public void visit(ForNode node) {
        LoopDepth++;
        if (node.getInit() != null) node.getInit().accept(this);
        if (node.getCond() != null) node.getCond().accept(this);
        if (node.getStep() != null) node.getStep().accept(this);
        node.getLoopBody().accept(this);
        LoopDepth--;
    }

    @Override
    public void visit(ReturnNode node) {
        if (curFunctionSymbol == null)
            throw new SemanticError("No function to return.", node.getLocation());
        if (node.getRetValue() != null)
            node.getRetValue().accept(this);
        node.setFunctionSymbol(curFunctionSymbol);
    }

    @Override
    public void visit(BreakNode node) {
        if (LoopDepth == 0)
            throw new SemanticError("No loop to break.", node.getLocation());
    }

    @Override
    public void visit(ContinueNode node) {
        if (LoopDepth == 0)
            throw new SemanticError("No loop to continue.", node.getLocation());
    }

    @Override
    public void visit(ExprStmtNode node) {
        node.getExpr().accept(this);
    }
    /**************************Statement End*****************************/

    /**************************Expression Begin*****************************/
    @Override
    public void visit(ThisNode node) {
        if (curClassSymbol == null)
            throw new SemanticError("This should be in a class.", node.getLocation());
        node.setScope(curClassSymbol);
    }

    @Override
    public void visit(IdNode node) {
        node.setSymbol(curScope.resolveSymbol(node.getIdentifier(), node.getLocation()));
    }

    @Override
    public void visit(SuffixExprNode node) {
        node.getExpr().accept(this);
    }

    @Override
    public void visit(MemberAccessNode node) {
        node.getObj().accept(this);
    }

    @Override
    public void visit(SubscriptNode node) {
        node.getBody().accept(this);
        node.getSubscript().accept(this);
    }

    @Override
    public void visit(FuncCallNode node) {
        node.getFuncObj().accept(this);
        for (var expr: node.getExprList())
            expr.accept(this);
    }

    @Override
    public void visit(PrefixExprNode node) {
        node.getExpr().accept(this);
    }

    @Override
    public void visit(NewNode node) {
        node.setDetailedBaseType(typeResolver(node.getBaseType(), globalScope));
        for (var expr: node.getExprList())
            expr.accept(this);
    }

    @Override
    public void visit(BinaryOpNode node) {
        node.getLhs().accept(this);
        node.getRhs().accept(this);
    }

    @Override
    public void visit(ArrayTypeNode node) {

    }

    @Override
    public void visit(NonArrayTypeNode node) {
        super.visit(node);
    }
    /**************************Expression End*****************************/

}

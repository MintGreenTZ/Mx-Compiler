package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.SymbolTable.ClassSymbol;
import Compiler.Utils.Location;

import java.util.List;

public class ClassDeclNode extends DeclNode {
    private String identifier;
    private FunctionDeclNode constructorDecl;
    private List<VariableDeclNode> variableDecl;
    private List<FunctionDeclNode> functionDecl;
    private ClassSymbol classSymbol;

    public ClassDeclNode(Location location, String identifier, FunctionDeclNode constructorDecl, List<VariableDeclNode> variableDecl, List<FunctionDeclNode> functionDecl) {
        super(location);
        this.identifier = identifier;
        this.constructorDecl = constructorDecl;
        this.variableDecl = variableDecl;
        this.functionDecl = functionDecl;
    }

    public void setClassSymbol(ClassSymbol classSymbol) {
        this.classSymbol = classSymbol;
    }

    public ClassSymbol getClassSymbol() {
        return classSymbol;
    }

    public String getIdentifier() {
        return identifier;
    }

    public List<VariableDeclNode> getVariableDecl() {
        return variableDecl;
    }

    public List<FunctionDeclNode> getFunctionDecl() {
        return functionDecl;
    }

    public FunctionDeclNode getConstructorDecl() {
        return constructorDecl;
    }

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}

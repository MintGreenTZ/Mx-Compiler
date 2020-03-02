package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.Utils.Location;

import java.util.List;

public class ClassDeclNode extends DeclNode {
    private String identifier;
    private List<VariableDeclNode> variableDecl;
    private List<FunctionDeclNode> functionDecl;

    public ClassDeclNode(Location location, String identifier, List<VariableDeclNode> variableDecl, List<FunctionDeclNode> functionDecl) {
        super(location);
        this.identifier = identifier;
        this.variableDecl = variableDecl;
        this.functionDecl = functionDecl;
    }

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}

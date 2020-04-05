package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.Utils.Location;

public class VariableDeclStmtNode extends StmtNode{
    private VariableDeclNode variableDecl;

    public VariableDeclStmtNode(Location location, VariableDeclNode variableDecl) {
        super(location);
        this.variableDecl = variableDecl;
    }

    public VariableDeclNode getVariableDecl() {
        return variableDecl;
    }

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}

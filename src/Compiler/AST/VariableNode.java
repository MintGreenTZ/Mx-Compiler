package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.Utils.Location;

public class VariableNode extends Node {
    private String identifier;
    private ExprNode init;

    public VariableNode(Location location, String identifier, ExprNode init) {
        super(location);
        this.identifier = identifier;
        this.init = init;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}

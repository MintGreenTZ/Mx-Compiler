package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.Utils.Location;

public class ParameterNode extends Node {
    private TypeNode type;
    private String identifier;

    public ParameterNode(Location location, TypeNode type, String identifier) {
        super(location);
        this.type = type;
        this.identifier = identifier;
    }

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}

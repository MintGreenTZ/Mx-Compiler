package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.Utils.Location;

public class ParameterNode extends Node {
    private TypeNode type;
    private VariableNode variable;

    public ParameterNode(Location location, TypeNode type, VariableNode variable) {
        super(location);
        this.type = type;
        this.variable = variable;
    }

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}

package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.Utils.Location;

public class ConstBoolNode extends ConstantNode {
    private boolean value;

    public ConstBoolNode(Location location, boolean value) {
        super(location);
        this.value = value;
    }

    public boolean isValue() {
        return value;
    }

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}

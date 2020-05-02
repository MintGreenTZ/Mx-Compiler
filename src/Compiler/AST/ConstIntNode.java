package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.Utils.Location;

public class ConstIntNode extends ConstantNode {
    private int value;

    public ConstIntNode(Location location, int value) {
        super(location);
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}

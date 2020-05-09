package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.Utils.Location;

public class ConstStringNode extends ConstantNode {
    private String value;

    public ConstStringNode(Location location, String value) {
        super(location);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}

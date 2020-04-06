package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.Utils.Location;

public class ConstNullNode extends ConstantNode {
    public ConstNullNode(Location location) {
        super(location);
    }

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}

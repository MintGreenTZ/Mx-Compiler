package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.Utils.Location;

public class SubscriptNode extends ExprNode {
    private ExprNode array, subscript;

    public SubscriptNode(Location location, ExprNode array, ExprNode subscript) {
        super(location);
        this.array = array;
        this.subscript = subscript;
    }

    public ExprNode getArray() {
        return array;
    }

    public ExprNode getSubscript() {
        return subscript;
    }

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}

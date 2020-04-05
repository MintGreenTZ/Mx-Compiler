package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.Utils.Location;

public class SubscriptNode extends ExprNode {
    private ExprNode body, subscript;

    public SubscriptNode(Location location, ExprNode body, ExprNode subscript) {
        super(location);
        this.body = body;
        this.subscript = subscript;
    }

    public ExprNode getBody() {
        return body;
    }

    public ExprNode getSubscript() {
        return subscript;
    }

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}

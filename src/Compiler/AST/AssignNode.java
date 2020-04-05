package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.Utils.Location;

public class AssignNode extends ExprNode {
    private ExprNode lhs, rhs;

    public AssignNode(Location location, ExprNode lhs, ExprNode rhs) {
        super(location);
        this.lhs = lhs;
        this.rhs = rhs;
    }

    public ExprNode getLhs() {
        return lhs;
    }

    public ExprNode getRhs() {
        return rhs;
    }

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}

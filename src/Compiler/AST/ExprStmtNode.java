package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.Utils.Location;

public class ExprStmtNode extends StmtNode {
    private ExprNode expr;

    public ExprStmtNode(Location location, ExprNode expr) {
        super(location);
        this.expr = expr;
    }

    public ExprNode getExpr() {
        return expr;
    }

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}

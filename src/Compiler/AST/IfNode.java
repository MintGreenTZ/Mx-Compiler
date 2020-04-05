package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.Utils.Location;

public class IfNode extends StmtNode {
    private ExprNode cond;
    private StmtNode thenStmt, elseStmt;

    public IfNode(Location location, ExprNode cond, StmtNode thenStmt, StmtNode elseStmt) {
        super(location);
        this.cond = cond;
        this.thenStmt = thenStmt;
        this.elseStmt = elseStmt;
    }

    public ExprNode getCond() {
        return cond;
    }

    public StmtNode getThenStmt() {
        return thenStmt;
    }

    public StmtNode getElseStmt() {
        return elseStmt;
    }

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}

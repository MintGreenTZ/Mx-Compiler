package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.Utils.Location;

public class WhileNode extends StmtNode {
    private ExprNode cond;
    private StmtNode loop;

    public WhileNode(Location location, ExprNode cond, StmtNode loop) {
        super(location);
        this.cond = cond;
        this.loop = loop;
    }

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}

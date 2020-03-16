package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.Utils.Location;

public class ForNode extends StmtNode{
    private ExprNode init, cond, step;
    private StmtNode loopBody;

    public ForNode(Location location, ExprNode init, ExprNode cond, ExprNode step, StmtNode loopBody) {
        super(location);
        this.init = init;
        this.cond = cond;
        this.step = step;
        this.loopBody = loopBody;
    }

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}

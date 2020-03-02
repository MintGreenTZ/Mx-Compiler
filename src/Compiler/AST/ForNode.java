package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.Utils.Location;

public class ForNode extends StmtNode{
    private ExprNode init, cond, step;

    public ForNode(Location location, ExprNode init, ExprNode cond, ExprNode step) {
        super(location);
        this.init = init;
        this.cond = cond;
        this.step = step;
    }

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}

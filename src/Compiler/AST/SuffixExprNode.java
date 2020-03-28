package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.Utils.Location;

public class SuffixExprNode extends ExprNode {
    public enum Op{SelfADD, SelfSUB};
    private Op op;
    private ExprNode expr;

    public SuffixExprNode(Location location, Op op, ExprNode expr){
        super(location);
        this.op = op;
        this.expr = expr;
    }

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
} {
}

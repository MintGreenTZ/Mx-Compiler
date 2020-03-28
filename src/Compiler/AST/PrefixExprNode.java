package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.Utils.Location;

public class PrefixExprNode extends ExprNode {
    public enum Op{INV, LogicINV, SelfADD, SelfSUB, ADD, SUB};
    private Op op;
    private ExprNode expr;

    public PrefixExprNode(Location location, Op op, ExprNode expr){
        super(location);
        this.op = op;
        this.expr = expr;
    }

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}

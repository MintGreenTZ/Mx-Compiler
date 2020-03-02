package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.Utils.Location;

public class UnaryOpNode extends ExprNode {
    public enum Op{
        sufADD, sufSUB,
        preADD, preSUB, bitNOT, NOT, POS, NEG
    }

    private ExprNode expr;
    private Op op;

    public UnaryOpNode(Location location, Op op, ExprNode expr) {
        super(location);
        this.op = op;
        this.expr = expr;
    }

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}

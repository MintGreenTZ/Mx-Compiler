package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.Utils.Location;

public class BinaryOpNode extends ExprNode {
    public enum Op{
        MUL, DIV, MOD,
        ADD, SUB,
        SHL, SHR,
        LT, LE, GT, GE,
        EQ, NEQ,
        AND, XOR, OR, LAND, LOR,
        ASS
    }

    private ExprNode lhs, rhs;
    public Op op;

    public BinaryOpNode(Location location, ExprNode lhs, Op op, ExprNode rhs) {
        super(location);
        this.lhs = lhs;
        this.op = op;
        this.rhs = rhs;
    }

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}

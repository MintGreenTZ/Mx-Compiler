package Compiler.IR.Inst;

import Compiler.IR.IRVisitor;
import Compiler.IR.Operand.Operand;
import Compiler.Utils.IRError;

public class BinaryOp extends IRInst {
    public enum Op {
        MUL, DIV, MOD,
        ADD, SUB,
        SHL, SHR,
        LT, LE, GT, GE,
        EQ, NEQ,
        AND, XOR, OR
    }

    private Operand lhs, rhs, dest;
    private Op op;

    public BinaryOp(Operand lhs, Operand rhs, Operand dest, Op op) {
        if (lhs == null) throw new IRError("Binary inst lhs is null.");
        this.lhs = lhs;
        this.rhs = rhs;
        this.dest = dest;
        this.op = op;
    }

    public Operand getLhs() {
        return lhs;
    }
    public Operand getRhs() {
        return rhs;
    }
    public Operand getDest() {
        return dest;
    }
    public Op getOp() {
        return op;
    }

    public void accept(IRVisitor visitor) {
        visitor.visit(this);
    }
}

package Compiler.IR.Inst;

import Compiler.IR.IRVisitor;
import Compiler.IR.Operand.Operand;

public class UnaryOp extends IRInst {
    public enum Op{
        INV, NEG
    }

    private Operand acted, dest;
    private Op op;

    public UnaryOp(Operand acted, Operand dest, Op op) {
        this.acted = acted;
        this.dest = dest;
        this.op = op;
    }

    public Operand getActed() {
        return acted;
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

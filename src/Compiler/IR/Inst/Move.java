package Compiler.IR.Inst;

import Compiler.IR.IRVisitor;
import Compiler.IR.Operand.Operand;

public class Move extends IRInst {

    // Move from reg[src] to reg[dest]

    private Operand src, dest;

    public Move(Operand src, Operand dest) {
        this.src = src;
        this.dest = dest;
    }

    public Operand getSrc() {
        return src;
    }
    public Operand getDest() {
        return dest;
    }

    public void accept(IRVisitor visitor) {
        visitor.visit(this);
    }
}

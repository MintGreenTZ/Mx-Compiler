package Compiler.IR.Inst;

import Compiler.IR.IRVisitor;
import Compiler.IR.Operand.Operand;

public class Store extends IRInst {

    // store register [src] to [ptr]

    public Operand src, ptr;

    public Store(Operand src, Operand ptr) {
        this.src = src;
        this.ptr = ptr;
    }

    public Operand getSrc() {
        return src;
    }
    public Operand getPtr() {
        return ptr;
    }

    public void visit(IRVisitor visitor) {
        visitor.visit(this);
    }
}

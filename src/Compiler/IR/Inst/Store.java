package Compiler.IR.Inst;

import Compiler.IR.IRVisitor;
import Compiler.IR.Operand.Operand;
import Compiler.Utils.IRError;

public class Store extends IRInst {

    // store register [src] to [ptr]

    public Operand src, ptr;

    public Store(Operand src, Operand ptr) {
        this.src = src;
        this.ptr = ptr;
        if (ptr == null) throw new IRError("Store without ptr");
    }

    public Operand getSrc() {
        return src;
    }
    public Operand getPtr() {
        return ptr;
    }

    public void accept(IRVisitor visitor) {
        visitor.visit(this);
    }
}

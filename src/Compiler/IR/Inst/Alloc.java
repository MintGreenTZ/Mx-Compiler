package Compiler.IR.Inst;

import Compiler.IR.IRVisitor;
import Compiler.IR.Operand.Operand;

public class Alloc extends IRInst {

    // Alloc [size] memory to pointer [ptr]

    private Operand size, ptr;

    public Alloc(Operand size, Operand ptr) {
        this.size = size;
        this.ptr = ptr;
    }

    public Operand getSize() {
        return size;
    }
    public Operand getPtr() {
        return ptr;
    }

    public void accept(IRVisitor visitor) {
        visitor.visit(this);
    }
}

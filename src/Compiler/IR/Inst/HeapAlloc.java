package Compiler.IR.Inst;

import Compiler.IR.IRVisitor;
import Compiler.IR.Operand.Operand;

public class HeapAlloc extends IRInst {

    // Alloc [size] memory to pointer [ptr]

    private Operand size, ptr;

    public HeapAlloc(Operand size, Operand ptr) {
        this.size = size;
        this.ptr = ptr;
    }

    public Operand getSize() {
        return size;
    }
    public Operand getPtr() {
        return ptr;
    }

    public void visit(IRVisitor visitor) {
        visitor.visit(this);
    }
}

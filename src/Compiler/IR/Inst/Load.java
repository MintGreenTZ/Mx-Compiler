package Compiler.IR.Inst;

import Compiler.IR.IRVisitor;
import Compiler.IR.Operand.Operand;

public class Load extends IRInst {

    // Load from [ptr] to register [dest]

    private Operand dest, ptr;

    public Load(Operand dest, Operand ptr) {
        this.dest = dest;
        this.ptr = ptr;
    }

    public Operand getDest() {
        return dest;
    }
    public Operand getPtr() {
        return ptr;
    }

    public void accept(IRVisitor visitor) {
        visitor.visit(this);
    }
}

package Compiler.IR.Inst;

import Compiler.IR.IRVisitor;
import Compiler.IR.Operand.Operand;

public class Return extends IRInst {
    private Operand ret;

    public Return(Operand ret) {
        this.ret = ret;
    }

    public Operand getRet() {
        return ret;
    }

    public void accept(IRVisitor visitor) {
        visitor.visit(this);
    }
}

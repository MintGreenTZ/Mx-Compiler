package Compiler.IR.Inst;

import Compiler.IR.BasicBlock;
import Compiler.IR.IRVisitor;

public class Jump extends IRInst {
    private BasicBlock BB;

    public Jump(BasicBlock BB) {
        this.BB = BB;
    }

    public BasicBlock getBB() {
        return BB;
    }

    public void accept(IRVisitor visitor) {
        visitor.visit(this);
    }
}

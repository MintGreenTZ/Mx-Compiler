package Compiler.IR.Inst;

import Compiler.IR.BasicBlock;
import Compiler.IR.IRVisitor;

public abstract class IRInst {
    private BasicBlock curBlock;
    private IRInst preInst, sucInst;

    public void setCurBlock(BasicBlock curBlock) {
        this.curBlock = curBlock;
    }
    public void setPreInst(IRInst preInst) {
        this.preInst = preInst;
    }
    public void setSucInst(IRInst sucInst) {
        this.sucInst = sucInst;
    }

    public BasicBlock getCurBlock() {
        return curBlock;
    }
    public IRInst getPreInst() {
        return preInst;
    }
    public IRInst getSucInst() {
        return sucInst;
    }

    public void removeSelf() {
        if (preInst == null)
            curBlock.setFirstInst(sucInst);
        else
            preInst.setSucInst(sucInst);
        if (sucInst == null)
            curBlock.setLastInst(preInst);
        else
            sucInst.setPreInst(preInst);
    }

    public abstract void visit(IRVisitor visitor);
}

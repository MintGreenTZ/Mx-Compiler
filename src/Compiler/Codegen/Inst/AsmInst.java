package Compiler.Codegen.Inst;

import Compiler.Codegen.AsmBasicBlock;
import Compiler.Codegen.AsmVisitor;
import Compiler.IR.Operand.Register;

import java.util.List;

public abstract class AsmInst {
    private AsmBasicBlock curBlock;
    private AsmInst preInst, sucInst;

    public void setCurBlock(AsmBasicBlock curBlock) {
        this.curBlock = curBlock;
    }
    public void setPreInst(AsmInst preInst) {
        this.preInst = preInst;
    }
    public void setSucInst(AsmInst sucInst) {
        this.sucInst = sucInst;
    }

    public AsmBasicBlock getCurBlock() {
        return curBlock;
    }
    public AsmInst getPreInst() {
        return preInst;
    }
    public AsmInst getSucInst() {
        return sucInst;
    }

    public abstract Register getDefReg();
    public abstract List<Register> getUseReg();
    public abstract void replaceDefReg(Register newReg);
    public abstract void replaceUseReg(Register oldReg, Register newReg);

    public void appendInst(AsmInst inst) {
        if (sucInst != null) sucInst.setPreInst(inst);
        inst.setSucInst(sucInst);
        sucInst = inst;
        inst.setPreInst(this);
        inst.setCurBlock(getCurBlock());
        if (getCurBlock().getLastInst() == this)
            getCurBlock().setLastInst(inst);
    }

    public void prependInst(AsmInst inst) {
        if (preInst != null) preInst.setSucInst(inst);
        inst.setPreInst(preInst);
        preInst = inst;
        inst.setSucInst(this);
        inst.setCurBlock(getCurBlock());
        if (getCurBlock().getFirstInst() == this)
            getCurBlock().setFirstInst(inst);
    }

    public abstract void accept(AsmVisitor asmVisitor);
}

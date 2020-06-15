package Compiler.Codegen.Inst;

import Compiler.Codegen.AsmBasicBlock;
import Compiler.Codegen.AsmVisitor;
import Compiler.IR.Operand.Register;

import java.util.Arrays;
import java.util.List;

public class AsmJump extends AsmInst{

    private AsmBasicBlock BB;

    public AsmJump(AsmBasicBlock BB) {
        this.BB = BB;
    }

    public AsmBasicBlock getBB() {
        return BB;
    }

    @Override
    public Register getDefReg() {
        return null;
    }

    @Override
    public List<Register> getUseReg() {
        return Arrays.asList();
    }

    @Override
    public void replaceDefReg(Register newReg) {
        assert false;
    }

    @Override
    public void replaceUseReg(Register oldReg, Register newReg) {
        // do nothing
    }

    public void accept(AsmVisitor visitor) {
        visitor.visit(this);
    }
}

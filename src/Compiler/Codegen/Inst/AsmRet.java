package Compiler.Codegen.Inst;

import Compiler.Codegen.AsmVisitor;
import Compiler.IR.Operand.Register;

import java.util.Arrays;
import java.util.List;

public class AsmRet extends AsmInst{

    public AsmRet() {
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

package Compiler.Codegen.Inst;

import Compiler.Codegen.AsmVisitor;
import Compiler.IR.Operand.Register;

import java.util.Arrays;
import java.util.List;

public class AsmMove extends AsmInst{

    private Register rs1;
    private Register rd;

    public AsmMove(Register rs1, Register rd) {
        this.rs1 = rs1;
        this.rd = rd;
    }

    public Register getRs1() {
        return rs1;
    }
    public Register getRd() {
        return rd;
    }

    @Override
    public Register getDefReg() {
        return rd;
    }

    @Override
    public List<Register> getUseReg() {
        return Arrays.asList(rs1);
    }

    @Override
    public void replaceDefReg(Register newReg) {
        rd = newReg;
    }

    @Override
    public void replaceUseReg(Register oldReg, Register newReg) {
        if (rs1 == oldReg) rs1 = newReg;
    }

    public void accept(AsmVisitor visitor) {
        visitor.visit(this);
    }
}

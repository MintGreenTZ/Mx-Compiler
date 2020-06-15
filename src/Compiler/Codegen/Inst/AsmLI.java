package Compiler.Codegen.Inst;

import Compiler.Codegen.AsmVisitor;
import Compiler.IR.Operand.Imm;
import Compiler.IR.Operand.Register;

import java.util.Arrays;
import java.util.List;

public class AsmLI extends AsmInst{

    private Register rd;
    private Imm imm;

    public AsmLI(Register rd, Imm imm) {
        this.imm = imm;
        this.rd = rd;
    }

    public Imm getImm() {
        return imm;
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
        return Arrays.asList();
    }

    @Override
    public void replaceDefReg(Register newReg) {
        rd = newReg;
    }

    @Override
    public void replaceUseReg(Register oldReg, Register newReg) {
        // do nothing
    }

    public void accept(AsmVisitor visitor) {
        visitor.visit(this);
    }
}

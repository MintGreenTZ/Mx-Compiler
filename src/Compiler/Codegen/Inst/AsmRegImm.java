package Compiler.Codegen.Inst;

import Compiler.Codegen.AsmVisitor;
import Compiler.IR.Operand.Imm;
import Compiler.IR.Operand.Register;

import java.util.Arrays;
import java.util.List;

public class AsmRegImm extends AsmInst{

    public enum Op{
        ADDI, ORI, ANDI, XORI,
        SLTI, SLTIU,
        SLLI, SRAI
    }

    private Register rs1, rd;
    private Imm imm;
    private Op op;

    public AsmRegImm(Register rs1, Imm imm, Register rd, Op op) {
        this.rs1 = rs1;
        this.imm = imm;
        this.rd = rd;
        this.op = op;
    }

    public Register getRs1() {
        return rs1;
    }
    public Register getRd() {
        return rd;
    }
    public Imm getImm() {
        return imm;
    }
    public Op getOp() {
        return op;
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

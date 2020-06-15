package Compiler.Codegen.Inst;

import Compiler.Codegen.AsmVisitor;
import Compiler.IR.Operand.Register;

import java.util.Arrays;
import java.util.List;

public class AsmRegReg extends AsmInst{

    public enum Op{
        ADD, SUB, MUL, DIV, REM,
        SLL, SRA,
        SLT, SLTU,
        AND, OR, XOR
    }

    private Register rs1, rs2, rd;
    private Op op;

    public AsmRegReg(Register rs1, Register rs2, Register rd, Op op) {
        this.rs1 = rs1;
        this.rs2 = rs2;
        this.rd = rd;
        this.op = op;
    }

    public Register getRs1() {
        return rs1;
    }
    public Register getRs2() {
        return rs2;
    }
    public Register getRd() {
        return rd;
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
        return Arrays.asList(rs1, rs2);
    }

    @Override
    public void replaceDefReg(Register newReg) {
        rd = newReg;
    }

    @Override
    public void replaceUseReg(Register oldReg, Register newReg) {
        if (rs1 == oldReg) rs1 = newReg;
        if (rs2 == oldReg) rs2 = newReg;
    }

    public void accept(AsmVisitor visitor) {
        visitor.visit(this);
    }
}

package Compiler.Codegen.Inst;

import Compiler.Codegen.AsmBasicBlock;
import Compiler.Codegen.AsmVisitor;
import Compiler.IR.Operand.Register;

import java.util.Arrays;
import java.util.List;

public class AsmBranch extends AsmInst{
    public enum Op{
        BEQ, BNE, BLE, BGE, BLT, BGT
    }

    AsmBasicBlock BB;
    Op op;
    Register rs1, rs2;

    public AsmBranch(Op op, Register rs1, Register rs2, AsmBasicBlock BB) {
        this.BB = BB;
        this.op = op;
        this.rs1 = rs1;
        this.rs2 = rs2;
    }

    public AsmBasicBlock getBB() {
        return BB;
    }
    public Op getOp() {
        return op;
    }
    public Register getRs1() {
        return rs1;
    }
    public Register getRs2() {
        return rs2;
    }

    @Override
    public Register getDefReg() {
        return null;
    }

    @Override
    public List<Register> getUseReg() {
        return Arrays.asList(rs1, rs2);
    }

    @Override
    public void replaceDefReg(Register newReg) {
        assert false;
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

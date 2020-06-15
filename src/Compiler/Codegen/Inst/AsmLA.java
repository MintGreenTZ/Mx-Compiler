package Compiler.Codegen.Inst;

import Compiler.Codegen.AsmVisitor;
import Compiler.IR.Operand.Register;
import Compiler.IR.Operand.StaticStr;

import java.util.Arrays;
import java.util.List;

public class AsmLA extends AsmInst{

    private Register rd;
    private StaticStr symbol;

    public AsmLA(Register rd, StaticStr symbol) {
        this.symbol = symbol;
        this.rd = rd;
    }

    public Register getRd() {
        return rd;
    }

    public StaticStr getSymbol() {
        return symbol;
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

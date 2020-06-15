package Compiler.Codegen.Inst;

import Compiler.Codegen.AsmFunction;
import Compiler.Codegen.AsmVisitor;
import Compiler.IR.Operand.Register;
import Compiler.Utils.CodegenError;

import java.util.Arrays;
import java.util.List;

public class AsmCall extends AsmInst{

    private AsmFunction asmFunction;

    public AsmCall(AsmFunction asmFunction) {
        if (asmFunction == null)
            throw new CodegenError("AsmFunction is null when created.");
        this.asmFunction = asmFunction;
    }

    public AsmFunction getAsmFunction() {
        return asmFunction;
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

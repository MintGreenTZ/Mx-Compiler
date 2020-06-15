package Compiler.Codegen.Inst;

import Compiler.Codegen.AsmVisitor;
import Compiler.Codegen.Operand.PhysicalRegister;
import Compiler.IR.Operand.Register;
import Compiler.IR.Operand.VirtualRegister;
import Compiler.IR.StackLocation;
import Compiler.IR.StackPointer;
import Compiler.Utils.CodegenError;

import java.util.Arrays;
import java.util.List;

public class AsmLoad extends AsmInst{

    private StackLocation loc;
    private Register rd;
    private int size;

    public AsmLoad(StackLocation loc, Register rd, int size) {
        this.rd = rd;
        this.loc = loc;
        this.size = size;
    }

    public Register getRd() {
        return rd;
    }
    public StackLocation getLoc() {
        return loc;
    }
    public int getSize() {
        return size;
    }

    @Override
    public Register getDefReg() {
        return rd;
    }

    @Override
    public List<Register> getUseReg() {
        if (loc instanceof PhysicalRegister)
            return Arrays.asList((Register) loc);
        else if (loc instanceof VirtualRegister)
            return ((VirtualRegister) loc).isGlobal() ? Arrays.asList() : Arrays.asList((Register) loc);
        else if (loc instanceof StackPointer)
            return Arrays.asList(((StackPointer) loc).getSp());
        else
            throw new CodegenError("Unexpected use reg");
    }

    @Override
    public void replaceDefReg(Register newReg) {
        rd = newReg;
    }

    @Override
    public void replaceUseReg(Register oldReg, Register newReg) {
        if (loc == oldReg) loc = newReg;
    }

    public void accept(AsmVisitor visitor) {
        visitor.visit(this);
    }
}

package Compiler.Codegen.Inst;

import Compiler.Codegen.AsmVisitor;
import Compiler.Codegen.Operand.PhysicalRegister;
import Compiler.IR.Operand.Register;
import Compiler.IR.Operand.VirtualRegister;
import Compiler.IR.StackLocation;
import Compiler.IR.StackPointer;

import java.util.ArrayList;
import java.util.List;

public class AsmStore extends AsmInst{

    private StackLocation loc;
    private Register rs, rt;
    private int size;

    public AsmStore(StackLocation loc, Register rs, Register rt, int size) {
        this.loc = loc;
        this.rs = rs;
        this.rt = rt;
        this.size = size;
    }

    public StackLocation getLoc() {
        return loc;
    }
    public Register getRs() {
        return rs;
    }
    public Register getRt() {
        return rt;
    }
    public int getSize() {
        return size;
    }

    @Override
    public Register getDefReg() {
        return rt;
    }

    @Override
    public List<Register> getUseReg() {
        List<Register> regs = new ArrayList<>();
        regs.add(rs);
        if (loc instanceof PhysicalRegister)
            regs.add((Register) loc);
        else if (loc instanceof VirtualRegister && !((VirtualRegister) loc).isGlobal())
            regs.add((Register) loc);
        else if (loc instanceof StackPointer)
            regs.add(((StackPointer) loc).getSp());
        return regs;
    }

    @Override
    public void replaceDefReg(Register newReg) {
        assert rt != null;
        rt = newReg;
    }

    @Override
    public void replaceUseReg(Register oldReg, Register newReg) {
        if (loc == oldReg) loc = newReg;
        if (rs == oldReg) rs = newReg;
    }

    public void accept(AsmVisitor visitor) {
        visitor.visit(this);
    }
}

package Compiler.IR;

import Compiler.Codegen.AsmFunction;
import Compiler.Codegen.Operand.PhysicalRegister;

public class StackPointer implements StackLocation{

    private AsmFunction asmFunction;
    private int offset;
    private boolean fromBottom;
    private PhysicalRegister sp;

    public StackPointer(AsmFunction asmFunction, int offset, boolean fromBottom, PhysicalRegister sp) {
        this.asmFunction = asmFunction;
        this.offset = offset;
        this.fromBottom = fromBottom;
        this.sp = sp;
    }

    public PhysicalRegister getSp() {
        return sp;
    }

    @Override
    public String toString() {
        int spOffset = offset + (fromBottom ? asmFunction.getNormalizedStackSize() : 0);
        return spOffset + "(sp)";
    }
}

package Compiler.Codegen.Operand;

import Compiler.IR.Operand.Register;

public class PhysicalRegister extends Register {
    public PhysicalRegister() {
    }

    public PhysicalRegister(String identifier) {
        super(identifier);
    }
}

package Compiler.IR.Operand;

public class Register extends Operand {
    private boolean global = false;

    public Register() {}

    public Register(String identifier) {
        super(identifier);
    }

    public void markGlobal() {
        global = true;
    }

    public boolean isGlobal() {
        return global;
    }
}

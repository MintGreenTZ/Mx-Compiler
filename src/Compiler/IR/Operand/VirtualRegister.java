package Compiler.IR.Operand;

public class VirtualRegister extends Register {

    private boolean global = false;

    public VirtualRegister() {
    }

    public VirtualRegister(String identifier) {
        super(identifier);
    }

    public void markGlobal() {
        global = true;
    }

    public boolean isGlobal() {
        return global;
    }
}

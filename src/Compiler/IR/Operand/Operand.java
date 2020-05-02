package Compiler.IR.Operand;

public abstract class Operand {

    private String identifier;

    public Operand() {
    }

    public Operand(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }
}

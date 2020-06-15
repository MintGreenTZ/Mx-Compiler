package Compiler.IR.Operand;

public abstract class Operand {

    private static int unnamedCnt = 0;
    private String identifier;

    public Operand() {
        this.identifier = "unnamed_" + unnamedCnt;
        unnamedCnt += 1;
    }

    public Operand(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }
}

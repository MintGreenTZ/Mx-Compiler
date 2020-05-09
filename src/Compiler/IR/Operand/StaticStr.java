package Compiler.IR.Operand;

public class StaticStr extends Operand {
    private String value;

    public StaticStr(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

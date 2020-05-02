package Compiler.IR.Operand;

public class ConstStr extends Operand {
    private String value;

    public ConstStr(String identifier, String value) {
        super(identifier);
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

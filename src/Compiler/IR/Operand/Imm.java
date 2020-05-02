package Compiler.IR.Operand;

public class Imm extends Operand {
    private int value;

    public Imm(int value) {
        super(Integer.toString(value));
        this.value = value;
    }
}

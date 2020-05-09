package Compiler.SymbolTable;

import Compiler.AST.Node;
import Compiler.IR.Operand.Register;
import Compiler.SymbolTable.Type.Type;

public class VariableSymbol extends Symbol{
    // for IR
    private Register variableReg;
    private int offset;

    public VariableSymbol(String name, Type type, Node definition) {
        super(name, type, definition);
    }

    public void setVariableReg(Register variableReg) {
        this.variableReg = variableReg;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public Register getVariableReg() {
        return variableReg;
    }

    public int getOffset() {
        return offset;
    }
}

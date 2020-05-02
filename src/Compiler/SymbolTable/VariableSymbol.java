package Compiler.SymbolTable;

import Compiler.AST.Node;
import Compiler.IR.Operand.Operand;
import Compiler.IR.Operand.Register;
import Compiler.SymbolTable.Type.Type;

public class VariableSymbol extends Symbol{
    // for IR
    private Register variableReg;

    public VariableSymbol(String name, Type type, Node definition) {
        super(name, type, definition);
    }

    public void setVariableReg(Register variableReg) {
        this.variableReg = variableReg;
    }

    public Register getVariableReg() {
        return variableReg;
    }
}

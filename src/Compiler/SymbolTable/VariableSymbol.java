package Compiler.SymbolTable;

import Compiler.AST.Node;
import Compiler.SymbolTable.Type.Type;

public class VariableSymbol extends Symbol{
    public VariableSymbol(String name, Type type, Node definition) {
        super(name, type, definition);
    }
}

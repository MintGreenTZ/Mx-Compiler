package Compiler.SymbolTable;

import Compiler.AST.Node;
import Compiler.SymbolTable.Type.Type;
import Compiler.Utils.Location;
import Compiler.Utils.SemanticError;

public class PrimitiveSymbol extends Symbol implements Type {
    public PrimitiveSymbol(String name) {
        super(name, null, null);
    }

    @Override
    public String getTypeName() {
        return super.getName();
    }
}

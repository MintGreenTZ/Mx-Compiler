package Compiler.SymbolTable;

import Compiler.AST.Node;
import Compiler.SymbolTable.Type.Type;
import Compiler.Utils.Location;
import Compiler.Utils.SemanticError;

import static Compiler.Configuration.REGISTER_SIZE;

public class PrimitiveSymbol extends Symbol implements Type {
    // int / bool / void

    public PrimitiveSymbol(String name) {
        super(name, null, null);
    }

    @Override
    public String getTypeName() {
        return super.getName();
    }

    @Override
    public boolean isReferenceType() {
        return false;
    }

    @Override
    public int getTypeSize() {
        return REGISTER_SIZE;
    }
}

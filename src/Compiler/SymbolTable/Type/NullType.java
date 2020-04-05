package Compiler.SymbolTable.Type;

import Compiler.Utils.Location;
import Compiler.Utils.SemanticError;

public class NullType implements Type {
    @Override
    public String getTypeName() {
        return "null";
    }
}

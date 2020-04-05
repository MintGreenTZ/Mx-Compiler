package Compiler.SymbolTable.Scope;

import Compiler.SymbolTable.ClassSymbol;
import Compiler.SymbolTable.Symbol;
import Compiler.Utils.Location;
import Compiler.Utils.SemanticError;

public class LocalScope extends CommonScope{
    public LocalScope(String name, Scope upperScope) {
        super(name, upperScope);
    }

    @Override
    public void defineClass(ClassSymbol symbol) {
        throw new SemanticError("Cannot define class in local.", symbol.getDefinition().getLocation());
    }

    @Override
    public Symbol resolveSymbol(String identifier, Location location) {
        Symbol symbol = symbolMap.get(identifier);
        if (symbol != null)
            return symbol;
        else
            return getUpperScope().resolveSymbol(identifier, location);
    }
}

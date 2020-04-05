package Compiler.SymbolTable.Scope;

import Compiler.SymbolTable.ClassSymbol;
import Compiler.SymbolTable.FunctionSymbol;
import Compiler.SymbolTable.Symbol;
import Compiler.SymbolTable.VariableSymbol;
import Compiler.Utils.Location;
import Compiler.Utils.SemanticError;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class CommonScope implements Scope {
    protected Map<String, Symbol> symbolMap = new LinkedHashMap<>();
    private String name;
    private Scope upperScope;

    public CommonScope(String name, Scope upperScope) {
        this.name = name;
        this.upperScope = upperScope;
    }

    @Override
    public Scope getUpperScope() {
        return upperScope;
    }

    @Override
    public void defineVariable(VariableSymbol symbol) {
        if (symbolMap.containsKey(symbol.getName()))
            throw new SemanticError("Duplicate identifiers.", symbol.getDefinition().getLocation());
        symbolMap.put(symbol.getName(), symbol);
    }

    @Override
    public void defineFunction(FunctionSymbol symbol) {
        if (symbolMap.containsKey(symbol.getName()))
            throw new SemanticError("Duplicate identifiers.", symbol.getDefinition().getLocation());
        symbolMap.put(symbol.getName(), symbol);
    }

    @Override
    public Symbol resolveSymbol(String identifier, Location location) {
        return null;
    }
}

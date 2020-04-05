package Compiler.SymbolTable.Scope;

import Compiler.SymbolTable.ClassSymbol;
import Compiler.SymbolTable.PrimitiveSymbol;
import Compiler.SymbolTable.Symbol;
import Compiler.SymbolTable.Type.NullType;
import Compiler.SymbolTable.Type.Type;
import Compiler.Utils.Location;
import Compiler.Utils.SemanticError;

import java.util.LinkedHashMap;
import java.util.Map;

public class GlobalScope extends CommonScope{
    private Map<String, Type> typeMap;

    public GlobalScope(String name, Scope upperScope) {
        super(name, upperScope);
        typeMap = new LinkedHashMap<>();
    }

    @Override
    public void defineClass(ClassSymbol symbol) {
        if (symbolMap.containsKey(symbol.getTypeName()))
            throw new SemanticError("Duplicate identifier.", symbol.getDefinition().getLocation());
        if (typeMap.containsKey(symbol.getTypeName()))
            throw new SemanticError("Duplicate identifier.", symbol.getDefinition().getLocation());
        symbolMap.put(symbol.getTypeName(), symbol);
        typeMap.put(symbol.getTypeName(), symbol);
        symbol.setScope(this);
    }

    public void defineType(PrimitiveSymbol symbol) {
        typeMap.put(symbol.getTypeName(), symbol);
        symbol.setScope(this);
    }

    public void defineNull(NullType symbol) {
        typeMap.put("null", symbol);
    }

    @Override
    public Symbol resolveSymbol(String identifier, Location location) {
        Symbol symbol = symbolMap.get(identifier);
        if (symbol != null) return symbol;
        throw new SemanticError("Unknown symbol.", location);
    }

    public Type resolveType(String identifier, Location location) {
        Type type = typeMap.get(identifier);
        if (type != null) return type;
        throw new SemanticError("Unknown type.", location);
    }
}

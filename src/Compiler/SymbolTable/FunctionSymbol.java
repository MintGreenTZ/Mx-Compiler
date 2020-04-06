package Compiler.SymbolTable;

import Compiler.AST.FunctionDeclNode;
import Compiler.AST.Node;
import Compiler.SymbolTable.Scope.Scope;
import Compiler.SymbolTable.Type.Type;
import Compiler.Utils.Location;
import Compiler.Utils.SemanticError;

import java.util.LinkedHashMap;
import java.util.Map;

public class FunctionSymbol extends Symbol implements Scope {
    private Scope upperScope;
    private Map<String, VariableSymbol> arguments;

    public FunctionSymbol(String name, Type type, FunctionDeclNode definition, Scope upperScope) {
        super(name, type, definition);
        this.upperScope = upperScope;
        this.arguments = new LinkedHashMap<>();
    }

    public void setUpperScope(Scope scope) {
        upperScope = scope;
    }

    @Override
    public Scope getUpperScope() {
        return upperScope;
    }

    public Map<String, VariableSymbol> getArguments() {
        return arguments;
    }

    @Override
    public void defineVariable(VariableSymbol symbol) {
        if (arguments.containsKey(symbol.getName()))
            throw new SemanticError("Duplicate identifiers.", symbol.getDefinition().getLocation());
        else
            arguments.put(symbol.getName(), symbol);
        symbol.setScope(this);
    }

    @Override
    public void defineClass(ClassSymbol symbol) {
        throw new SemanticError("Cannot define class in function.", symbol.getDefinition().getLocation());
    }

    @Override
    public void defineFunction(FunctionSymbol symbol) {
        throw new SemanticError("Cannot define function in function.", symbol.getDefinition().getLocation());
    }

    @Override
    public Symbol resolveSymbol(String identifier, Location location) {
        Symbol symbol = arguments.get(identifier);
        if (symbol != null)
            return symbol;
        else
            return upperScope.resolveSymbol(identifier, location);
    }
}

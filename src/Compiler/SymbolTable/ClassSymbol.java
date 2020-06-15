package Compiler.SymbolTable;

import Compiler.AST.ClassDeclNode;
import Compiler.SymbolTable.Scope.Scope;
import Compiler.SymbolTable.Type.Type;
import Compiler.Utils.Location;
import Compiler.Utils.SemanticError;

import java.util.LinkedHashMap;
import java.util.Map;

import static Compiler.Configuration.POINTER_SIZE;

public class ClassSymbol extends Symbol implements Type, Scope {
    private Map<String, VariableSymbol> variableSymbolMap;
    private Map<String, FunctionSymbol> functionSymbolMap;
    private Scope upperScope;
    private FunctionSymbol constructor;

    // for IR
    private int size;

    public ClassSymbol(String name, ClassDeclNode definition, Scope upperScope) {
        super(name, null, definition);
        this.variableSymbolMap = new LinkedHashMap<>();
        this.functionSymbolMap = new LinkedHashMap<>();
        this.upperScope = upperScope;
        this.constructor = null;
    }

    public FunctionSymbol getConstructor() {
        return constructor;
    }

    public void setConstructor(FunctionSymbol constructor) {
        this.constructor = constructor;
    }

    @Override
    public Scope getUpperScope() {
        return upperScope;
    }

    public Symbol accessMember(String identifier, Location location) {
        if (variableSymbolMap.containsKey(identifier))
            return variableSymbolMap.get(identifier);
        if (functionSymbolMap.containsKey(identifier))
            return functionSymbolMap.get(identifier);
        throw new SemanticError("No such a member.", location);
    }

    @Override
    public String getTypeName() {
        return getName();
    }

    @Override
    public void defineVariable(VariableSymbol symbol) {
        if (variableSymbolMap.containsKey(symbol.getName()) || functionSymbolMap.containsKey(symbol.getName()))
            throw new SemanticError("Duplicate Identifier.", symbol.getDefinition().getLocation());
        variableSymbolMap.put(symbol.getName(), symbol);
        symbol.setScope(this);
        symbol.setOffset(size);
        size += symbol.getType().getTypeSize();
    }

    @Override
    public void defineClass(ClassSymbol symbol) {
        throw new SemanticError("Cannot define class in class.", symbol.getDefinition().getLocation());
    }

    @Override
    public void defineFunction(FunctionSymbol symbol) {
        if (variableSymbolMap.containsKey(symbol.getName()) || functionSymbolMap.containsKey(symbol.getName()))
            throw new SemanticError("Duplicate Identifier.", symbol.getDefinition().getLocation());
        functionSymbolMap.put(symbol.getName(), symbol);
        symbol.markMemberFunction();
        symbol.setScope(this);
    }

    // for IR use, don't need to give location to mark error
    public Symbol resolveSymbol(String identifier) {
        return  resolveSymbol(identifier, new Location(0, 0));
    }

    @Override
    public Symbol resolveSymbol(String identifier, Location location) {
        Symbol variableSymbol = variableSymbolMap.get(identifier);
        Symbol functionSymbol = functionSymbolMap.get(identifier);
        if (variableSymbol != null) return variableSymbol;
        if (functionSymbol != null) return functionSymbol;
        return upperScope.resolveSymbol(identifier, location);
    }

    public Symbol resolveMember(String identifier, Location location) {
        Symbol variableSymbol = variableSymbolMap.get(identifier);
        Symbol functionSymbol = functionSymbolMap.get(identifier);
        if (variableSymbol != null) return variableSymbol;
        if (functionSymbol != null) return functionSymbol;
        throw new SemanticError("No such a member.", location);
    }

    @Override
    public boolean isReferenceType() {
        return true;
    }

    @Override
    public int getTypeSize() {
        return POINTER_SIZE;
    }

    public int getObjSize() {
        return size;
    }
}

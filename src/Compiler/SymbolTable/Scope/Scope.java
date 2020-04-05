package Compiler.SymbolTable.Scope;

import Compiler.SymbolTable.ClassSymbol;
import Compiler.SymbolTable.FunctionSymbol;
import Compiler.SymbolTable.Symbol;
import Compiler.SymbolTable.VariableSymbol;
import Compiler.Utils.Location;

public interface Scope {
    Scope getUpperScope();

    void defineVariable(VariableSymbol symbol);

    void defineClass(ClassSymbol symbol);

    void defineFunction(FunctionSymbol symbol);

    Symbol resolveSymbol(String identifier, Location location);
}

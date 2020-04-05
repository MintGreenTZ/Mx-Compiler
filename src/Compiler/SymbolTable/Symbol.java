package Compiler.SymbolTable;

import Compiler.AST.Node;
import Compiler.SymbolTable.Scope.Scope;
import Compiler.SymbolTable.Type.Type;

public class Symbol{
    private String name;
    private Type type;
    private Scope scope;
    private Node definition;

    public Symbol(String name, Type type, Node definition) {
        this.name = name;
        this.type = type;
        this.definition = definition;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public Scope getScope() {
        return scope;
    }

    public Node getDefinition() {
        return definition;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }
}

package Compiler.AST;

import Compiler.Utils.Location;

abstract public class TypeNode extends Node {
    private String identifier;

    public TypeNode(Location location, String identifier) {
        super(location);
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }
}

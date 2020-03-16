package Compiler.AST;

import Compiler.Utils.Location;

abstract public class ExprNode extends Node {
    private Location location;

    public ExprNode(Location location) {
        super(location);
    }
}

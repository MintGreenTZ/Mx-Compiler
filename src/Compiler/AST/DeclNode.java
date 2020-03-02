package Compiler.AST;

import Compiler.Utils.Location;

abstract public class DeclNode extends Node {
    public DeclNode(Location location) {
        super(location);
    }
}

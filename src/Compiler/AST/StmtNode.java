package Compiler.AST;

import Compiler.Utils.Location;

abstract public class StmtNode extends Node {
    public StmtNode(Location location) {
        super(location);
    }
}

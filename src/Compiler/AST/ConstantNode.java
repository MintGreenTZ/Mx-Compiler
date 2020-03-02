package Compiler.AST;

import Compiler.Utils.Location;

abstract public class ConstantNode extends ExprNode {
    public ConstantNode(Location location) {
        super(location);
    }
}

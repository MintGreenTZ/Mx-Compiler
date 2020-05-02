package Compiler.AST;

import Compiler.IR.Operand.Operand;
import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.Utils.Location;

abstract public class Node {
    private Location location;



    public Node(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    abstract public void accept(ASTVisitor visitor);
}

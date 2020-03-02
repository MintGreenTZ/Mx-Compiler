package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.Utils.Location;

public class ThisNode extends ExprNode{
    public ThisNode(Location location) {
        super(location);
    }

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}

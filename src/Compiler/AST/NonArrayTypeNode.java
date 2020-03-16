package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.Utils.Location;

public class NonArrayTypeNode extends TypeNode{
    public NonArrayTypeNode(Location location, String identifier) {
        super(location, identifier);
    }

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}

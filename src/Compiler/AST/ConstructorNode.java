package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.Utils.Location;

public class ConstructorNode extends Node {
    private String identifier;
    private BlockNode block;

    public ConstructorNode(Location location, String identifier, BlockNode block) {
        super(location);
        this.identifier = identifier;
        this.block = block;
    }

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}

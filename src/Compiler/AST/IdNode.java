package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.Utils.Location;

public class IdNode extends ExprNode {
    private String identifier;

    public IdNode(Location location, String identifier) {
        super(location);
        this.identifier = identifier;
    }

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}

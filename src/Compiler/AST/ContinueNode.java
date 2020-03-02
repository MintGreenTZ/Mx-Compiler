package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.Utils.Location;

public class ContinueNode extends StmtNode{
    public ContinueNode(Location location) {
        super(location);
    }

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}

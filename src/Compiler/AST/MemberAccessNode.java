package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.Utils.Location;

public class MemberAccessNode extends ExprNode {
    private ExprNode obj;
    private String member;

    public MemberAccessNode(Location location, ExprNode obj, String member) {
        super(location);
        this.obj = obj;
        this.member = member;
    }

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}

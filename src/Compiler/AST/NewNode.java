package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.Utils.Location;

import java.util.List;

public class NewNode extends ExprNode {
    private TypeNode type;
    private int dim;
    private List<ExprNode> exprList;

    public NewNode(Location location, TypeNode type, int dim, List<ExprNode> exprList) {
        super(location);
        this.type = type;
        this.dim = dim;
        this.exprList = exprList;
    }

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}

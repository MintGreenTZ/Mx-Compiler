package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.Utils.Location;

import java.util.List;

public class ExprListNode extends Node{
    private List<ExprNode> exprNodeList;

    public ExprListNode(Location location, List<ExprNode> exprNodeList) {
        super(location);
        this.exprNodeList = exprNodeList;
    }

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}

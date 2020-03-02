package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.Utils.Location;

import java.util.List;

public class FuncCallNode extends ExprNode {
    private ExprNode funcObj;
    private List<ExprNode> parameterList;

    public FuncCallNode(Location location, ExprNode funcObj, List<ExprNode> parameterList) {
        super(location);
        this.funcObj = funcObj;
        this.parameterList = parameterList;
    }

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}

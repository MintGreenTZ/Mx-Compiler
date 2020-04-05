package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.Utils.Location;

import java.util.ArrayList;
import java.util.List;

public class FuncCallNode extends ExprNode {
    private ExprNode funcObj;
    private ArrayList<ExprNode> exprList;

    public FuncCallNode(Location location, ExprNode funcObj, ArrayList<ExprNode> exprList) {
        super(location);
        this.funcObj = funcObj;
        this.exprList = exprList;
    }

    public ExprNode getFuncObj() {
        return funcObj;
    }

    public ArrayList<ExprNode> getExprList() {
        return exprList;
    }

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}

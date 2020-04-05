package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.Utils.Location;

public class ReturnNode extends StmtNode{
    private ExprNode retValue;

    public ReturnNode(Location location, ExprNode retValue) {
        super(location);
        this.retValue = retValue;
    }

    public ExprNode getRetValue() {
        return retValue;
    }

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}

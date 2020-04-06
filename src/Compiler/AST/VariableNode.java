package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.SymbolTable.Type.Type;
import Compiler.Utils.Location;

public class VariableNode extends ExprNode {
    private Type detailedType;
    private String identifier;
    private ExprNode initExpr;

    public VariableNode(Location location, String identifier, ExprNode initExpr) {
        super(location);
        this.identifier = identifier;
        this.initExpr = initExpr;
    }

    public void setDetailedType(Type detailedType) {
        this.detailedType = detailedType;
    }

    public Type getDetailedType() {
        return detailedType;
    }

    public String getIdentifier() {
        return identifier;
    }

    public ExprNode getInitExpr() {
        return initExpr;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}

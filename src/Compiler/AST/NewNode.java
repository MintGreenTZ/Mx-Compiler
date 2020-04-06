package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.SymbolTable.Type.Type;
import Compiler.Utils.Location;

import java.util.List;

public class NewNode extends ExprNode {
    private TypeNode baseType;
    private Type detailedBaseType;
    private int dim;
    private List<ExprNode> exprList;

    public NewNode(Location location, TypeNode baseType, int dim, List<ExprNode> exprList) {
        super(location);
        this.baseType = baseType;
        this.dim = dim;
        this.exprList = exprList;
    }

    public TypeNode getBaseType() {
        return baseType;
    }

    public int getDim() {
        return dim;
    }

    public void setDetailedBaseType(Type detailedBaseType) {
        this.detailedBaseType = detailedBaseType;
    }

    public Type getDetailedBaseType() {
        return detailedBaseType;
    }

    public List<ExprNode> getExprList() {
        return exprList;
    }

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}

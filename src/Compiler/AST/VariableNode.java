package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.SymbolTable.Type.Type;
import Compiler.SymbolTable.VariableSymbol;
import Compiler.Utils.Location;

public class VariableNode extends ExprNode {
    private Type detailedType;
    private String identifier;
    private ExprNode initExpr;

    //For IR
    private VariableSymbol variableSymbol;
    private boolean isGlobal = false;
    private boolean isParameter = false;

    public VariableNode(Location location, String identifier, ExprNode initExpr) {
        super(location);
        this.identifier = identifier;
        this.initExpr = initExpr;
    }

    public void setDetailedType(Type detailedType) {
        this.detailedType = detailedType;
    }

    public void setVariableSymbol(VariableSymbol variableSymbol) {
        this.variableSymbol = variableSymbol;
    }

    public void markGlobal() {
        isGlobal = true;
    }

    public void markParameter() {
        isParameter = true;
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

    public VariableSymbol getVariableSymbol() {
        return variableSymbol;
    }

    public boolean isGlobal() {
        return isGlobal;
    }

    public boolean isParameter() {
        return isParameter;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}

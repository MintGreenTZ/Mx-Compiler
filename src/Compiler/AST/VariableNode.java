package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.SymbolTable.Type.Type;
import Compiler.Utils.Location;

public class VariableNode extends Node {
    private Type detailedType;
    private String identifier;
    private ExprNode init;

    public VariableNode(Location location, String identifier, ExprNode init) {
        super(location);
        this.identifier = identifier;
        this.init = init;
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

    public ExprNode getInit() {
        return init;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}

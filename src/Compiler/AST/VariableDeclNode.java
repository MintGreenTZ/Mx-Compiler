package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.SymbolTable.Type.Type;
import Compiler.Utils.Location;

import java.util.List;

public class VariableDeclNode extends DeclNode {
    private TypeNode type;
    private Type detailedType;
    private List<VariableNode> variables;

    public VariableDeclNode(Location location, TypeNode type, List<VariableNode> variables) {
        super(location);
        this.type = type;
        this.variables = variables;
    }

    public TypeNode getType() {
        return type;
    }

    public void setDetailedType(Type detailedType) {
        this.detailedType = detailedType;
    }

    public Type getDetailedType() {
        return detailedType;
    }

    public List<VariableNode> getVariables() {
        return variables;
    }

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}

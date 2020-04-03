package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.Utils.Location;

import java.util.List;

public class VariableDeclNode extends DeclNode {
    private TypeNode type;
    private List<VariableNode> variableNames;

    public VariableDeclNode(Location location, TypeNode type, List<VariableNode> variableNames) {
        super(location);
        this.type = type;
        this.variableNames = variableNames;
    }

    public TypeNode getType() {
        return type;
    }

    public List<VariableNode> getVariableNames() {
        return variableNames;
    }

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}

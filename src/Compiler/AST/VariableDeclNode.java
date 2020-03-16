package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.Utils.Location;

import java.util.List;

public class VariableDeclNode extends DeclNode {
    private TypeNode type;
    private List<IdNode> variableNames;

    public VariableDeclNode(Location location, TypeNode type, List<IdNode> variableNames) {
        super(location);
        this.type = type;
        this.variableNames = variableNames;
    }

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}

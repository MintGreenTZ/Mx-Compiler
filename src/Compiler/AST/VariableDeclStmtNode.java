package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.Utils.Location;

import java.util.List;

public class VariableDeclStmtNode extends StmtNode {
    private TypeNode type;
    private List<VariableNode> variableNames;

    public VariableDeclStmtNode(Location location, TypeNode type, List<VariableNode> variableNames) {
        super(location);
        this.type = type;
        this.variableNames = variableNames;
    }

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}

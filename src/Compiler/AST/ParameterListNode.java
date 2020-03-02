package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.Utils.Location;

import java.util.List;

public class ParameterListNode extends Node {
    private List<VariableDeclNode> parameterList;

    public ParameterListNode(Location location, List<VariableDeclNode> parameterList) {
        super(location);
        this.parameterList = parameterList;
    }

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}

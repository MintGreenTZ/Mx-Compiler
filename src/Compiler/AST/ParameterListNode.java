package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.Utils.Location;

import java.util.List;

public class ParameterListNode extends Node {
    private List<ParameterNode> parameterList;

    public ParameterListNode(Location location, List<ParameterNode> parameterList) {
        super(location);
        this.parameterList = parameterList;
    }

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}

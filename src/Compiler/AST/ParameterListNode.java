package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.Utils.Location;

import java.util.ArrayList;
import java.util.List;

public class ParameterListNode extends Node {
    private ArrayList<ParameterNode> parameterList;

    public ParameterListNode(Location location, ArrayList<ParameterNode> parameterList) {
        super(location);
        this.parameterList = parameterList;
    }

    public ArrayList<ParameterNode> getParameterList() {
        return parameterList;
    }

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}

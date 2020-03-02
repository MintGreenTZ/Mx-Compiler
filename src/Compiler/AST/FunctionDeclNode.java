package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.Utils.Location;

import java.util.List;

public class FunctionDeclNode extends DeclNode{
    private TypeNode type;
    private String identifier;
    private ParameterListNode parameterList;
    private BlockNode funcBody;

    public FunctionDeclNode(Location location, TypeNode type, String identifier, ParameterListNode parameterList, BlockNode funcNode) {
        super(location);
        this.type = type;
        this.identifier = identifier;
        this.parameterList = parameterList;
        this.funcBody = funcNode;
    }

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
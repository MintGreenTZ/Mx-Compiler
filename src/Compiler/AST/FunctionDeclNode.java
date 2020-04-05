package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.SymbolTable.FunctionSymbol;
import Compiler.Utils.Location;

import java.util.ArrayList;
import java.util.List;

public class FunctionDeclNode extends DeclNode{
    private TypeNode type;
    private String identifier;
    private ArrayList<ParameterNode> parameterList;
    private BlockNode funcBody;
    private FunctionSymbol functionSymbol;

    public FunctionDeclNode(Location location, TypeNode type, String identifier, ArrayList<ParameterNode> parameterList, BlockNode funcNode) {
        super(location);
        this.type = type;
        this.identifier = identifier;
        this.parameterList = parameterList;
        this.funcBody = funcNode;
    }

    public String getIdentifier() {
        return identifier;
    }

    public TypeNode getType() {
        return type;
    }

    public FunctionSymbol getFunctionSymbol() {
        return functionSymbol;
    }

    public void setFunctionSymbol(FunctionSymbol functionSymbol) {
        this.functionSymbol = functionSymbol;
    }

    public ArrayList<ParameterNode> getParameterList() {
        return parameterList;
    }

    public BlockNode getFuncBody() {
        return funcBody;
    }

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}

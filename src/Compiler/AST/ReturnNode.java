package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.SymbolTable.FunctionSymbol;
import Compiler.Utils.Location;

public class ReturnNode extends StmtNode{
    private ExprNode retValue;
    private FunctionSymbol functionSymbol;

    public ReturnNode(Location location, ExprNode retValue) {
        super(location);
        this.retValue = retValue;
    }

    public ExprNode getRetValue() {
        return retValue;
    }

    public void setFunctionSymbol(FunctionSymbol functionSymbol) {
        this.functionSymbol = functionSymbol;
    }

    public FunctionSymbol getFunctionSymbol() {
        return functionSymbol;
    }

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}

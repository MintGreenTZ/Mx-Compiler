package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.SymbolTable.Symbol;
import Compiler.Utils.Location;

public class MemberAccessNode extends ExprNode {
    private ExprNode obj;
    private String member;
    private Symbol symbol;

    public MemberAccessNode(Location location, ExprNode obj, String member) {
        super(location);
        this.obj = obj;
        this.member = member;
    }

    public ExprNode getObj() {
        return obj;
    }

    public String getMember() {
        return member;
    }

    public void setSymbol(Symbol symbol) {
        this.symbol = symbol;
    }

    public Symbol getSymbol() {
        return symbol;
    }

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}

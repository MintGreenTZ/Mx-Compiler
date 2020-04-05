package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.SymbolTable.Symbol;
import Compiler.Utils.Location;

public class IdNode extends ExprNode {
    private String identifier;
    private Symbol symbol;

    public IdNode(Location location, String identifier) {
        super(location);
        this.identifier = identifier;
    }

    public void setSymbol(Symbol symbol) {
        this.symbol = symbol;
    }

    public String getIdentifier() {
        return identifier;
    }

    public Symbol getSymbol() {
        return symbol;
    }

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}

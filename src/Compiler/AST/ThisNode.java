package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.SymbolTable.Scope.Scope;
import Compiler.Utils.Location;

public class ThisNode extends ExprNode{
    private Scope scope;

    public ThisNode(Location location) {
        super(location);
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public Scope getScope() {
        return scope;
    }

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}

package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.Utils.Location;

import java.util.List;

public class VariableDeclNode extends DeclNode {
    private TypeNode type;
    private List<IdNode> obj;

    public VariableDeclNode(Location location, TypeNode type, List<IdNode> obj) {
        super(location);
        this.type = type;
        this.obj = obj;
    }

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}

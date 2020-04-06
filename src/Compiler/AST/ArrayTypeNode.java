package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.Utils.Location;

public class ArrayTypeNode extends TypeNode{
    private TypeNode baseType;
    private int dim;

    public ArrayTypeNode(Location location, TypeNode subType) {
        super(location, subType.getIdentifier());
        if (subType instanceof ArrayTypeNode) {
            this.baseType = ((ArrayTypeNode) subType).baseType;
            dim = ((ArrayTypeNode) subType).dim + 1;
        }
        else {
            this.baseType = subType;
            this.dim = 1;
        }
    }

    public TypeNode getBaseType() {
        return baseType;
    }

    public int getDim() {
        return dim;
    }

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}

package Compiler.SymbolTable.Type;

import Compiler.Utils.Location;
import Compiler.Utils.SemanticError;

import javax.lang.model.type.NullType;

public class ArrayType implements Type{
    private Type baseType;
    private int dim;

    public ArrayType(Type baseType, int dim) {
        this.baseType = baseType;
        this.dim = dim;
    }

    @Override
    public String getTypeName() {
        return baseType.getTypeName() + " " + dim + "-dim array";
    }

    public Type getBaseType() {
        return baseType;
    }

    public int getDim() {
        return dim;
    }
}

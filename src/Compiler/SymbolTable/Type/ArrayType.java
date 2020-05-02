package Compiler.SymbolTable.Type;

import Compiler.Utils.Location;
import Compiler.Utils.SemanticError;

import javax.lang.model.type.NullType;

import static Compiler.Configuration.POINTER_SIZE;
import static Compiler.Configuration.REGISTER_SIZE;

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

    @Override
    public boolean isReferenceType() {
        return true;
    }

    @Override
    public int getTypeSize() {
        return POINTER_SIZE;
    }
}

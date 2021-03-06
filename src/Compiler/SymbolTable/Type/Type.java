package Compiler.SymbolTable.Type;

import Compiler.Utils.Location;

public interface Type {
    String getTypeName();

    boolean isReferenceType();

    int getTypeSize();
}

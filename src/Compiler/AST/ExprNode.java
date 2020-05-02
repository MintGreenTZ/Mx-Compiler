package Compiler.AST;

import Compiler.IR.BasicBlock;
import Compiler.IR.Operand.Operand;
import Compiler.SymbolTable.FunctionSymbol;
import Compiler.SymbolTable.Type.Type;
import Compiler.Utils.Location;

abstract public class ExprNode extends Node {
    public enum Category{
        LVALUE, RVALUE, CLASS, FUNCTION
    }

    private Location location;
    private Category category;
    private Type type;
    private FunctionSymbol functionSymbol;
    // for IR
    private Operand IRResult;

    public ExprNode(Location location) {
        super(location);
        this.category = Category.RVALUE;
        type = null;
        functionSymbol = null;
    }

    public boolean isInt() {
        return type.getTypeName().equals("int");
    }

    public boolean isString() {
        return type.getTypeName().equals("string");
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Category getCategory() {
        return category;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public void setFunctionSymbol(FunctionSymbol functionSymbol) {
        this.functionSymbol = functionSymbol;
    }

    public FunctionSymbol getFunctionSymbol() {
        return functionSymbol;
    }

    public boolean isLvalue() {
        return category == Category.LVALUE;
    }

    public void setIRResult(Operand IRResult) {
        this.IRResult = IRResult;
    }

    public Operand getIRResult() {
        return IRResult;
    }
}

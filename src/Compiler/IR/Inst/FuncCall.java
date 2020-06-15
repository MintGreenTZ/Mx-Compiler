package Compiler.IR.Inst;

import Compiler.IR.Function;
import Compiler.IR.IRVisitor;
import Compiler.IR.Operand.Operand;
import Compiler.Utils.IRError;

import java.util.ArrayList;

public class FuncCall extends IRInst {
    // function(paralist) -> dest

    private Function function;
    private Operand referenceToThisClass; // record class obj in case of class member function
    private ArrayList<Operand> paralist;
    private Operand dest;

    public FuncCall(Function function, Operand referenceToThisClass, ArrayList<Operand> paralist, Operand dest) {
        if (function == null) throw new IRError("null pointer when construct funcCall");
        this.function = function;
        this.referenceToThisClass = referenceToThisClass;
        this.paralist = paralist;
        this.dest = dest;
    }

    public Function getFunction() {
        return function;
    }
    public Operand getReferenceToThisClass() {
        return referenceToThisClass;
    }
    public ArrayList<Operand> getParalist() {
        return paralist;
    }
    public Operand getDest() {
        return dest;
    }

    public void accept(IRVisitor visitor) {
        visitor.visit(this);
    }
}

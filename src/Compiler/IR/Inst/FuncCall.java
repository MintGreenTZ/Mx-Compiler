package Compiler.IR.Inst;

import Compiler.IR.Function;
import Compiler.IR.IRVisitor;
import Compiler.IR.Operand.Operand;

import java.util.ArrayList;

public class FuncCall extends IRInst {
    // function(paralist) -> dest

    private Function function;
    private Operand obj;
    private ArrayList<Operand> paralist;
    private Operand dest;

    public FuncCall(Function function, Operand obj, ArrayList<Operand> paralist, Operand dest) {
        this.function = function;
        this.obj = obj;
        this.paralist = paralist;
        this.dest = dest;
    }

    public Function getFunction() {
        return function;
    }
    public Operand getObj() {
        return obj;
    }
    public ArrayList<Operand> getParalist() {
        return paralist;
    }
    public Operand getDest() {
        return dest;
    }

    public void visit(IRVisitor visitor) {
        visitor.visit(this);
    }
}

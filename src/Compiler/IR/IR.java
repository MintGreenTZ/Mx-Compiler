package Compiler.IR;

import Compiler.IR.Operand.Register;
import Compiler.IR.Operand.StaticStr;

import java.util.ArrayList;

public class IR {
    private ArrayList<Function> functions = new ArrayList<>();
    private ArrayList<StaticStr> staticStrs = new ArrayList<>();
    private ArrayList<Register> globalVariables = new ArrayList<>();

    public void addFunction(Function function) {functions.add(function);}
    public void addStaticStr(StaticStr staticStr) {staticStrs.add(staticStr);}
    public void addGlobalVariable(Register globalVariable) {globalVariables.add(globalVariable);}

    public ArrayList<Function> getFunctions() {
        return functions;
    }
    public ArrayList<StaticStr> getStaticStrs() {
        return staticStrs;
    }
    public ArrayList<Register> getGlobalVariables() {return globalVariables; }
}

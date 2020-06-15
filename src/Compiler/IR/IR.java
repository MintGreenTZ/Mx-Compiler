package Compiler.IR;

import Compiler.IR.Operand.StaticStr;
import Compiler.IR.Operand.VirtualRegister;

import java.util.ArrayList;

public class IR {
    private ArrayList<Function> functions = new ArrayList<>();
    private ArrayList<StaticStr> staticStrs = new ArrayList<>();
    private ArrayList<VirtualRegister> globalVariables = new ArrayList<>();

    public void addFunction(Function function) {functions.add(function);}
    public void addStaticStr(StaticStr staticStr) {staticStrs.add(staticStr);}
    public void addGlobalVariable(VirtualRegister globalVariable) {globalVariables.add(globalVariable);}

    public ArrayList<Function> getFunctions() {
        return functions;
    }
    public ArrayList<StaticStr> getStaticStrs() {
        return staticStrs;
    }
    public ArrayList<VirtualRegister> getGlobalVariables() {return globalVariables; }
}

package Compiler.IR;

import Compiler.IR.Inst.Return;
import Compiler.IR.Operand.Operand;
import Compiler.IR.Operand.Register;

import java.util.ArrayList;

public class Function {
    private String identifier;
    private BasicBlock entryBB, exitBB;
    private ArrayList<Register> args = new ArrayList<>();
    private ArrayList<Return> returns = new ArrayList<>();
    private Register referenceToCurClass;
    private boolean isBuiltin = false;
    private Operand memberObj;

    public Function(String identifier) {
        this.identifier = identifier;
    }

    public void setEntryBB(BasicBlock entryBB) {
        this.entryBB = entryBB;
    }
    public void setExitBB(BasicBlock exitBB) {
        this.exitBB = exitBB;
    }
    public void setReferenceToCurClass(Register referenceToCurClass) {
        this.referenceToCurClass = referenceToCurClass;
    }
    public void addArg(Register arg) {args.add(arg);}
    public void addReturnInst(Return inst) {returns.add(inst);}
    public void markBuiltin() {isBuiltin = true;}
    public void setMemberObj(Operand memberObj) {
        this.memberObj = memberObj;
    }

    public BasicBlock getEntryBB() {
        return entryBB;
    }
    public BasicBlock getExitBB() {
        return exitBB;
    }
    public Register getReferenceToCurClass() {
        return referenceToCurClass;
    }
    public ArrayList<Return> getReturns() {
        return returns;
    }
    public boolean isBuiltin() {
        return isBuiltin;
    }
    public Operand getMemberObj() {
        return memberObj;
    }
}

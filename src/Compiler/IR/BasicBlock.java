package Compiler.IR;

import Compiler.IR.Inst.IRInst;

import java.util.ArrayList;

public class BasicBlock {
    private String name;
    private ArrayList<BasicBlock> successors = new ArrayList<>();
    private IRInst firstInst, lastInst;

    public BasicBlock() {
    }

    public BasicBlock(String name) {
        this.name = name;
    }

    public void addSuccessor(BasicBlock successor) {
        successors.add(successor);
    }

    public String getName() {
        return name;
    }
    public ArrayList<BasicBlock> getSuccessors() {
        return successors;
    }
    public IRInst getFirstInst() {
        return firstInst;
    }
    public IRInst getLastInst() {
        return lastInst;
    }

    public void setFirstInst(IRInst firstInst) {
        this.firstInst = firstInst;
    }
    public void setLastInst(IRInst lastInst) {
        this.lastInst = lastInst;
    }

    public void addInst(IRInst inst) {
        if (firstInst == null) {
            firstInst = lastInst = inst;
            inst.setPreInst(null);
            inst.setSucInst(null);
        } else {
            lastInst.setSucInst(inst);
            inst.setPreInst(lastInst);
            inst.setSucInst(null);
            lastInst = inst;
        }
    }
}

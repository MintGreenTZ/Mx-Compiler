package Compiler.IR;

import Compiler.IR.Inst.Branch;
import Compiler.IR.Inst.IRInst;
import Compiler.IR.Inst.Jump;
import Compiler.IR.Inst.Return;
import Compiler.Utils.IRError;

import java.util.ArrayList;
import java.util.List;

public class BasicBlock {
    private String name;
    private IRInst firstInst, lastInst;
    private boolean terminated = false;

    // for DFS
    private BasicBlock parentBB;
    private List<BasicBlock> preBBList;
    private List<BasicBlock> sucBBList;

    public BasicBlock() {
    }

    public BasicBlock(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public IRInst getFirstInst() {
        return firstInst;
    }
    public IRInst getLastInst() {
        return lastInst;
    }

    public void addInst(IRInst inst) {
        if (terminated) return;
        sudoAddInst(inst);
    }

    public void sudoAddInst(IRInst inst) {
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
        inst.setCurBlock(this);
    }

    public void addLastInst(IRInst inst) {
        addInst(inst);
        terminated = true;
    }

    public void makeSucBBList() {
        sucBBList = new ArrayList<>();
        if (lastInst instanceof Jump)
            sucBBList.add(((Jump) lastInst).getBB());
        else if (lastInst instanceof Branch) {
            sucBBList.add(((Branch) lastInst).getThenBB());
            if (((Branch) lastInst).getThenBB() != ((Branch) lastInst).getElseBB())
                sucBBList.add(((Branch) lastInst).getElseBB());
        }
        else if (!(lastInst instanceof Return))
            throw new IRError("Ir Block terminated unexpectedly.");
    }

    public void setFirstInst(IRInst firstInst) {
        this.firstInst = firstInst;
    }
    public void setLastInst(IRInst lastInst) {
        this.lastInst = lastInst;
    }
    public void setParentBB(BasicBlock parentBB) {
        this.parentBB = parentBB;
    }
    public void setPreBBList(List<BasicBlock> preBBList) {
        this.preBBList = preBBList;
    }

    public List<BasicBlock> getPreBBList() {
        return preBBList;
    }
    public List<BasicBlock> getSucBBList() {
        return sucBBList;
    }
}

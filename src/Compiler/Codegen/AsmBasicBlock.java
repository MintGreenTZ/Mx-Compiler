package Compiler.Codegen;

import Compiler.Codegen.Inst.AsmBranch;
import Compiler.Codegen.Inst.AsmInst;
import Compiler.Codegen.Inst.AsmJump;
import Compiler.IR.Operand.Register;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AsmBasicBlock {

    private String identifier;
    private AsmInst firstInst, lastInst;

    // liveness analysis
    private Set<Register> def = new HashSet<>();
    private Set<Register> use = new HashSet<>();
    private Set<Register> liveIn = new HashSet<>();
    private Set<Register> liveOut = new HashSet<>();

    // for DFS
    private AsmBasicBlock parentBB;
    private List<AsmBasicBlock> preBBList;
    private List<AsmBasicBlock> sucBBList;

    public AsmBasicBlock(String identifier) {
        this.identifier = identifier;
    }

    public void setFirstInst(AsmInst firstInst) {
        this.firstInst = firstInst;
    }
    public void setLastInst(AsmInst lastInst) {
        this.lastInst = lastInst;
    }
    public void setLiveIn(Set<Register> liveIn) {
        this.liveIn = liveIn;
    }
    public void setLiveOut(Set<Register> liveOut) {
        this.liveOut = liveOut;
    }

    public String getIdentifier() {
        return identifier;
    }
    public AsmInst getFirstInst() {
        return firstInst;
    }
    public AsmInst getLastInst() {
        return lastInst;
    }
    public Set<Register> getDef() {
        return def;
    }
    public Set<Register> getUse() {
        return use;
    }
    public Set<Register> getLiveIn() {
        return liveIn;
    }
    public Set<Register> getLiveOut() {
        return liveOut;
    }

    public void appendInst(AsmInst inst) {
        if (firstInst == null) { //empty
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

    public void makeSucBBList() {
        sucBBList = new ArrayList<>();
        // AsmBB finish with (Jump -> AsmJump) or (Branch -> AsmBranch, AsmJump)
        if (lastInst != null) {
            if (lastInst.getPreInst() != null)
                sucBBListAppend(lastInst.getPreInst());
            sucBBListAppend(lastInst);
        }
        // ensure uniqueness in case of branch to same palace
        sucBBList = new ArrayList<>(new HashSet<>(sucBBList));
    }

    private void sucBBListAppend(AsmInst inst) {
        if (inst instanceof AsmJump)
            sucBBList.add(((AsmJump) inst).getBB());
        if (inst instanceof AsmBranch)
            sucBBList.add(((AsmBranch) inst).getBB());
    }

    public void setParentBB(AsmBasicBlock parentBB) {
        this.parentBB = parentBB;
    }
    public void setPreBBList(List<AsmBasicBlock> preBBList) {
        this.preBBList = preBBList;
    }

    public List<AsmBasicBlock> getPreBBList() {
        return preBBList;
    }
    public List<AsmBasicBlock> getSucBBList() {
        return sucBBList;
    }
}

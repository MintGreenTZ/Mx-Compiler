package Compiler.IR;

import Compiler.IR.Inst.Return;
import Compiler.IR.Operand.Operand;
import Compiler.IR.Operand.Register;
import Compiler.IR.Operand.VirtualRegister;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Function {
    private String identifier;
    private BasicBlock entryBB, exitBB;
    private ArrayList<Register> args = new ArrayList<>();
    private ArrayList<Return> returns = new ArrayList<>();
    private boolean isBuiltin = false;
    private VirtualRegister referenceToThisClass;

    private List<BasicBlock> BBList;
    private Set<BasicBlock> visited;

    public Function(String identifier) {
        this.identifier = identifier;
    }

    public void setEntryBB(BasicBlock entryBB) {
        this.entryBB = entryBB;
    }
    public void setExitBB(BasicBlock exitBB) {
        this.exitBB = exitBB;
    }
    public void addArg(Register arg) {args.add(arg);}
    public void addReturnInst(Return inst) {returns.add(inst);}
    public void markBuiltin() {isBuiltin = true;}
    public void setReferenceToThisClass(VirtualRegister referenceToThisClass) {
        this.referenceToThisClass = referenceToThisClass;
    }

    public String getIdentifier() {
        return identifier;
    }
    public BasicBlock getEntryBB() {
        return entryBB;
    }
    public BasicBlock getExitBB() {
        return exitBB;
    }
    public ArrayList<Return> getReturns() {
        return returns;
    }
    public boolean isBuiltin() {
        return isBuiltin;
    }
    public Operand getReferenceToThisClass() {
        return referenceToThisClass;
    }
    public ArrayList<Register> getArgs() {
        return args;
    }

    // dfs BBs to gather all the BB for Asm use

    public void makeBBList() {
        BBList = new ArrayList<>();
        visited = new HashSet<>();
        dfsBB(entryBB, null);
    }

    public void dfsBB(BasicBlock BB, BasicBlock parentBB) {
        if (BB == null || visited.contains(BB)) {
            if (BB != null && !BB.getPreBBList().contains(parentBB)) // enter BB in another way
                BB.getPreBBList().add(parentBB);
            return;
        }
        visited.add(BB);
        BBList.add(BB);
        BB.setPreBBList(parentBB == null ? new ArrayList<>() : new ArrayList<>(){{add(parentBB);}});
        BB.setParentBB(parentBB);
        BB.makeSucBBList();
        for (BasicBlock exitEntry: BB.getSucBBList())
            dfsBB(exitEntry, BB);
    }

    public List<BasicBlock> getBBList() {
        return BBList;
    }
}

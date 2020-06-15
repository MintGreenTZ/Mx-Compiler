package Compiler.Codegen;

import Compiler.IR.Function;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AsmFunction {

    private String identifier;
    private AsmBasicBlock entryBB;
    private AsmBasicBlock exitBB;
    private boolean isBuiltin;
    private int stackSizeFromTop = 0;
    private int stackSizeFromBottom = 0;

    private List<AsmBasicBlock> BBList;
    private Set<AsmBasicBlock> visited;

    public AsmFunction(Function function) {
        this.identifier = function.getIdentifier();
        this.isBuiltin = function.isBuiltin();
    }

    public AsmFunction(String identifier, boolean isBuiltin) {
        this.identifier = identifier;
        this.isBuiltin = isBuiltin;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
    public void setEntryBB(AsmBasicBlock entryBB) {
        this.entryBB = entryBB;
    }
    public void setExitBB(AsmBasicBlock exitBB) {
        this.exitBB = exitBB;
    }

    public String getIdentifier() {
        return identifier;
    }
    public AsmBasicBlock getEntryBB() {
        return entryBB;
    }
    public AsmBasicBlock getExitBB() {
        return exitBB;
    }
    public boolean isBuiltin() {
        return isBuiltin;
    }

    public void setStackSizeFromTopBound(int stackSizeFromTop) {
        this.stackSizeFromTop = Integer.max(this.stackSizeFromTop, stackSizeFromTop);
    }

    public int stackAllocFromBottom(int size) {
        stackSizeFromBottom += size;
        return -stackSizeFromBottom;
    }

    public int getNormalizedStackSize() {
        return (stackSizeFromTop + stackSizeFromBottom + 15) / 16 * 16;
    }
    // dfs BBs to gather all the BB for as Function.java

    public void makeBBList() {
        BBList = new ArrayList<>();
        visited = new HashSet<>();
        dfsBB(entryBB, null);
    }

    public void dfsBB(AsmBasicBlock BB, AsmBasicBlock parentBB) {
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
        for (AsmBasicBlock exitEntry: BB.getSucBBList())
            dfsBB(exitEntry, BB);
    }

    public List<AsmBasicBlock> getBBList() {
        return BBList;
    }
}

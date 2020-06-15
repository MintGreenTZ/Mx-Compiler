package Compiler.Codegen;

import Compiler.Codegen.Inst.*;
import Compiler.Codegen.Operand.PhysicalRegister;
import Compiler.IR.Operand.RegValue;
import Compiler.IR.Operand.Register;
import Compiler.IR.StackPointer;

import java.util.*;

import static Compiler.Configuration.REGISTER_SIZE;

public class RegisterAllocation {

    private Assembly asm;
    private int K;

    private Set<Register> preColored        = new LinkedHashSet<>();
    private Set<Register> initial           = new LinkedHashSet<>();
    private Set<Register> simplifyWorklist  = new LinkedHashSet<>();
    private Set<Register> freezeWorklist    = new LinkedHashSet<>();
    private Set<Register> spillWorklist     = new LinkedHashSet<>();
    private Set<Register> spilledNodes      = new LinkedHashSet<>();
    private Set<Register> coalescedNodes    = new LinkedHashSet<>();
    private Set<Register> coloredNodes      = new LinkedHashSet<>();
    private Stack<Register> selectStack     = new Stack<>();

    private Set<AsmMove> coalescedMoves     = new LinkedHashSet<>();
    private Set<AsmMove> constrainedMoves   = new LinkedHashSet<>();
    private Set<AsmMove> frozenMoves        = new LinkedHashSet<>();
    private Set<AsmMove> worklistMoves      = new LinkedHashSet<>();
    private Set<AsmMove> activeMoves        = new LinkedHashSet<>();

    private Set<Edge> adjEdges              = new LinkedHashSet<>();

    class Edge {
        Register u, v;

        public Edge(Register u, Register v) {
            this.u = u;
            this.v = v;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Edge edge = (Edge) o;
            return u == edge.u && v == edge.v;
        }

        @Override
        public int hashCode() {
            return Objects.hash(u, v);
        }
    }

    public RegisterAllocation(Assembly asm) {
        this.asm = asm;
        this.K = asm.allocatableRegName.length;
        initialization();
    }

    private void initialization() {
        for (AsmFunction func: asm.getAsmFunctionList())
            if (!func.isBuiltin()) {
                while (true) {
                    clean();
                    initialization(func);
                    livenessAnalysis(func);
                    build(func);
                    workListInit();
                    while (!simplifyWorklist.isEmpty() || !worklistMoves.isEmpty() || !freezeWorklist.isEmpty() || !spillWorklist.isEmpty()) {
                        if (!simplifyWorklist.isEmpty())
                            simplify();
                        else if (!worklistMoves.isEmpty())
                            coalesce();
                        else if (!freezeWorklist.isEmpty())
                            freeze();
                        else
                            selectSpill();
                    }
                    assignColors();
                    if (!spilledNodes.isEmpty())
                        rewriteProgram(func);
                    else
                        break;
                }
                applyColoring(func);
            }
    }

    private void clean() {
        preColored.clear();
        initial.clear();
        simplifyWorklist.clear();
        freezeWorklist.clear();
        spillWorklist.clear();
        spilledNodes.clear();
        coalescedNodes.clear();
        coloredNodes.clear();
        selectStack.clear();

        coalescedMoves.clear();
        constrainedMoves.clear();
        frozenMoves.clear();
        worklistMoves.clear();
        activeMoves.clear();

        adjEdges.clear();
    }

    private void initialization(AsmFunction asmFunction) {
        asmFunction.makeBBList();

        // precolor
        for (String name: asm.phyRegName) {
            PhysicalRegister pReg = asm.getPhyReg(name);
            pReg.color = name;
            pReg.degree = Integer.MAX_VALUE;
            preColored.add(pReg);
        }

        initial.addAll(preColored);
        for (var BB: asmFunction.getBBList())
            for (var inst = BB.getFirstInst(); inst != null; inst = inst.getSucInst()) {
                if (inst.getDefReg() != null) initial.add(inst.getDefReg());
                initial.addAll(inst.getUseReg());
            }

        for (var reg: initial)
            reg.clean();

        initial.removeAll(preColored);

        for (var reg: initial) {
            reg.color = null;
            reg.degree = 0;
        }
    }

    private void livenessAnalysis(AsmFunction asmFunction) {
        for (AsmBasicBlock BB:asmFunction.getBBList()) {
            BB.getDef().clear();
            BB.getUse().clear();
            BB.getLiveIn().clear();
            BB.getLiveOut().clear();
            for (AsmInst inst = BB.getFirstInst(); inst != null; inst = inst.getSucInst()) {
                for (Register use: inst.getUseReg())
                    if (!BB.getDef().contains(use))
                        BB.getUse().add(use);
                if (inst.getDefReg() != null)
                    BB.getDef().add(inst.getDefReg());
                if (inst instanceof AsmCall)
                    for (String callerRegName: asm.callerRegisterName)
                        BB.getDef().add(asm.getPhyReg(callerRegName));
            }
        }
        // liveness Analysis
        boolean changed = true;
        while (changed) {
            changed = false;
            for (AsmBasicBlock BB: asmFunction.getBBList()) {
                Set<Register> newLiveIn = new LinkedHashSet<>(BB.getLiveOut());
                Set<Register> newLiveOut = new LinkedHashSet<>();
                newLiveIn.removeAll(BB.getDef());
                newLiveIn.addAll(BB.getUse());
                for (var sucBB: BB.getSucBBList())
                    newLiveOut.addAll(sucBB.getLiveIn());
                if (!newLiveIn.equals(BB.getLiveIn()) || !newLiveOut.equals(BB.getLiveOut())) {
                    BB.setLiveIn(newLiveIn);
                    BB.setLiveOut(newLiveOut);
                    changed = true;
                }
            }
        }
    }

    private void build(AsmFunction asmFunction) {
        for (AsmBasicBlock BB: asmFunction.getBBList()) {
            Set<Register> live = new LinkedHashSet<>(BB.getLiveOut());
            for (AsmInst inst = BB.getLastInst(); inst != null; inst = inst.getPreInst()) {

                if (inst instanceof AsmMove) {
                    live.removeAll(inst.getUseReg());
                    inst.getDefReg().moveList.add((AsmMove) inst);
                    for (var reg: inst.getUseReg())
                        reg.moveList.add((AsmMove) inst);
                    worklistMoves.add((AsmMove) inst);
                }

                List<Register> defs = new ArrayList<>();
                if (inst.getDefReg() != null)
                    defs.add(inst.getDefReg());
                if (inst instanceof AsmCall)
                    for (var name: asm.callerRegisterName)
                        defs.add(asm.getPhyReg(name));

                if(inst instanceof AsmStore && ((AsmStore) inst).getRt() != null)
                    addEdge(((AsmStore) inst).getRs(), ((AsmStore) inst).getRt());
                for (var def: defs)
                    for (var reg: live)
                        addEdge(def, reg);
                live.removeAll(defs);
                live.addAll(inst.getUseReg());
            }
        }
    }

    private void workListInit() {
        for (var reg: initial)
            if (reg.degree >= K)
                spillWorklist.add(reg);
            else if (moveRelated(reg))
                freezeWorklist.add(reg);
            else
                simplifyWorklist.add(reg);
        initial.clear();
    }

    private void simplify() {
        Register reg = simplifyWorklist.iterator().next();
        simplifyWorklist.remove(reg);
        selectStack.push(reg);
        for (var adj: adjacent(reg))
            decDegree(adj);
    }

    private void coalesce() {
        var moveIns = worklistMoves.iterator().next();
        Register x = getAlias(moveIns.getRs1());
        Register y = getAlias(moveIns.getRd());
        Register u = preColored.contains(y) ? y : x;
        Register v = preColored.contains(y) ? x : y;
        worklistMoves.remove(moveIns);
        if (u == v) {
            coalescedMoves.add(moveIns);
            addWorkList(u);
        } else if (preColored.contains(v) || adjEdges.contains(new Edge(u, v))) {
            constrainedMoves.add(moveIns);
            addWorkList(u);
            addWorkList(v);
        } else {
            boolean flag = true;
            if (preColored.contains(u))
                for (Register adj: adjacent(v))
                    flag &= check(adj, u);
            else {
                var tmp = adjacent(u);
                tmp.addAll(adjacent(v));
                flag = withoutMuchStrongNeighbour(tmp);
            }
            if (flag) {
                coalescedMoves.add(moveIns);
                combine(u, v);
                addWorkList(u);
            } else
                activeMoves.add(moveIns);
        }
    }

    private void freeze() {
        var reg = freezeWorklist.iterator().next();
        freezeWorklist.remove(reg);
        simplifyWorklist.add(reg);
        freezeMoves(reg);
    }

    private void selectSpill() {
        var reg = spillWorklist.iterator().next();
        spillWorklist.remove(reg);
        simplifyWorklist.add(reg);
        freezeMoves(reg);
    }

    private void assignColors() {
        while (!selectStack.isEmpty()) {
            Register reg = selectStack.pop();
            Set<String> candidates = new LinkedHashSet<>(Arrays.asList(asm.allocatableRegName));
            for (var adj: reg.adjList) {
                var realAdj = getAlias(adj);
                if (coloredNodes.contains(realAdj) || preColored.contains(realAdj))
                    candidates.remove(realAdj.color);
            }
            if (candidates.isEmpty())
                spilledNodes.add(reg);
            else {
                coloredNodes.add(reg);
                // assign to caller-save first to optimize
                String color = null;
                for (var r: asm.callerRegisterName)
                    if (candidates.contains(r)) {
                        color = r;
                        break;
                    }
                if (color == null)
                    for (var r: asm.calleeRegisterName)
                        if (candidates.contains(r)) {
                            color = r;
                            break;
                        }
                assert color != null;
                reg.color = color;
            }
        }
        for (Register reg: coalescedNodes)
            reg.color = getAlias(reg).color;
    }

    private void rewriteProgram(AsmFunction func) {
        for (var spilledReg: spilledNodes)
            spilledReg.spillAddr = new StackPointer(func, func.stackAllocFromBottom(REGISTER_SIZE), true, asm.getPhyReg("sp"));
        for (var BB: func.getBBList()) {
            AsmInst nextInst;
            for (AsmInst inst = BB.getFirstInst(); inst != null; inst = nextInst) {
                nextInst = inst.getSucInst();
                for (var useReg: inst.getUseReg())
                    if (useReg.spillAddr != null) {
                        var tmp = new RegValue("spill_use_" + useReg.getIdentifier());
                        inst.prependInst(new AsmLoad(useReg.spillAddr, tmp, REGISTER_SIZE));
                        inst.replaceUseReg(useReg, tmp);
                    }
                if (inst.getDefReg() != null && inst.getDefReg().spillAddr != null) {
                    var defReg = inst.getDefReg();
                    var tmp = new RegValue("spill_def_" + defReg.getIdentifier());
                    inst.replaceDefReg(tmp);
                    inst.appendInst(new AsmStore(defReg.spillAddr, tmp, null, REGISTER_SIZE));
                }
            }
        }
    }

    private void applyColoring(AsmFunction func) {
        for (var BB: func.getBBList())
            for (AsmInst inst = BB.getFirstInst(); inst != null; inst = inst.getSucInst()) {
                for (var useReg: inst.getUseReg()) {
                    if (!(useReg instanceof PhysicalRegister) && useReg.color != null)
                        inst.replaceUseReg(useReg, asm.getPhyReg(useReg.color));
                }
                var defReg = inst.getDefReg();
                if (defReg != null && !(defReg instanceof PhysicalRegister) && defReg.color != null)
                    inst.replaceDefReg(asm.getPhyReg(defReg.color));
            }
    }

    private Set<AsmMove> nodeMoves(Register reg) {
        // return reg.moveList \cap (activeMoves \cup worklistMoves)
        return new LinkedHashSet<>(reg.moveList) {{
            retainAll(new LinkedHashSet<>(activeMoves) {{
                addAll(worklistMoves);
            }});
        }};
    }

    private boolean moveRelated(Register reg) {
        return !nodeMoves(reg).isEmpty();
    }

    private Set<Register> adjacent(Register reg) {
        return new LinkedHashSet<>(reg.adjList) {{
            removeAll(selectStack);
            removeAll(coalescedNodes);
        }};
    }

    private void decDegree(Register reg) {
        reg.degree = reg.degree - 1;
        if (reg.degree == K - 1) {
            Set<Register> regs = adjacent(reg);
            regs.add(reg);
            enableMoves(regs);
            spillWorklist.remove(reg);
            if (moveRelated(reg))
                freezeWorklist.add(reg);
            else
                simplifyWorklist.add(reg);
        }
    }

    private void enableMoves(Set<Register> regs) {
        for (var reg: regs)
            for (AsmMove asmMove: nodeMoves(reg))
                if (activeMoves.contains(asmMove)) {
                    activeMoves.remove(asmMove);
                    worklistMoves.add(asmMove);
                }
    }

    private Register getAlias(Register reg) {
        return coalescedNodes.contains(reg) ? getAlias(reg.alias) : reg;
    }

    private void addWorkList(Register reg) {
        if (!preColored.contains(reg) && !moveRelated(reg) && reg.degree < K) {
            freezeWorklist.remove(reg);
            simplifyWorklist.add(reg);
        }
    }

    private boolean check(Register u, Register v) {
        return u.degree < K || preColored.contains(u) || adjEdges.contains(new Edge(u, v));
    }

    private boolean withoutMuchStrongNeighbour(Set<Register> neighbours) {
        int cnt = 0;
        for (var neighbour: neighbours) {
            if (neighbour.degree >= K)
                cnt += 1;
        }
        return cnt < K;
    }

    private void combine(Register u, Register v) {
        freezeWorklist.remove(v);
        spillWorklist.remove(v);
        coalescedNodes.add(v);
        v.alias = u;
        u.moveList.addAll(v.moveList);
        enableMoves(Set.of(v));
        for (var adj: adjacent(v)) {
            addEdge(adj, u);
            decDegree(adj);
        }
        if (u.degree >= K && freezeWorklist.contains(u)) {
            freezeWorklist.remove(u);
            spillWorklist.add(u);
        }
    }

    private void addEdge(Register u, Register v) {
        if (u == v || adjEdges.contains(new Edge(u, v))) return;
        adjEdges.add(new Edge(u, v));
        adjEdges.add(new Edge(v, u));
        if (!preColored.contains(u)) {
            u.adjList.add(v);
            u.degree += 1;
        }
        if (!preColored.contains(v)) {
            v.adjList.add(u);
            v.degree += 1;
        }
    }

    private void freezeMoves(Register u) {
        for (var moveIns: nodeMoves(u)) {
            Register v = getAlias(moveIns.getRs1()) == getAlias(u) ? getAlias(moveIns.getRd()) : getAlias(moveIns.getRs1());
            activeMoves.remove(moveIns);
            frozenMoves.add(moveIns);
            if (freezeWorklist.contains(v) && nodeMoves(v).isEmpty()) {
                freezeWorklist.remove(v);
                simplifyWorklist.add(v);
            }
        }
    }
}

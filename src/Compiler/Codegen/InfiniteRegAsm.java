package Compiler.Codegen;

import Compiler.Codegen.Inst.*;
import Compiler.Codegen.Operand.PhysicalRegister;
import Compiler.IR.*;
import Compiler.IR.Inst.*;
import Compiler.IR.Operand.*;
import Compiler.Utils.CodegenError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static Compiler.Codegen.Inst.AsmBranch.Op.BNE;
import static Compiler.Configuration.REGISTER_SIZE;

public class InfiniteRegAsm implements IRVisitor {

    private IR ir;
    private Assembly asm = new Assembly();

    private Map<BasicBlock, AsmBasicBlock> BB2AsmBB = new HashMap<>();
    private Map<Function, AsmFunction> func2AsmFunc = new HashMap<>();
    private Map<String, VirtualRegister> savedReg = new HashMap<>();

    private AsmFunction curFunction;
    private AsmBasicBlock curBB;

    public InfiniteRegAsm(IR ir) {
        this.ir = ir;
        initialize();
    }

    public PhysicalRegister str2PhyReg(String name) {
        return asm.getPhyReg(name);
    }

    private void initialize() {
        asm.setGlobalVariableList(new ArrayList<>(ir.getGlobalVariables()));
        asm.setStaticStrList(new ArrayList<>(ir.getStaticStrs()));

        for (Function func: ir.getFunctions())
            func2AsmFunc.put(func, new AsmFunction(func));

        for (Function func: ir.getFunctions())
            if (!func.isBuiltin()) {
                func.makeBBList();
                for (BasicBlock BB: func.getBBList())
                    BB2AsmBB.put(BB, new AsmBasicBlock(BB.getName()));
            }

        for (Function func: ir.getFunctions())
            if (!func.isBuiltin())
                visit(func);
    }

    public void visit(Function func) {
        curFunction = func2AsmFunc.get(func);
        curBB = BB2AsmBB.get(func.getEntryBB());

        AsmFunction asmFunction = curFunction;
        asm.addFunction(asmFunction);
        asmFunction.setEntryBB(curBB);

        // save callee-save registers
        savedReg.clear();
        for (String calleeRegName: asm.calleeRegisterName) {
            RegValue backup = new RegValue("backup_" + calleeRegName);
            curBB.appendInst(new AsmMove(str2PhyReg(calleeRegName), backup));
            savedReg.put(calleeRegName, backup);
        }
        RegValue backup_ra = new RegValue("backup_ra");
        curBB.appendInst(new AsmMove(str2PhyReg("ra"), backup_ra));
        savedReg.put("ra", backup_ra);

        // save args
        List<Operand> paraList = new ArrayList<>();
        if (func.getReferenceToThisClass() != null)
            paraList.add(func.getReferenceToThisClass());
        paraList.addAll(func.getArgs());
        for (int i = 0; i < Integer.min(8, paraList.size()); i++)
            curBB.appendInst(new AsmMove(str2PhyReg("a" + i), (Register) paraList.get(i)));
        for (int i = 8; i < paraList.size(); i++)
            curBB.appendInst(new AsmLoad(new StackPointer(asmFunction, REGISTER_SIZE * (i - 8), true, str2PhyReg("sp"))
                    , (Register) paraList.get(i), REGISTER_SIZE));

        for (BasicBlock BB: func.getBBList())
            visit(BB);
        asmFunction.setExitBB(BB2AsmBB.get(func.getExitBB()));
        asmFunction.makeBBList();
    }

    public void visit(BasicBlock BB) {
        curBB = BB2AsmBB.get(BB);
        for (IRInst inst = BB.getFirstInst(); inst != null; inst = inst.getSucInst())
            inst.accept(this);
    }

    @Override
    public void visit(BinaryOp inst) {
        if (inst.getLhs() instanceof Imm && inst.getRhs() instanceof Imm) {
            curBB.appendInst(new AsmLI((Register) inst.getDest(), new Imm(IRBuilder.calc(inst.getOp(), ((Imm)inst.getLhs()).getValue(), ((Imm) inst.getRhs()).getValue()))));
            return;
        }
        boolean reg2reg = inst.getLhs() instanceof Register && inst.getRhs() instanceof Register;
        // case of reg2imm no such interface
        reg2reg = reg2reg || inst.getOp() == BinaryOp.Op.SUB || inst.getOp() == BinaryOp.Op.MUL || inst.getOp() == BinaryOp.Op.DIV || inst.getOp() == BinaryOp.Op.MOD;
        reg2reg = reg2reg || (inst.getLhs() instanceof Imm && (inst.getOp() == BinaryOp.Op.SHL || inst.getOp() == BinaryOp.Op.SHR));
        if (reg2reg) {
            Register rs1 = immStr2Reg(inst.getLhs());
            Register rs2 = immStr2Reg(inst.getRhs());
            Register rd = immStr2Reg(inst.getDest());
            switch (inst.getOp()) {
                case ADD:   curBB.appendInst(new AsmRegReg(rs1, rs2, rd, AsmRegReg.Op.ADD));    break;
                case SUB:   curBB.appendInst(new AsmRegReg(rs1, rs2, rd, AsmRegReg.Op.SUB));    break;
                case MUL:   curBB.appendInst(new AsmRegReg(rs1, rs2, rd, AsmRegReg.Op.MUL));    break;
                case DIV:   curBB.appendInst(new AsmRegReg(rs1, rs2, rd, AsmRegReg.Op.DIV));    break;
                case MOD:   curBB.appendInst(new AsmRegReg(rs1, rs2, rd, AsmRegReg.Op.REM));    break;
                case SHL:   curBB.appendInst(new AsmRegReg(rs1, rs2, rd, AsmRegReg.Op.SLL));    break;
                case SHR:   curBB.appendInst(new AsmRegReg(rs1, rs2, rd, AsmRegReg.Op.SRA));    break;
                case GT:    curBB.appendInst(new AsmRegReg(rs2, rs1, rd, AsmRegReg.Op.SLT));    break;
                case LT:    curBB.appendInst(new AsmRegReg(rs1, rs2, rd, AsmRegReg.Op.SLT));    break;
                case GE:
                    RegValue tmpGE = new RegValue("tmpGE");
                    curBB.appendInst(new AsmRegReg(rs1, rs2, tmpGE, AsmRegReg.Op.SLT));
                    curBB.appendInst(new AsmRegImm(tmpGE, new Imm(1), rd, AsmRegImm.Op.XORI));
                    break;
                case LE:
                    RegValue tmpLE = new RegValue("tmpLE");
                    curBB.appendInst(new AsmRegReg(rs2, rs1, tmpLE, AsmRegReg.Op.SLT));
                    curBB.appendInst(new AsmRegImm(tmpLE, new Imm(1), rd, AsmRegImm.Op.XORI));
                    break;
                case EQ:
                    RegValue tmpEQ = new RegValue("tmpEQ");
                    curBB.appendInst(new AsmRegReg(rs1, rs2, tmpEQ, AsmRegReg.Op.SUB));
                    curBB.appendInst(new AsmRegImm(tmpEQ, new Imm(1), rd, AsmRegImm.Op.SLTIU));
                    break;
                case NEQ:
                    RegValue tmpNEQ = new RegValue("tmpNEQ");
                    curBB.appendInst(new AsmRegReg(rs1, rs2, tmpNEQ, AsmRegReg.Op.SUB));
                    curBB.appendInst(new AsmRegReg(str2PhyReg("zero"), tmpNEQ, rd, AsmRegReg.Op.SLTU));
                    break;
                case AND:   curBB.appendInst(new AsmRegReg(rs1, rs2, rd, AsmRegReg.Op.AND));    break;
                case OR:    curBB.appendInst(new AsmRegReg(rs1, rs2, rd, AsmRegReg.Op.OR));     break;
                case XOR:   curBB.appendInst(new AsmRegReg(rs1, rs2, rd, AsmRegReg.Op.XOR));    break;
            }
        } else { // reg2imm
            // ensure lhs is reg & rhs is imm first
            boolean wrongOrder = inst.getLhs() instanceof Imm;
            Register rs1 = wrongOrder ? (Register) inst.getRhs() : (Register) inst.getLhs();
            Imm imm = wrongOrder ? (Imm) inst.getLhs() : (Imm) inst.getRhs();
            Register rd = (Register) inst.getDest();
            if (wrongOrder) {
                switch (inst.getOp()) {
                    case LT:    inst.setOp(BinaryOp.Op.GT); break;
                    case LE:    inst.setOp(BinaryOp.Op.GE); break;
                    case GT:    inst.setOp(BinaryOp.Op.LT); break;
                    case GE:    inst.setOp(BinaryOp.Op.LE); break;
                }
            }

            switch (inst.getOp()) {
                case ADD:   curBB.appendInst(new AsmRegImm(rs1, imm, rd, AsmRegImm.Op.ADDI));               break;

                case SUB:
                case MUL:
                case DIV:
                case MOD:   throw new CodegenError("No such interface.");

                case SHL:   curBB.appendInst(new AsmRegImm(rs1, imm, rd, AsmRegImm.Op.SLLI));               break;
                case SHR:   curBB.appendInst(new AsmRegImm(rs1, imm, rd, AsmRegImm.Op.SRAI));               break;
                case LT:    curBB.appendInst(new AsmRegImm(rs1, imm, rd, AsmRegImm.Op.SLTI));               break;
                case GT:    curBB.appendInst(new AsmRegReg(immStr2Reg(imm), rs1, rd, AsmRegReg.Op.SLT));    break;
                case LE:
                    RegValue tmpLE = new RegValue("tmpLE");
                    curBB.appendInst(new AsmRegReg(immStr2Reg(imm), rs1, tmpLE, AsmRegReg.Op.SLT));
                    curBB.appendInst(new AsmRegImm(tmpLE, new Imm(1), rd, AsmRegImm.Op.XORI));
                    break;
                case GE:
                    RegValue tmpGE = new RegValue("tmpGE");
                    curBB.appendInst(new AsmRegImm(rs1, imm, tmpGE, AsmRegImm.Op.SLTI));
                    curBB.appendInst(new AsmRegImm(tmpGE, new Imm(1), rd, AsmRegImm.Op.XORI));
                    break;
                case EQ:
                    RegValue tmpEQ = new RegValue("tmpEQ");
                    curBB.appendInst(new AsmRegReg(rs1, immStr2Reg(imm), tmpEQ, AsmRegReg.Op.SUB));
                    curBB.appendInst(new AsmRegImm(tmpEQ, new Imm(1), rd, AsmRegImm.Op.SLTIU));
                    break;
                case NEQ:
                    RegValue tmpNEQ = new RegValue("tmpNEQ");
                    curBB.appendInst(new AsmRegReg(rs1, immStr2Reg(imm), tmpNEQ, AsmRegReg.Op.SUB));
                    curBB.appendInst(new AsmRegReg(str2PhyReg("zero"), tmpNEQ, rd, AsmRegReg.Op.SLTU));
                    break;
                case AND:   curBB.appendInst(new AsmRegImm(rs1, imm, rd, AsmRegImm.Op.ANDI));    break;
                case OR:    curBB.appendInst(new AsmRegImm(rs1, imm, rd, AsmRegImm.Op.ORI));     break;
                case XOR:   curBB.appendInst(new AsmRegImm(rs1, imm, rd, AsmRegImm.Op.XORI));    break;
            }
        }
    }

    @Override
    public void visit(Branch inst) {
        curBB.appendInst(new AsmBranch(BNE, immStr2Reg(inst.getCond()), str2PhyReg("zero"), BB2AsmBB.get(inst.getThenBB())));
        curBB.appendInst(new AsmJump(BB2AsmBB.get(inst.getElseBB())));
    }

    @Override
    public void visit(FuncCall inst) {
        // paras
        ArrayList<Operand> paraList = new ArrayList<>();
        if (inst.getReferenceToThisClass() != null)
            paraList.add(inst.getReferenceToThisClass());
        paraList.addAll(inst.getParalist());
        for (int i = 0; i < Integer.min(8, paraList.size()); i++)
            curBB.appendInst(new AsmMove(immStr2Reg(paraList.get(i)), str2PhyReg("a" + i)));
        for (int i = 8; i < paraList.size(); i++)
            curBB.appendInst(new AsmStore(new StackPointer(curFunction, REGISTER_SIZE * (i - 8), false, str2PhyReg("sp"))
                    , immStr2Reg(paraList.get(i)), null, REGISTER_SIZE));

        curFunction.setStackSizeFromTopBound(REGISTER_SIZE * Integer.max(0, paraList.size() - 8));

        curBB.appendInst(new AsmCall(func2AsmFunc.get(inst.getFunction())));
        if (inst.getDest() != null)
            curBB.appendInst(new AsmMove(str2PhyReg("a0"), (Register) inst.getDest()));
    }

    @Override
    public void visit(Alloc inst) {
        curBB.appendInst(new AsmMove(immStr2Reg(inst.getSize()), str2PhyReg("a0")));
        curBB.appendInst(new AsmCall(asm.asmMalloc));
        if (inst.getPtr() != null)
            curBB.appendInst(new AsmMove(str2PhyReg("a0"), (Register) inst.getPtr()));
    }

    @Override
    public void visit(Jump inst) {
        curBB.appendInst(new AsmJump(BB2AsmBB.get(inst.getBB())));
    }

    @Override
    public void visit(Load inst) {
        curBB.appendInst(new AsmLoad((Register) inst.getPtr(), (Register) inst.getDest(), REGISTER_SIZE));
    }

    @Override
    public void visit(Move inst) {
        curBB.appendInst(new AsmMove(immStr2Reg(inst.getSrc()), (Register) inst.getDest()));
    }

    @Override
    public void visit(Return inst) {
        if (inst.getRet() != null)
            curBB.appendInst(new AsmMove(immStr2Reg(inst.getRet()), str2PhyReg("a0")));
        for (var entry: savedReg.entrySet())
            curBB.appendInst(new AsmMove(entry.getValue(), str2PhyReg(entry.getKey())));
        curBB.appendInst(new AsmRet());
    }

    @Override
    public void visit(Store inst) {
        Register globalRt = null;
        if (((VirtualRegister) inst.getPtr()).isGlobal())
            globalRt = new RegValue("store_rt");
        curBB.appendInst(new AsmStore((Register) inst.getPtr(), immStr2Reg(inst.getSrc()), globalRt, REGISTER_SIZE));
    }

    @Override
    public void visit(UnaryOp inst) {
        Register rs1 = immStr2Reg(inst.getActed());
        Register rd = (Register) inst.getDest();
        switch (inst.getOp()) {
            case INV:   curBB.appendInst(new AsmRegImm(rs1, new Imm(-1), rd, AsmRegImm.Op.XORI));       break;
            case NEG:   curBB.appendInst(new AsmRegReg(str2PhyReg("zero"), rs1, rd, AsmRegReg.Op.SUB)); break;
        }
    }

    private Register immStr2Reg(Operand operand) {
        if (operand instanceof Register) return (Register) operand;
        if (operand instanceof Imm) {
            if(((Imm) operand).getValue() == 0) return str2PhyReg("zero");
            RegValue reg = new RegValue("imm" + ((Imm) operand).getValue());
            curBB.appendInst(new AsmLI(reg, (Imm) operand));
            return reg;
        }
        if (operand instanceof StaticStr) {
            RegValue reg = new RegValue("str");
            curBB.appendInst(new AsmLA(reg, (StaticStr) operand));
            return reg;
        }
        throw new CodegenError("Unexpected type when immStr2Reg");
    }

    public Assembly getAsm() {
        return asm;
    }
}

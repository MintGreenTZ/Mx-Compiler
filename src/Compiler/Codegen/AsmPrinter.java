package Compiler.Codegen;

import Compiler.Codegen.Inst.*;
import Compiler.Codegen.Operand.PhysicalRegister;
import Compiler.IR.Operand.Imm;
import Compiler.IR.Operand.Register;
import Compiler.IR.Operand.VirtualRegister;
import Compiler.Utils.CodegenError;

import java.io.PrintStream;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class AsmPrinter implements AsmVisitor {

    private Assembly asm;
    PrintStream output;

    Set<AsmBasicBlock> visitedBB = new LinkedHashSet<>();
    Map<AsmBasicBlock, String> BB2Name = new LinkedHashMap<>();
    int nameCnt = 0;

    public AsmPrinter(Assembly asm) {
        this.asm = asm;
        setSP();
        mainReplacing();
        builtinFunctionRenaming();
    }

    public void run(PrintStream printStream) {
        output = printStream;
        indPrintln(".section" + "\t" + ".data" + "\n");
        for (var globalVar: asm.getGlobalVariableList()) {
            indPrintln(".globl" + "\t" + globalVar.getIdentifier());
            println(globalVar.getIdentifier() + ":");
            indPrintln(".zero" + "\t" + "4");
            println("");
        }
        for (var staticStr: asm.getStaticStrList()) {
            indPrintln(".globl" + "\t" + staticStr.getIdentifier());
            println(staticStr.getIdentifier() + ":");
            indPrintln(".string" + "\t" + staticStr.getValue());
            println("");
        }
        indPrintln(".text" + "\n");
        for (var func: asm.getAsmFunctionList())
            if (!func.isBuiltin())
                visit(func);
    }

    public void visit(AsmFunction func) {
        indPrintln(".globl" + "\t" + func.getIdentifier());
        println(func.getIdentifier() + ":");
        for (var BB: func.getBBList())
            visit(BB);
        println("");
    }

    public void visit(AsmBasicBlock BB) {
        println(BB2Str(BB) + ":");
        for (var inst = BB.getFirstInst(); inst != null; inst = inst.getSucInst())
            inst.accept(this);
    }

    @Override
    public void visit(AsmBranch inst) {
        indPrintln(inst.getOp().toString().toLowerCase() + "\t" + inst.getRs1().getIdentifier()
                + ", " + inst.getRs2().getIdentifier() + ", " + BB2Str(inst.getBB()));
    }

    @Override
    public void visit(AsmCall inst) {
        indPrintln("call" + "\t" + inst.getAsmFunction().getIdentifier());
    }

    @Override
    public void visit(AsmJump inst) {
        indPrintln("j" + "\t" + BB2Str(inst.getBB()));
    }

    @Override
    public void visit(AsmLA inst) {
        indPrintln("la" + "\t" + inst.getRd().getIdentifier() + ", " + inst.getSymbol().getIdentifier());
    }

    @Override
    public void visit(AsmLI inst) {
        indPrintln("li" + "\t" + inst.getRd().getIdentifier() + ", " + inst.getImm().getIdentifier());
    }

    @Override
    public void visit(AsmLoad inst) {
        String op;
        switch (inst.getSize()) {
            case 1: op = "lb"; break;
            case 2: op = "lh"; break;
            case 4: op = "lw"; break;
            default: throw new CodegenError("No such interface with load.");
        }
        var loc = inst.getLoc();
        String src;
        if (loc instanceof PhysicalRegister)
            src = "0(" + ((PhysicalRegister) loc).getIdentifier() + ")";
        else if (loc instanceof VirtualRegister)
            src = ((VirtualRegister) loc).getIdentifier();
        else
            src = loc.toString();
        indPrintln(op + "\t" + inst.getRd().getIdentifier() + ", " + src);
    }

    @Override
    public void visit(AsmMove inst) {
        indPrintln("mv" + "\t" + inst.getRd().getIdentifier() + ", " + inst.getRs1().getIdentifier());
    }

    @Override
    public void visit(AsmRegImm inst) {
        indPrintln(inst.getOp().toString().toLowerCase() + "\t" + inst.getRd().getIdentifier()
            + ", " + inst.getRs1().getIdentifier() + ", " + inst.getImm().getIdentifier());
    }

    @Override
    public void visit(AsmRegReg inst) {
        indPrintln(inst.getOp().toString().toLowerCase() + "\t" + inst.getRd().getIdentifier()
                + ", " + inst.getRs1().getIdentifier() + ", " + inst.getRs2().getIdentifier());
    }

    @Override
    public void visit(AsmRet inst) {
        indPrintln("ret");
    }

    @Override
    public void visit(AsmStore inst) {
        String op;
        switch (inst.getSize()) {
            case 1: op = "sb"; break;
            case 2: op = "sh"; break;
            case 4: op = "sw"; break;
            default: throw new CodegenError("No such interface with store.");
        }
        if (inst.getRt() != null) { // global
            assert inst.getLoc() instanceof VirtualRegister;
            indPrintln(op + "\t" + inst.getRs().getIdentifier() + ", " + ((VirtualRegister) inst.getLoc()).getIdentifier() + ", " + inst.getRt().getIdentifier());
        } else {
            String src;
            if (inst.getLoc() instanceof Register)
                src = "0(" + ((Register) inst.getLoc()).getIdentifier() + ")";
            else
                src = inst.getLoc().toString();
            indPrintln(op + "\t" + inst.getRs().getIdentifier() + ", " + src);
        }
    }

    private String BB2Str(AsmBasicBlock BB) {
        if (BB2Name.get(BB) == null)
            BB2Name.put(BB, "." + (BB.getIdentifier() != null ? BB.getIdentifier() : "b") + "_" + nameCnt++);
        return BB2Name.get(BB);
    }
    private void println(String string) {
        output.print(string + "\n");
    }

    private void indPrintln(String string) {
        println("\t" + string);
    }

    private void setSP() {
        for (var func: asm.getAsmFunctionList())
            if (!func.isBuiltin()) {
                int stackSize = func.getNormalizedStackSize();
                if (stackSize > 0) {
                    func.getEntryBB().getFirstInst().prependInst(new AsmRegImm(asm.getPhyReg("sp"), new Imm(-stackSize), asm.getPhyReg("sp"), AsmRegImm.Op.ADDI));
                    func.getExitBB().getLastInst().prependInst(new AsmRegImm(asm.getPhyReg("sp"), new Imm(stackSize), asm.getPhyReg("sp"), AsmRegImm.Op.ADDI));
                }
            }
    }

    private void mainReplacing() {
        for (var func: asm.getAsmFunctionList())
            if (func.getIdentifier().equals("main"))
                func.setIdentifier("_main");
        for (var func: asm.getAsmFunctionList())
            if (func.getIdentifier().equals("__global_init"))
                func.setIdentifier("main");
    }

    private void builtinFunctionRenaming() {
        for (var func: asm.getAsmFunctionList())
            if (!func.isBuiltin())
                for (var BB: func.getBBList())
                    for (var inst = BB.getFirstInst(); inst != null; inst = inst.getSucInst())
                        if (inst instanceof AsmCall) {
                            var newIdentifier = ((AsmCall) inst).getAsmFunction().getIdentifier().replace(".", "_");
                            if (((AsmCall) inst).getAsmFunction().isBuiltin())
                                ((AsmCall) inst).getAsmFunction().setIdentifier(newIdentifier);
                        }
    }
}

package Compiler.IR;

import Compiler.IR.Inst.*;
import Compiler.IR.Operand.Imm;
import Compiler.IR.Operand.Operand;
import Compiler.IR.Operand.Register;
import Compiler.IR.Operand.StaticStr;
import Compiler.Utils.IRError;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class IRPrinter implements IRVisitor {
    PrintStream output;
    int cnt = 0;

    Set<BasicBlock> visitedBB = new HashSet<>();
    Map<Operand, String> operandNameMap = new HashMap<>();
    Map<BasicBlock, String> basicBlockNameMap = new HashMap<>();

    public void print(IR ir, PrintStream printStream) {
        output = printStream;

        for (StaticStr staticStr: ir.getStaticStrs())
            visit(staticStr);

        for (Function function: ir.getFunctions())
            visit(function);
    }

    public void visit(StaticStr staticStr) {
        output.println(operand2Str(staticStr) + " = " + staticStr.getValue());
    }

    public void visit(Function function) {
        // builtin function
        if (function.getEntryBB() == null) return;
        output.print("func " + function.getIdentifier() + " ");
        if (function.getReferenceToThisClass() != null)
            output.print(operand2Str(function.getReferenceToThisClass()) + " ");
        for (Register arg: function.getArgs())
            output.print(operand2Str(arg) + " ");
        output.println("{");
        visit(function.getEntryBB());
        output.println("}");
        output.println();
    }

    public void visit(BasicBlock basicBlock) {
        if (visitedBB.contains(basicBlock)) return;

        output.println(basicBlock2Str(basicBlock) + ":");

        for (IRInst irInst = basicBlock.getFirstInst(); irInst != null; irInst = irInst.getSucInst()) {
            output.print("\t");
            irInst.accept(this);
        }

        visitedBB.add(basicBlock);

        // visit next block
        IRInst lastInst = basicBlock.getLastInst();
        if (lastInst == null) return;
        if (lastInst instanceof Jump)
            visit(((Jump) lastInst).getBB());
        else if (lastInst instanceof Branch) {
            visit(((Branch) lastInst).getThenBB());
            visit(((Branch) lastInst).getElseBB());
        } else if (!(lastInst instanceof Return))
            throw new IRError("No jump or branch at the end of basic block.");
    }

    @Override
    public void visit(BinaryOp inst) {
        output.println(operand2Str(inst.getDest()) + " = " + inst.getOp().toString().toLowerCase() + " " + operand2Str(inst.getLhs()) + " " + operand2Str(inst.getRhs()));
    }

    @Override
    public void visit(Branch inst) {
        output.println("br " + operand2Str(inst.getCond()) + " " + basicBlock2Str(inst.getThenBB()) + " " + basicBlock2Str(inst.getElseBB()));
    }

    @Override
    public void visit(FuncCall inst) {
        if (inst.getDest() != null)
            output.print(operand2Str(inst.getDest()) + " = ");
        output.print("call " + inst.getFunction().getIdentifier());
        if (inst.getObj() != null)
            output.print(" " + operand2Str(inst.getObj()));
        for (Operand operand : inst.getParalist())
            output.print(" " + operand2Str(operand));
        output.println();
    }

    @Override
    public void visit(Alloc inst) {
        output.println(operand2Str(inst.getPtr()) + " = alloc " + operand2Str(inst.getSize()));
    }

    @Override
    public void visit(Jump inst) {
        output.println("jump " + basicBlock2Str(inst.getBB()));
    }

    @Override
    public void visit(Load inst) {
        output.println(operand2Str(inst.getDest()) + " = load " + operand2Str(inst.getPtr()));
    }

    @Override
    public void visit(Move inst) {
        output.println(operand2Str(inst.getDest()) + " = move " + operand2Str(inst.getSrc()));
    }

    @Override
    public void visit(Return inst) {
        if (inst.getRet() == null)
            output.println("ret");
        else
            output.println("ret " + operand2Str(inst.getRet()));
    }

    @Override
    public void visit(Store inst) {
        output.println("store " + operand2Str(inst.getSrc()) + " " + operand2Str(inst.getPtr()));
    }

    @Override
    public void visit(UnaryOp inst) {
        output.println(operand2Str(inst.getDest()) + " = " + inst.getOp().toString().toLowerCase() + " " + operand2Str(inst.getActed()));
    }

    public String operand2Str(Operand operand) {
        if (operand instanceof Imm) return operand.getIdentifier();

        if (operandNameMap.get(operand) == null)
            operandNameMap.put(operand, (operand.getIdentifier() == null ? "t" : operand.getIdentifier()) + "_" + cnt++);

        if (operand instanceof Register) {
            if (((Register) operand).isGlobal())
                return "@" + operandNameMap.get(operand);
            else
                return "%" + operandNameMap.get(operand);
        } else { // StaticStr
            return "@" + operandNameMap.get(operand);
        }
    }

    public String basicBlock2Str(BasicBlock basicBlock) {
        if (basicBlockNameMap.get(basicBlock) == null)
            basicBlockNameMap.put(basicBlock, (basicBlock.getName() == null ? "b" : basicBlock.getName()) + "_" + cnt++);
        return basicBlockNameMap.get(basicBlock);
    }
}

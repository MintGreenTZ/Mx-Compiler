package Compiler.IR.Inst;

import Compiler.IR.BasicBlock;
import Compiler.IR.IRVisitor;
import Compiler.IR.Operand.Operand;

public class Branch extends IRInst {
    private Operand cond;
    private BasicBlock thenBB, elseBB;

    public Branch(Operand cond, BasicBlock thenBB, BasicBlock elseBB) {
        this.cond = cond;
        this.thenBB = thenBB;
        this.elseBB = elseBB;
    }

    public Operand getCond() {
        return cond;
    }
    public BasicBlock getThenBB() {
        return thenBB;
    }
    public BasicBlock getElseBB() {
        return elseBB;
    }

    public void visit(IRVisitor visitor) {
        visitor.visit(this);
    }
}

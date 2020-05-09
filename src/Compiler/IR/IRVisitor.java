package Compiler.IR;

import Compiler.IR.Inst.*;

public interface IRVisitor {
    void visit(BinaryOp inst);
    void visit(Branch inst);
    void visit(FuncCall inst);
    void visit(Alloc inst);
    void visit(Jump inst);
    void visit(Load inst);
    void visit(Move inst);
    void visit(Return inst);
    void visit(Store inst);
    void visit(UnaryOp inst);
}

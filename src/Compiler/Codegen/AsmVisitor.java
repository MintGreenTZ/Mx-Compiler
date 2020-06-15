package Compiler.Codegen;

import Compiler.Codegen.Inst.*;

public interface AsmVisitor {

    void visit(AsmBranch inst);
    void visit(AsmCall inst);
    void visit(AsmJump inst);
    void visit(AsmLA inst);
    void visit(AsmLI inst);
    void visit(AsmLoad inst);
    void visit(AsmMove inst);
    void visit(AsmRegImm inst);
    void visit(AsmRegReg inst);
    void visit(AsmRet inst);
    void visit(AsmStore inst);
}

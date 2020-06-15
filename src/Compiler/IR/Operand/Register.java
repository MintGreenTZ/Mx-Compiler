package Compiler.IR.Operand;

import Compiler.Codegen.Inst.AsmMove;
import Compiler.IR.StackLocation;
import Compiler.IR.StackPointer;

import java.util.HashSet;
import java.util.Set;

public class Register extends Operand implements StackLocation {

    public Register() {}

    public Register(String identifier) {
        super(identifier);
    }

    // register allocation
    public String color;
    public int degree;
    public Register alias;
    public StackPointer spillAddr;
    public Set<Register> adjList = new HashSet<>();
    public Set<AsmMove> moveList = new HashSet<>();

    public void clean() {
        alias = null;
        spillAddr = null;
        adjList.clear();
        moveList.clear();
    }
}

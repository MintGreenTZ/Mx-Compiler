package Compiler.Codegen;

import Compiler.Codegen.Operand.PhysicalRegister;
import Compiler.IR.Operand.StaticStr;
import Compiler.IR.Operand.VirtualRegister;
import Compiler.Utils.CodegenError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Assembly {

    public String[] phyRegName = {
          "zero", "ra", "sp",  "gp",  "tp", "t0", "t1", "t2",
            "s0", "s1", "a0",  "a1",  "a2", "a3", "a4", "a5",
            "a6", "a7", "s2",  "s3",  "s4", "s5", "s6", "s7",
            "s8", "s9", "s10", "s11", "t3", "t4", "t5", "t6"
    };

    public String[] allocatableRegName = {
                  "ra",					  "t0", "t1", "t2",
            "s0", "s1", "a0", "a1", "a2", "a3", "a4", "a5",
            "a6", "a7", "s2", "s3", "s4", "s5", "s6", "s7",
            "s8", "s9", "s10", "s11", "t3", "t4", "t5", "t6"
    };

    public String[] calleeRegisterName = {
            "s0", "s1",
                        "s2", "s3", "s4", "s5", "s6", "s7",
            "s8", "s9", "s10", "s11"
    };

    public String[] callerRegisterName = {
                  "ra", 				  "t0", "t1", "t2",
                        "a0", "a1", "a2", "a3", "a4", "a5",
            "a6", "a7",
                                    "t3", "t4", "t5", "t6"
    };

    // global variables
    private List<VirtualRegister> globalVariableList = new ArrayList<>();
    private List<StaticStr> staticStrList = new ArrayList<>();

    private List<AsmFunction> asmFunctionList = new ArrayList<>();

    private HashMap<String, PhysicalRegister> phyRegs = new HashMap<>();

    public AsmFunction asmMalloc = new AsmFunction("malloc", true);

    public Assembly() {
        for (String name: phyRegName)
            phyRegs.put(name, new PhysicalRegister(name));
    }

    public PhysicalRegister getPhyReg(String name) {
        PhysicalRegister ret = phyRegs.get(name);
        if (ret == null)
            throw new CodegenError("No such a physical register called \"" + name + "\"");
        return ret;
    }

    public void addFunction(AsmFunction asmFunction) {
        asmFunctionList.add(asmFunction);
    }

    public List<VirtualRegister> getGlobalVariableList() {
        return globalVariableList;
    }
    public List<StaticStr> getStaticStrList() {
        return staticStrList;
    }
    public List<AsmFunction> getAsmFunctionList() {
        return asmFunctionList;
    }

    public void setGlobalVariableList(List<VirtualRegister> globalVariableList) {
        this.globalVariableList = globalVariableList;
    }
    public void setStaticStrList(List<StaticStr> staticStrList) {
        this.staticStrList = staticStrList;
    }
}

package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.Utils.Location;

import java.util.List;

public class ProgramNode extends Node {
    private List<DeclNode> decl;

    public ProgramNode(Location location, List<DeclNode> decl) {
        super(location);
        this.decl = decl;
    }

    public List<DeclNode> getDecl() {
        return decl;
    }

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}

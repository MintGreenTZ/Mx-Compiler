package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.Utils.Location;

import java.util.List;

public class BlockNode extends StmtNode {
    private List<StmtNode> stmtList;

    public BlockNode(Location location, List<StmtNode> stmtList) {
        super(location);
        this.stmtList = stmtList;
    }

    public List<StmtNode> getStmtList() {
        return stmtList;
    }

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}

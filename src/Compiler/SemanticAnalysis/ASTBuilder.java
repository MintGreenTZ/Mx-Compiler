package Compiler.SemanticAnalysis;

import Compiler.AST.*;
import Compiler.Parser.MxstarBaseVisitor;
import Compiler.Parser.MxstarParser;
import Compiler.Utils.Location;

import java.util.ArrayList;
import java.util.List;

/* -------------------------------------- Interface with MxstarParser ---------------------------------------------
Every non-terminal symbol <Symbol Name> in g4 will form a class called "<Symbol Name>Context" in MxstarParser.java
Interfaces with every parsed symbol <Symbol> are as follows:

    non-terminal symbol grammar

        nary grammar (without *)

            public <Symbol>Context <Symbol>() { return getToken(MxstarParser.<Symbol>, 0); }

        ary grammar (with *)

            public List<<Symbol>Context> <Symbol>() { return getRuleContexts(<Symbol>Context.class); }
		    public <Symbol>Context <Symbol>(int i) { return getRuleContext(<Symbol>Context.class,i); }

    terminal symbol grammar

        nary grammar (without *)

            public TerminalNode <Symbol>() { return getToken(MxstarParser.<Symbol>, 0); }

        ary grammar (with *)

            public List<TerminalNode> <Symbol>() { return getTokens(MxstarParser.<Symbol>); }
            public TerminalNode <Symbol>(int i) { return getToken(MxstarParser.<Symbol>, i); }

    about <Symbol>Context
        (in AbstractParseTreeVisitor<T>)
             public T visit(ParseTree tree) { return tree.accept(this); }
---------------------------------------------------------------------------------------------------------------- */

public class ASTBuilder extends MxstarBaseVisitor<Node> {

    @Override public Node visitProgram(MxstarParser.ProgramContext ctx) {
        List<DeclNode> declNodes = new ArrayList<>();
        for (var declNode: ctx.decl())
            declNodes.add((DeclNode) visit(declNode));
        return new ProgramNode(new Location(ctx.getStart()), declNodes);
    }

    @Override public Node visitClassDecl(MxstarParser.ClassDeclContext ctx) {
        String identifier = ctx.Identifier().getText();
        FunctionDeclNode constructor = null;
        List<VariableDeclNode> variableDeclList = new ArrayList<>();
        List<FunctionDeclNode> functionDeclList = new ArrayList<>();
        for (var constructorDecl : ctx.constructorDef())
            constructor = (FunctionDeclNode) visit(constructorDecl);
        for (var variableDecl : ctx.variableDecl())
            variableDeclList.add((VariableDeclNode) visit(variableDecl));
        for (var functionDecl : ctx.functionDecl())
            functionDeclList.add((FunctionDeclNode) visit(functionDecl));
        return new ClassDeclNode(new Location(ctx.getStart()), identifier, constructor, variableDeclList, functionDeclList);
    }

    @Override public Node visitVariableDecl(MxstarParser.VariableDeclContext ctx) {
        TypeNode type = (TypeNode) visit(ctx.type());
        List<IdNode> variableNameList = new ArrayList<>();
        for (var variableName: ctx.Identifier())
            variableNameList.add((IdNode) visit(variableName));
        return new VariableDeclNode(new Location(ctx.getStart()), type, variableNameList);
    }

    @Override public Node visitType(MxstarParser.TypeContext ctx) {
        if (ctx.nonArrayType() != null)
            return visit(ctx.nonArrayType());
        else
            return new ArrayTypeNode(new Location(ctx.getStart()), (TypeNode) visit(ctx.type()));
    }

    @Override public Node visitNonArrayType(MxstarParser.NonArrayTypeContext ctx) {
        if (ctx.Bool() != null)
            return new NonArrayTypeNode(new Location(ctx.getStart()), "Bool");
        else if (ctx.Int() != null)
            return new NonArrayTypeNode(new Location(ctx.getStart()), "Int");
        else if (ctx.String() != null)
            return new NonArrayTypeNode(new Location(ctx.getStart()), "String");
        else
            return new NonArrayTypeNode(new Location(ctx.getStart()), ctx.Identifier().getText());
    }

    @Override public Node visitFunctionDecl(MxstarParser.FunctionDeclContext ctx) {
        TypeNode type = (TypeNode) visit(ctx.type());
        String identifier = ctx.Identifier().getText();
        ParameterListNode parameterList = (ParameterListNode) visit(ctx.parameterList());
        BlockNode funcBody = (BlockNode) visit(ctx.block());
        return new FunctionDeclNode(new Location(ctx.getStart()), type, identifier, parameterList, funcBody);
    }

    @Override public Node visitParameterList(MxstarParser.ParameterListContext ctx) {
        List<ParameterNode> parameterList = new ArrayList<>();
        for (var parameter : ctx.parameter())
            parameterList.add((ParameterNode) visit(parameter));
        return new ParameterListNode(new Location(ctx.getStart()), parameterList);
    }

    @Override public Node visitParameter(MxstarParser.ParameterContext ctx) {
        TypeNode type = (TypeNode) visit(ctx.type());
        String identifier = ctx.Identifier().getText();
        return new ParameterNode(new Location(ctx.getStart()), type, identifier);
    }

    @Override public Node visitConstructorDef(MxstarParser.ConstructorDefContext ctx) {
        String identifier = ctx.Identifier().getText();
        BlockNode block = (BlockNode) visit(ctx.block());
        return new ConstructorNode(new Location(ctx.getStart()), identifier, block);
    }

    @Override public Node visitBlock(MxstarParser.BlockContext ctx) {
        List<StmtNode> stmtList = new ArrayList<>();
        for (var stmt: ctx.statement())
            stmtList.add((StmtNode) visit(stmt));
        return new BlockNode(new Location(ctx.getStart()), stmtList);
    }

    @Override public Node visitBlockStmt(MxstarParser.BlockStmtContext ctx) {
        return visit(ctx.block());
    }

    @Override public Node visitVariableDeclStmt(MxstarParser.VariableDeclStmtContext ctx) {
        return visit(ctx.variableDecl());
    }

    @Override public Node visitIfStmt(MxstarParser.IfStmtContext ctx) {
        ExprNode cond = (ExprNode) visit(ctx.expression());
        StmtNode thenStmt = (StmtNode) visit(ctx.statement(0));
        StmtNode elseStmt = (StmtNode) visit(ctx.statement(1));
        return new IfNode(new Location(ctx.getStart()), cond, thenStmt, elseStmt);
    }

    @Override public Node visitWhileStmt(MxstarParser.WhileStmtContext ctx) {
        ExprNode cond = (ExprNode) visit(ctx.expression());
        StmtNode body = (StmtNode) visit(ctx.statement());
        return new WhileNode(new Location(ctx.getStart()), cond, body);
    }

    @Override public Node visitForStmt(MxstarParser.ForStmtContext ctx) {
        ExprNode init = (ExprNode) visit(ctx.expression(0));
        ExprNode cond = (ExprNode) visit(ctx.expression(1));
        ExprNode step = (ExprNode) visit(ctx.expression(2));
        StmtNode body = (StmtNode) visit(ctx.statement());
        return new ForNode(new Location(ctx.getStart()), init, cond, step, body);
    }

    @Override public Node visitRetStmt(MxstarParser.RetStmtContext ctx) {
        ExprNode retValue = (ExprNode) visit(ctx.expression());
        return new ReturnNode(new Location(ctx.getStart()), retValue);
    }

    @Override public Node visitBreakStmt(MxstarParser.BreakStmtContext ctx) {
        return new BreakNode(new Location(ctx.getStart()));
    }

    @Override public Node visitContinueStmt(MxstarParser.ContinueStmtContext ctx) {
        return new ContinueNode(new Location(ctx.getStart()));
    }

    @Override public Node visitExprStmt(MxstarParser.ExprStmtContext ctx) {
        return new ExprStmtNode(new Location(ctx.getStart()), (ExprNode) visit(ctx.expression()));
    }

    @Override public Node visitEmptyStmt(MxstarParser.EmptyStmtContext ctx) {
        return null;
    }

    @Override public Node visitMemberExpr(MxstarParser.MemberExprContext ctx) {
        ExprNode obj = (ExprNode) visit(ctx.expression());
        String member = ctx.Identifier().toString();
        return new MemberAccessNode(new Location(ctx.getStart()), obj, member);
    }

    @Override public Node visitAssignOpExpr(MxstarParser.AssignOpExprContext ctx) {
        ExprNode lhs = (ExprNode) visit(ctx.expression(0));
        ExprNode rhs = (ExprNode) visit(ctx.expression(1));
        return new AssignNode(new Location(ctx.getStart()), lhs, rhs);
    }

    @Override public Node visitFuncCallExpr(MxstarParser.FuncCallExprContext ctx) {
        ExprNode funcObj = (ExprNode) visit(ctx.expression());
        ParameterListNode parameterList = (ParameterListNode) visit(ctx.expressionList());
        return new FuncCallNode(new Location(ctx.getStart()), funcObj, parameterList);
    }

    @Override public Node visitNewExpr(MxstarParser.NewExprContext ctx) {
        return visit(ctx.creator());
    }

    @Override public Node visitPrefixExpr(MxstarParser.PrefixExprContext ctx) {
        ExprNode expr = (ExprNode) visit(ctx.expression());
        PrefixExprNode.Op op;
        switch (ctx.op.getText()) {
            case "~":  op = PrefixExprNode.Op.INV; break;
            case "!":  op = PrefixExprNode.Op.LogicINV; break;
            case "++": op = PrefixExprNode.Op.SelfADD; break;
            case "--": op = PrefixExprNode.Op.SelfSUB; break;
            case "+":  op = PrefixExprNode.Op.ADD; break;
            case "-":  op = PrefixExprNode.Op.SUB; break;
            default: op = null;
        }
        return new PrefixExprNode(new Location(ctx.getStart()), op, expr);
    }

    @Override public Node visitSubExpr(MxstarParser.SubExprContext ctx) {
        return visit(ctx.expression());
    }

    @Override public Node visitIdExpr(MxstarParser.IdExprContext ctx) {
        return new IdNode(new Location(ctx.getStart()), ctx.Identifier().getText());
    }

    @Override public Node visitConstExpr(MxstarParser.ConstExprContext ctx) {
        return visit(ctx.constant());
    }

    @Override public Node visitSuffixExpr(MxstarParser.SuffixExprContext ctx) {
        ExprNode expr = (ExprNode) visit(ctx.expression());
        PrefixExprNode.Op op;
        switch (ctx.op.getText()) {
            case "++": op = PrefixExprNode.Op.SelfADD; break;
            case "--": op = PrefixExprNode.Op.SelfSUB; break;
            default: op = null;
        }
        return new PrefixExprNode(new Location(ctx.getStart()), op, expr);
    }

    @Override public Node visitThisExpr(MxstarParser.ThisExprContext ctx) {
        return new ThisNode(new Location(ctx.getStart()));
    }

    @Override public Node visitBinaryOpExpr(MxstarParser.BinaryOpExprContext ctx) {
        ExprNode lhs = (ExprNode) visit(ctx.lhs);
        ExprNode rhs = (ExprNode) visit(ctx.rhs);
        BinaryOpNode.Op op;
        switch (ctx.op.getText()) {
            case "*":  op = BinaryOpNode.Op.MUL; break;
            case "/":  op = BinaryOpNode.Op.DIV; break;
            case "%":  op = BinaryOpNode.Op.MOD; break;
            case "+":  op = BinaryOpNode.Op.ADD; break;
            case "-":  op = BinaryOpNode.Op.SUB; break;
            case "<<": op = BinaryOpNode.Op.SHL; break;
            case ">>": op = BinaryOpNode.Op.SHR; break;
            case "<":  op = BinaryOpNode.Op.LT; break;
            case "<=": op = BinaryOpNode.Op.LE; break;
            case ">":  op = BinaryOpNode.Op.GT; break;
            case ">=": op = BinaryOpNode.Op.GE; break;
            case "==": op = BinaryOpNode.Op.EQ; break;
            case "!=": op = BinaryOpNode.Op.NEQ; break;
            case "&":  op = BinaryOpNode.Op.AND; break;
            case "^":  op = BinaryOpNode.Op.XOR; break;
            case "|":  op = BinaryOpNode.Op.OR; break;
            case "&&": op = BinaryOpNode.Op.LAND; break;
            case "||": op = BinaryOpNode.Op.LOR; break;
            case "=":  op = BinaryOpNode.Op.ASS; break;
            default:op = null;
        }
        return new BinaryOpNode(new Location(ctx.getStart()), lhs, op, rhs);
    }

    @Override public Node visitSubscriptExpr(MxstarParser.SubscriptExprContext ctx) {
        ExprNode body = (ExprNode) visit(ctx.expression(0));
        ExprNode subscript = (ExprNode) visit(ctx.expression(1));
        return new SubscriptNode(new Location(ctx.getStart()), body, subscript);
    }

    @Override public Node visitExpressionList(MxstarParser.ExpressionListContext ctx) {
        List<ExprNode> exprNodes = new ArrayList<>();
        for (var expression: ctx.expression())
            exprNodes.add((ExprNode) visit(expression));
        return new ExprListNode(new Location(ctx.getStart()), exprNodes);
    }

    @Override public Node visitConstInteger(MxstarParser.ConstIntegerContext ctx) {
        return new ConstIntNode(new Location(ctx.getStart()), Integer.parseInt(ctx.getText()));
    }

    @Override public Node visitConstString(MxstarParser.ConstStringContext ctx) {
        return new ConstStringNode(new Location(ctx.getStart()), ctx.getText());
    }

    @Override public Node visitConstBool(MxstarParser.ConstBoolContext ctx) {
        return new ConstBoolNode(new Location(ctx.getStart()), Boolean.parseBoolean(ctx.getText()));
    }

    @Override public Node visitConstNull(MxstarParser.ConstNullContext ctx) {
        return new VoidNode(new Location(ctx.getStart()));
    }

    @Override public Node visitNaryCreator(MxstarParser.NaryCreatorContext ctx) {
        return new NewNode(new Location(ctx.getStart()), (TypeNode)visit(ctx.nonArrayType()), 0, null);
    }

    @Override public Node visitArrayCreator(MxstarParser.ArrayCreatorContext ctx) {
        int dim = (ctx.getChildCount() - ctx.expression().size() - 1) >> 1;
        List<ExprNode> exprList = new ArrayList<>();
        for (var expr :ctx.expression())
            exprList.add((ExprNode) visit(expr));
        return new NewNode(new Location(ctx.getStart()), (TypeNode)visit(ctx.nonArrayType()), dim, exprList);
    }

    @Override public Node visitClassCreator(MxstarParser.ClassCreatorContext ctx) {
        return new NewNode(new Location(ctx.getStart()), (TypeNode)visit(ctx.nonArrayType()), 0, null);
    }
}

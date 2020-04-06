package Compiler.SemanticAnalysis;

import Compiler.AST.*;
import Compiler.SymbolTable.ClassSymbol;
import Compiler.SymbolTable.FunctionSymbol;
import Compiler.SymbolTable.Scope.GlobalScope;
import Compiler.SymbolTable.Scope.LocalScope;
import Compiler.SymbolTable.Scope.Scope;
import Compiler.SymbolTable.Symbol;
import Compiler.SymbolTable.Type.NullType;
import Compiler.SymbolTable.Type.Type;
import Compiler.SymbolTable.VariableSymbol;
import Compiler.Utils.SemanticError;

import static Compiler.SemanticAnalysis.GlobalVisitor.nullType;

/* This Class is used to deal with everything in class scope
 * · add definition of func, var of class to class Scope
 * · check constructor
 */

public class ClassMemberVisitor extends ASTBaseVisitor {
    private GlobalScope globalScope;
    private Scope scope;

    public ClassMemberVisitor(GlobalScope globalScope) {
        this.globalScope = globalScope;
        this.scope = globalScope;
    }

    @Override
    public void visit(ProgramNode node) {
        for (var decl: node.getDecl())
            if (decl instanceof ClassDeclNode)
                decl.accept(this);
    }

    @Override
    public void visit(ClassDeclNode node) {
        ClassSymbol classSymbol = (ClassSymbol) scope.resolveSymbol(node.getIdentifier(), node.getLocation());
        scope = classSymbol;
        for (var funcDecl: node.getFunctionDecl())
            funcDecl.accept(this);
        constructorCheckerAndSetter(node, classSymbol);
        for (var varDecl: node.getVariableDecl())
            varDecl.accept(this);
        scope = scope.getUpperScope();
    }

    @Override
    public void visit(FunctionDeclNode node) {
        FunctionSymbol functionSymbol = new FunctionSymbol(node.getIdentifier(), typeResolver(node.getType(), globalScope), node, scope);
        for (var varDecl: node.getParameterList()) {
            String name = varDecl.getVariable().getIdentifier();
            Type type = typeResolver(varDecl.getType(), globalScope);
            functionSymbol.defineVariable(new VariableSymbol(name, type, varDecl.getVariable()));
        }
        scope.defineFunction(functionSymbol);
        node.setFunctionSymbol(functionSymbol);
    }

    @Override
    public void visit(VariableDeclNode node) {
        for (var variable: node.getVariables())
            scope.defineVariable(new VariableSymbol(variable.getIdentifier(), typeResolver(node.getType(), globalScope), variable));
    }


    private void constructorCheckerAndSetter(ClassDeclNode node, ClassSymbol classSymbol) {
        Symbol constructor = scope.resolveSymbol(classSymbol.getTypeName(), node.getLocation());
        if (constructor instanceof ClassSymbol) {
            //classSymbol.setConstructor(new FunctionSymbol(classSymbol.getTypeName(), classSymbol, null, classSymbol));
            return;
        }
        if (constructor.getType() != nullType)
            throw new SemanticError("Return value of constructor is forbidden.", node.getLocation());
        if (!((FunctionDeclNode) constructor.getDefinition()).getParameterList().isEmpty())
            throw new SemanticError("Parameter of constructor is forbidden", node.getLocation());
        classSymbol.setConstructor((FunctionSymbol) constructor);
    }
}

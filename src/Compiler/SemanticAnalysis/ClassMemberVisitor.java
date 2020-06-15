package Compiler.SemanticAnalysis;

import Compiler.AST.*;
import Compiler.SymbolTable.ClassSymbol;
import Compiler.SymbolTable.FunctionSymbol;
import Compiler.SymbolTable.Scope.GlobalScope;
import Compiler.SymbolTable.Scope.Scope;
import Compiler.SymbolTable.Type.Type;
import Compiler.SymbolTable.VariableSymbol;
import Compiler.Utils.SemanticError;

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
        constructorCheckerAndSetter(node, classSymbol);
        for (var varDecl: node.getVariableDecl())
            varDecl.accept(this);
        for (var funcDecl: node.getFunctionDecl())
            funcDecl.accept(this);
        scope = scope.getUpperScope();
    }

    @Override
    public void visit(FunctionDeclNode node) {
        if (node.getIdentifier().equals(((ClassSymbol) scope).getName()))
            throw new SemanticError("Constructor shouldn't have return value.", node.getLocation());
        FunctionSymbol functionSymbol = new FunctionSymbol(node.getIdentifier(), typeResolver(node.getType(), globalScope), node, scope);
        for (var parameter: node.getParameterList()) {
            VariableNode variableNode = parameter.getVariable();
            String name = variableNode.getIdentifier();
            Type type = typeResolver(parameter.getType(), globalScope);
            VariableSymbol variableSymbol = new VariableSymbol(name, type, variableNode);
            functionSymbol.defineVariable(variableSymbol);
            variableNode.setVariableSymbol(variableSymbol);
        }
        scope.defineFunction(functionSymbol);
        node.setFunctionSymbol(functionSymbol);
    }

    @Override
    public void visit(VariableDeclNode node) {
        for (var variable: node.getVariables()) {
            VariableSymbol variableSymbol = new VariableSymbol(variable.getIdentifier(), typeResolver(node.getType(), globalScope), variable);
            scope.defineVariable(variableSymbol);
            // for IR
            variable.setVariableSymbol(variableSymbol);
        }
    }


    private void constructorCheckerAndSetter(ClassDeclNode node, ClassSymbol classSymbol) {
        /*Symbol constructor = scope.resolveSymbol(classSymbol.getTypeName(), node.getLocation());
        if (constructor instanceof ClassSymbol) {
            //classSymbol.setConstructor(new FunctionSymbol(classSymbol.getTypeName(), classSymbol, null, classSymbol));
            return;
        }
        if (constructor.getType() != nullType)
            throw new SemanticError("Return value of constructor is forbidden.", node.getLocation());
        if (!((FunctionDeclNode) constructor.getDefinition()).getParameterList().isEmpty())
            throw new SemanticError("Parameter of constructor is forbidden", node.getLocation());*/
        if (node.getConstructorDecl() != null) {
            String identifier = node.getConstructorDecl().getIdentifier();
            if (!identifier.equals(classSymbol.getTypeName()))
                throw new SemanticError("Name of constructor is not match the class.", node.getLocation());
            FunctionSymbol constructor = new FunctionSymbol(identifier, null, node.getConstructorDecl(), classSymbol);
            classSymbol.setConstructor(constructor);
            scope.defineFunction(constructor);
            node.getConstructorDecl().setFunctionSymbol(constructor);
        }
    }
}

package Compiler.SemanticAnalysis;

import Compiler.AST.*;
import Compiler.SymbolTable.ClassSymbol;
import Compiler.SymbolTable.FunctionSymbol;
import Compiler.SymbolTable.PrimitiveSymbol;
import Compiler.SymbolTable.Scope.GlobalScope;
import Compiler.SymbolTable.Type.NullType;
import Compiler.SymbolTable.Type.Type;
import Compiler.SymbolTable.VariableSymbol;
import Compiler.Utils.Location;
import Compiler.Utils.SemanticError;

/* This Class is used to deal with everything in global scope
 * · add definition of global class, func, var to globalScope
 * · initialize built-in Type
 * · check main Function
 */

public class GlobalVisitor extends ASTBaseVisitor {
    private static GlobalScope globalScope;

    public GlobalVisitor(GlobalScope globalScope) {
        this.globalScope = globalScope;
        initializer();
    }

    @Override
    public void visit(ProgramNode node) {
        for (DeclNode decl: node.getDecl())
            if (decl instanceof ClassDeclNode)
                decl.accept(this);
        for (DeclNode decl: node.getDecl())
            if (decl instanceof FunctionDeclNode)
                decl.accept(this);
        mainFunctionChecker();
        for (DeclNode decl: node.getDecl())
            if (decl instanceof VariableDeclNode)
                decl.accept(this);
    }

    @Override
    public void visit(ClassDeclNode node) {
        ClassSymbol classSymbol = new ClassSymbol(node.getIdentifier(), node, globalScope);
        globalScope.defineClass(classSymbol);
        node.setClassSymbol(classSymbol);
    }

    @Override
    public void visit(FunctionDeclNode node) {
        FunctionSymbol functionSymbol = new FunctionSymbol(node.getIdentifier(), typeResolver(node.getType(), globalScope), node, globalScope);
        for (var varDecl: node.getParameterList()) {
            String name = varDecl.getVariable().getIdentifier();
            Type type = typeResolver(varDecl.getType(), globalScope);
            functionSymbol.defineVariable(new VariableSymbol(name, type, varDecl.getVariable()));
        }
        globalScope.defineFunction(functionSymbol);
        node.setFunctionSymbol(functionSymbol);
    }

    @Override
    public void visit(VariableDeclNode node) {
        for (var varDecl: node.getVariables()) {
            String name = varDecl.getIdentifier();
            Type type = typeResolver(node.getType(), globalScope);
            globalScope.defineVariable(new VariableSymbol(name, type, varDecl));
        }
    }

    public static PrimitiveSymbol intType = new PrimitiveSymbol("int");
    public static PrimitiveSymbol boolType = new PrimitiveSymbol("bool");
    public static PrimitiveSymbol voidType = new PrimitiveSymbol("void");
    public static NullType nullType = new NullType();
    public static ClassSymbol StringType = new ClassSymbol("string", null, globalScope);
    public static FunctionSymbol getArraySizeSymbol = new FunctionSymbol("size", intType, null, globalScope);

    private void initializer() {
        globalScope.defineType(intType);
        globalScope.defineType(boolType);
        globalScope.defineType(voidType);
        globalScope.defineNull(nullType);
        globalScope.defineClass(StringType);
        StringType.defineFunction(new FunctionSymbol("length", intType, null, globalScope));
        StringType.defineFunction(new FunctionSymbol("substring", StringType, null, globalScope) {{
            defineVariable(new VariableSymbol("left", intType, null));
            defineVariable(new VariableSymbol("right", intType, null));
        }});
        StringType.defineFunction(new FunctionSymbol("parseInt", intType, null, globalScope));
        StringType.defineFunction(new FunctionSymbol("ord", intType, null, globalScope) {{
            defineVariable(new VariableSymbol("pos", intType, null));
        }});
        globalScope.defineFunction(new FunctionSymbol("print", voidType, null, globalScope){{
            defineVariable(new VariableSymbol("str", StringType, null));
        }});
        globalScope.defineFunction(new FunctionSymbol("println", voidType, null, globalScope){{
            defineVariable(new VariableSymbol("str", StringType, null));
        }});
        globalScope.defineFunction(new FunctionSymbol("printInt", voidType, null, globalScope){{
            defineVariable(new VariableSymbol("n", intType, null));
        }});
        globalScope.defineFunction(new FunctionSymbol("printlnInt", voidType, null, globalScope){{
            defineVariable(new VariableSymbol("n", intType, null));
        }});
        globalScope.defineFunction(new FunctionSymbol("getString", StringType, null, globalScope));
        globalScope.defineFunction(new FunctionSymbol("getInt", intType, null, globalScope));
        globalScope.defineFunction(new FunctionSymbol("toString", StringType, null, globalScope){{
            defineVariable(new VariableSymbol("i", intType, null));
        }});
    }

    private void mainFunctionChecker() {
        FunctionSymbol main = (FunctionSymbol) globalScope.resolveSymbol("main", new Location(0, 0));
        if (main.getType() != intType)
            throw new SemanticError("Return value of main must be int.", new Location(0, 0));
        if (!((FunctionDeclNode) main.getDefinition()).getParameterList().isEmpty())
            throw new SemanticError("No parameter should be in main.", new Location(0, 0));
    }
}

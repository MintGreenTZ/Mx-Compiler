package Compiler;

import Compiler.AST.ProgramNode;
import Compiler.Parser.MxstarErrorListener;
import Compiler.Parser.MxstarLexer;
import Compiler.Parser.MxstarParser;
import Compiler.SemanticAnalysis.*;
import Compiler.SymbolTable.Scope.GlobalScope;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.FileInputStream;
import java.io.InputStream;

public class Main {
    public static void main(String[] args) {
        try{
            InputStream inputStream;
            CharStream input = null;

            inputStream = new FileInputStream("code.mx");
            input = CharStreams.fromStream(inputStream);
            MxstarLexer lexer = new MxstarLexer(input);
            CommonTokenStream token = new CommonTokenStream(lexer);
            MxstarParser parser = new MxstarParser(token);
            parser.removeErrorListeners();
            parser.addErrorListener(new MxstarErrorListener());
            ASTBuilder astBuilder = new ASTBuilder();
            ProgramNode astRoot = (ProgramNode) astBuilder.visit(parser.program());

            GlobalScope globalScope = new GlobalScope("globalScope", null);
            new GlobalVisitor(globalScope).visit(astRoot);
            new ClassMemberVisitor(globalScope).visit(astRoot);
            new FunctionBodyVisitor(globalScope).visit(astRoot);
            new SemanticChecker(globalScope).visit(astRoot);
        }
        catch (Exception error) {
            error.printStackTrace();
            System.err.println(error.getMessage());
            System.exit(1);
        }

    }
}

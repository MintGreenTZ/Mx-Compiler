package Compiler;

import Compiler.AST.ProgramNode;
import Compiler.Codegen.AsmPrinter;
import Compiler.Codegen.Assembly;
import Compiler.Codegen.InfiniteRegAsm;
import Compiler.Codegen.RegisterAllocation;
import Compiler.IR.IRBuilder;
import Compiler.IR.IRPrinter;
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
import java.io.PrintStream;

public class Main {
    public static void main(String[] args) {
        try{
            boolean semanticOnly = false;

            for(String arg : args){
                switch (arg){
                    case "-semantic": semanticOnly = true; break;
                    default: break;
                }
            }

            InputStream inputStream;
            CharStream input = null;

            inputStream = new FileInputStream("code.mx");
            input = CharStreams.fromStream(inputStream);
            MxstarLexer lexer = new MxstarLexer(input);
            CommonTokenStream token = new CommonTokenStream(lexer);
            MxstarParser parser = new MxstarParser(token);
            lexer.removeErrorListeners();
            lexer.addErrorListener(new MxstarErrorListener());
            parser.removeErrorListeners();
            parser.addErrorListener(new MxstarErrorListener());
            ASTBuilder astBuilder = new ASTBuilder();
            ProgramNode astRoot = (ProgramNode) astBuilder.visit(parser.program());

            // Semantic Analysis
            GlobalScope globalScope = new GlobalScope("globalScope", null);
            new GlobalVisitor(globalScope).visit(astRoot);
            new ClassMemberVisitor(globalScope).visit(astRoot);
            new FunctionBodyVisitor(globalScope).visit(astRoot);
            new SemanticChecker(globalScope).visit(astRoot);
            if (semanticOnly) return;

            // IR
            IRBuilder irBuilder = new IRBuilder(globalScope);
            irBuilder.visit(astRoot);
            new IRPrinter().print(irBuilder.getIr(), new PrintStream("ir.txt"));
            //IRInterpreter.main("ir.txt", System.out, new FileInputStream("test.in"), false);

            // codegen
            InfiniteRegAsm infiniteRegAsm = new InfiniteRegAsm(irBuilder.getIr());
            Assembly asm = infiniteRegAsm.getAsm();
            new RegisterAllocation(asm);
            new AsmPrinter(asm).run(new PrintStream("test.s"));
        }
        catch (Exception error) {
            error.printStackTrace();
            System.err.println(error.getMessage());
            System.exit(1);
        }

    }
}

package Compiler.Utils;

public class CodegenError extends RuntimeException {
    String msg;

    public CodegenError(String message) {
        super(message);
        this.msg = message;
    }

    @Override
    public String getMessage() {
        return "Semantic Error:" + msg;
    }
}

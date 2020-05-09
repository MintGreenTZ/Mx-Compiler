package Compiler.Utils;

public class IRError extends RuntimeException {
    String msg;

    public IRError(String message) {
        super(message);
        this.msg = message;
    }

    @Override
    public String getMessage() {
        return "Semantic Error:" + msg;
    }
}

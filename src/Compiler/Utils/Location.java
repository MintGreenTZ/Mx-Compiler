package Compiler.Utils;

import org.antlr.v4.runtime.Token;

public class Location {
    private int row, col;

    public Location(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public Location(Token token) {
        this.row = token.getLine();
        this.col = token.getCharPositionInLine();
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}

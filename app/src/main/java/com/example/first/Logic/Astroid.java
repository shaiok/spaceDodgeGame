package com.example.first.Logic;

public class Astroid {
    private int col ;
    private int row ;

    public Astroid(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public Astroid setCol(int col) {
        this.col = col;
        return this;
    }

    public Astroid setRow(int row) {
        this.row = row;
        return this;
    }
}

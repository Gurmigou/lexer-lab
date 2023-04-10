package code.util;

public class CodeLineContainer {
    private int row;
    private int column;
    private String codeLine;

    public CodeLineContainer(int row, int column, String codeLine) {
        this.row = row;
        this.column = column;
        this.codeLine = codeLine;
    }

    public boolean isEnded(int pos) {
        return codeLine.length() <= column + pos;
    }

    public char nextSymbol(int pos) {
        return codeLine.charAt(column + pos);
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public String getCodeLine() {
        return codeLine;
    }

    public void setCodeLine(String codeLine) {
        this.codeLine = codeLine;
    }
}

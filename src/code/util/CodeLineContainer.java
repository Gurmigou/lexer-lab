package code.util;

public class CodeLineContainer {
    private int row;
    private int column;
    private String codeLine;

    public CodeLineContainer() {
    }

    public boolean isEnded(int shift) {
        return column + shift >= codeLine.length();
    }

    public boolean isEnded() {
        return isEnded(0);
    }

    public char getShifted(int shift) {
        return codeLine.charAt(column + shift);
    }

    public char getShifted() {
        return getShifted(0);
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

    public void incrementRow() {
        this.row++;
    }

    public void incrementColumn() {
        this.column++;
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

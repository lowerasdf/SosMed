package application;
/**
 * Checked exception thrown if the syntax of a
 * command is wrong
 */
@SuppressWarnings("serial")
public class InvalidSyntaxException extends Exception {
    private int lineNum;
    
    public InvalidSyntaxException(int lineNum) {
        this.lineNum = lineNum;
    }
    
    public int getLineNum() {
        return this.lineNum;
    }
}
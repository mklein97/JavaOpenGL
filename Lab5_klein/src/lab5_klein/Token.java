package lab5_klein;

/**
 *
 * @author Matt
 */
public class Token {
    private String c;
    private int lineNum;
    private int colNum;
    private String line;
    
    public Token(String c, int lineNum, int colNum, String line){
        this.c = c;
        this.colNum = colNum;
        this.lineNum = lineNum;
        this.line = line;
    }
    
    public String getChar(){
        return c;
    }
    
    public int getColNum(){
        return colNum;
    }
    
    public int getLineNum(){
        return lineNum;
    }
    
    public String getLine(){
        return line;
    }
}

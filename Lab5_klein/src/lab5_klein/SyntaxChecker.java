package lab5_klein;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Stack;

/**
 *
 * @author Matt
 */
public class SyntaxChecker {

    public static boolean check(String s, boolean conOut) throws FileNotFoundException {
        if (conOut) {
            System.out.println("Processing '" + s + "'");
            System.out.println("----------------------");
        }

        File file = new File(s);
        Scanner input = new Scanner(file);
        boolean result = true;
        Stack<Token> stack = new Stack();
        int line = 1;
        boolean inString = false;
        boolean inComment = false;

        while (input.hasNext()) {
            String nextLine = input.nextLine();
            inComment = false;
            for (int i = 0; i < nextLine.length(); i++) {
                String c = Character.toString(nextLine.charAt(i));
                if (c.equals("\"")) {
                    if (!inString) {
                        Token t = new Token(c, line, i, nextLine);
                        stack.push(t);
                        inString = true;
                    } else {
                        if (!calcError(stack, "\"", s, nextLine, line, i)) {
                            result = false;
                        }
                        inString = false;
                    }
                }
                if (inString)
                    continue;

                String c2;
                if (nextLine.length() >= i+2)
                    c2 = nextLine.substring(i, i+2);
                else {
                    c2 = "";
                }
                if (c2.equals("//"))
                   inComment = true;
                
                if (inComment)
                    continue;
                
                if (c.equals("(") || c.equals("{") || c.equals("[")) {
                    Token t = new Token(c, line, i, nextLine);
                    stack.push(t);
                }
                if (c2.equals("/*")) {
                    Token t = new Token(c2, line, i, nextLine);
                    stack.push(t);
                }

                if (c.equals(")")) {
                    if (!calcError(stack, "(", s, nextLine, line, i)) {
                        result = false;
                    }
                }
                if (c.equals("}")) {
                    if (!calcError(stack, "{", s, nextLine, line, i)) {
                        result = false;
                    }
                }
                if (c.equals("]")) {
                    if (!calcError(stack, "[", s, nextLine, line, i)) {
                        result = false;
                    }
                }
                if (c2.equals("*/")) {
                    if (!calcError(stack, "/*", s, nextLine, line, i)) {
                        result = false;
                    }
                }
            }
            line++;
        }
        input.close();

        while (!stack.empty()) {
            Token t = stack.pop();
            System.out.println("[ERROR] Unmatched token in file '" + s + "' line#" + t.getLineNum() + ":");
            System.out.println(t.getLine());
            for (int j = 0; j < t.getColNum(); j++) {
                System.out.print(" ");
            }
            System.out.println("^");
            result = false;
        }

        if (result) {
            System.out.println("[SUCCESS]");
        }
        return result;
    }

    private static boolean calcError(Stack<Token> stack, String c, String filename, String line, int lineNum, int colNum) {
        Token t = stack.peek();
        if (t.getChar().equals(c)) {
            stack.pop();
        } else {
            System.out.println("[ERROR] Unexpected closing token in file '" + filename + "' line#" + lineNum + ":");
            System.out.println(line);
            for (int j = 0; j < colNum; j++) {
                System.out.print(" ");
            }
            System.out.println("^");
            return false;
        }
        return true;
    }

}

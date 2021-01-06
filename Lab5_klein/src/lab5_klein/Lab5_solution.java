package lab5_klein;

/**
 *
 * @author Matt
 */

import java.io.FileNotFoundException;

public class Lab5_solution {

    /* Excpected output:
    
    Processing 'test1.txt'
    ----------------------
    [ERROR] Unexpected closing token in file 'test1.txt' line#7:
    }
    ^
    [ERROR] Unmatched token in file 'test1.txt' line#3:
        if (a == b
           ^
    [ERROR] Unmatched token in file 'test1.txt' line#2:
    {
    ^
    Processing 'test2.txt'
    ----------------------
    [SUCCESS]
    Processing 'test3.txt'
    ----------------------
    [ERROR] Unexpected closing token in file 'test3.txt' line#3:
        if (a == b}
                  ^
    [ERROR] Unexpected closing token in file 'test3.txt' line#7:
    }
    ^
    [ERROR] Unmatched token in file 'test3.txt' line#3:
        if (a == b}
           ^
    [ERROR] Unmatched token in file 'test3.txt' line#2:
    {
    ^
    Processing 'test4.txt'
    ----------------------
    [SUCCESS]
    Processing 'test5.txt'
    ----------------------
    [SUCCESS]
    Processing 'test6.txt'
    ----------------------
    [SUCCESS]
    Processing 'test7.txt'
    ----------------------
    [ERROR] Unexpected closing token in file 'test7.txt' line#1:
    ([)]
      ^
    [ERROR] Unmatched token in file 'test7.txt' line#1:
    ([)]
    ^
    Processing 'test8.txt'
    ----------------------
    [ERROR] Unexpected closing token in file 'test8.txt' line#1:
    {()[}]
        ^
    [ERROR] Unmatched token in file 'test8.txt' line#1:
    {()[}]
    ^
    Processing 'test9.txt'
    ----------------------
    [ERROR] Unexpected closing token in file 'test9.txt' line#3:
        if (a == b}
                  ^
    [ERROR] Unexpected closing token in file 'test9.txt' line#7:
    }
    ^
    [ERROR] Unmatched token in file 'test9.txt' line#3:
        if (a == b}
           ^
    [ERROR] Unmatched token in file 'test9.txt' line#2:
    {
    ^
    Processing 'test10.txt'
    -----------------------
    [ERROR] Unexpected closing token in file 'test10.txt' line#1:
    [{}][({)}
           ^
    [ERROR] Unexpected closing token in file 'test10.txt' line#2:
    (({))
       ^
    [ERROR] Unexpected closing token in file 'test10.txt' line#2:
    (({))
        ^
    [ERROR] Unmatched token in file 'test10.txt' line#2:
    (({))
      ^
    [ERROR] Unmatched token in file 'test10.txt' line#2:
    (({))
     ^
    [ERROR] Unmatched token in file 'test10.txt' line#2:
    (({))
    ^
    [ERROR] Unmatched token in file 'test10.txt' line#1:
    [{}][({)}
         ^
    [ERROR] Unmatched token in file 'test10.txt' line#1:
    [{}][({)}
    
    */
    
    public static void main(String[] args) 
    {
        for (int i = 1; i <= 12; i++)
        {
            String f = "test" + i + ".txt";
            try
            {
                // This function should return true if no errors, false if
                // an errors.  The boolean parameter is to indicate if
                // we want console output or not (if not, there should be
                // no output whatsoever).
                SyntaxChecker.check(f, true);
            }
            catch (FileNotFoundException e)
            {
                System.out.println("Unable to find file '" + f + "'");
            }
        }
    }
    
}

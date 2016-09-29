import java.util.*;
import java.io.*;
import java.math.*;

class Solution {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int DEC = in.nextInt();
        int remainder;
        boolean negative = false;
        
        if (DEC < 0) {
            negative = true;
            DEC = DEC*(-1);
        }
        
        StringBuilder result = new StringBuilder();

        if (DEC == 0) {
            result.append(0);
        }

        while (DEC > 0) {
            remainder = DEC%3;
            DEC = DEC/3;
            
            switch (remainder) {
                case 0:
                    result.append(0);
                    break;
                case 1:
                    if (!negative) {
                        result.append(1);
                    } else {
                        result.append('T');
                    }
                    break;
                case 2:
                    DEC++;
                    if (!negative) {
                        result.append('T');
                    } else {
                        result.append(1);
                    }
                    break;
            }
        }
        
        System.out.println(result.reverse().toString());
    }
}

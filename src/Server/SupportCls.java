package Server;
/* Support class for calculation checkSum and etc stuff...*/

import java.util.ArrayList;
import java.util.Arrays;

public class SupportCls {

    public static String getCheckSum(String msg) {
        int sum = 0;
        for (int i = 0; i < msg.length(); i++) {
            sum += msg.charAt(i);
        }
        sum += 16; //adding CR and ETX
        sum = sum % 256;
        String checksum = Integer.toHexString(sum).toUpperCase();
        if (checksum.length() == 1) {
            checksum = "0" + checksum;
        }
        //System.out.println("\n Check Sum is ="+checksum);
        return checksum;
    }

    public static void printArray(ArrayList<String> arrayList) {
        for (String s : arrayList) {
            char[] sChar = s.toCharArray();
            for (char chr: sChar) {
                if (chr != 13 && chr != 10) {
                    System.out.print(chr);
                } else if (chr == 10)
                    System.out.print("\\n");
                else
                    System.out.print("\\r");
            }
            System.out.println();
        }
    }
}

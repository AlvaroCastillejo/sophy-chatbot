package utils;

import java.util.Scanner;

public class ScannerInput {
    private static Scanner in = new Scanner(System.in);

    public static String askString(){
        return in.nextLine();
    }

    public static int askInteger() {
        return in.nextInt();
    }

    public static void fflush() {
        in.nextLine();
    }

    public static double askDouble() {
        return in.nextDouble();
    }
}

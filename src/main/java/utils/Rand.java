package utils;

import java.util.Random;

public class Rand {
    public static int getRand(int low, int high){
        Random r = new Random();
        return r.nextInt(high-low) + low;
    }
}

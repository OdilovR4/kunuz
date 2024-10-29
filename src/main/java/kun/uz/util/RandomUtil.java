package kun.uz.util;

import java.util.Random;

public class RandomUtil {
    public static final Random rand = new Random();
    public static int getRandomInt(int n) {
        return rand.nextInt((int)Math.pow(10,n-1),(int)(Math.pow(10,n)-1));
    }
}

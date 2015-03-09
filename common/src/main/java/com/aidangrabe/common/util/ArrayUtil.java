package com.aidangrabe.common.util;

import java.util.Random;

/**
 * Created by aidan on 01/11/14.
 *
 */
public class ArrayUtil {

    public static int[] shuffle(int[] array) {
        Random rnd = new Random();
        for (int i = 0; i < array.length; i++) {
            int swp = array[i];
            int j = rnd.nextInt(array.length);
            array[i] = array[j];
            array[j] = swp;
        }
        return array;
    }

}

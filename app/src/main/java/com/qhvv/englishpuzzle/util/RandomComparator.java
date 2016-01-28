package com.qhvv.englishpuzzle.util;

import java.util.Comparator;
import java.util.Random;

/**
 * Created by voqua on 11/28/2015.
 */
public class RandomComparator implements Comparator {
    private Random random = new Random();
    public int compare(Object lhs, Object rhs) {
        return random.nextInt() - random.nextInt();
    }
}

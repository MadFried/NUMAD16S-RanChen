package edu.neu.madcourse.ranchen.asn3;

import java.util.ArrayList;

/**
 * Created by FredChen on 2/15/16.
 */
public class FileService {


    public static int binarySearch(ArrayList<String> integerList, String searchValue) {

        int low = 0;
        int high = integerList.size() - 1;
        int mid = (low + high) / 2;

        while (low <= high && !integerList.get(mid).equalsIgnoreCase(searchValue)) {

            if (integerList.get(mid).compareTo(searchValue) < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }

            mid = (low + high) / 2;

            if (low > high) {
                mid = -1;
            }

        }
        return mid;
    }

    public boolean findWord(ArrayList<String> strs, String s) {
        //Collections.sort(strs);
        int value = binarySearch(strs, s);
        if (value != -1) {
            return true;
        } else {
            return false;
        }
    }


}

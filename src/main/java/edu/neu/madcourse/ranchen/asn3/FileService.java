package edu.neu.madcourse.ranchen.asn3;

import android.content.res.Resources;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import edu.neu.madcourse.ranchen.R;

/**
 * Created by FredChen on 2/15/16.
 */
public class FileService {

    public FileService() {

    }

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

package com.nisoft.photopicker.util;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/8/8.
 */

public class FileUtil {
    public static ArrayList<String> getAllImagesName(String folderPath) {
        ArrayList<String> imagesName = new ArrayList<>();
        File file = new File(folderPath);
        if (!file.exists()) {
            return null;
        }
        String[] picsName = file.list();
        if (picsName == null || picsName.length == 0) {
            return null;
        }
        for (int i = 0; i < picsName.length; i++) {
            String[] strings = picsName[i].split("\\.");
            String type = strings[strings.length - 1];
            if (type.equals("jpg") || type.equals("bmp")) {
                imagesName.add(picsName[i]);
            }
        }
        return imagesName;
    }
}

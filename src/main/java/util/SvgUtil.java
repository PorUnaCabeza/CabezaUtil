package util;

import java.io.File;

/**
 * Created by Cabeza on 2016-09-12.
 */
public class SvgUtil {
    public void showAllFile(File dir) {
        if (!dir.isDirectory()) {
            System.out.println(dir.getAbsolutePath());
            //parse svg
            return;
        }
        for (File f : dir.listFiles()) {
            showAllFile(f);
        }
    }

    public static void main(String[] args) {
        new SvgUtil().showAllFile(new File("C://9-9diedai"));
    }
}

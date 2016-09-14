package util;

import dao.SvgDao;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Cabeza on 2016-09-12.
 */
public class SvgUtil {
    private static AtomicInteger count = new AtomicInteger(1);

    public void showAllFile(File file, int index) {
        String name = file.getName();
        String parent = file.getParentFile().getName();
        if (!file.isDirectory()) {
            SvgDao.insertSvg(count.intValue(), parseSvg(file, count.getAndIncrement()), parent);
            return;
        } else {
            if (index == -1)
                parent = null;
            SvgDao.insertCategory(name, parent, index + 1);
        }
        File[] list = file.listFiles();
        for (int i = 0; i < list.length; i++) {
            showAllFile(list[i], i);
        }
    }

    public String parseSvg(File file, int index) {
        Document doc = null;
        try {
            doc = Jsoup.parse(file, "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        doc.select("svg").first()
                .removeAttr("id")
                .attr("width", "100%")
                .attr("height", "100%");
        Elements gs = doc.select("g");
        for (Element g : gs) {
            g.removeAttr("id");
        }
        return doc.body().toString().replaceAll("<(/{0,1})body>", "");
    }


    public static void main(String[] args) {
        new SvgUtil().showAllFile(new File("C:\\10000000000000"), -1);
    }
}

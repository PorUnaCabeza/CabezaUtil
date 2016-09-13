package dao;

import org.n3r.eql.Eql;
import util.Collections;

/**
 * Created by zhangxs on 2016/9/13.
 */
public class SvgDao {

    public static boolean insertCategory(String categoryCode, String parentCode, int index) {
        int result = new Eql()
                .insert("insertCategory")
                .params(Collections.asMap(
                        "categoryCode", categoryCode,
                        "parentCode", parentCode,
                        "index", index))
                .execute();
        if (result > 0)
            return true;
        return false;
    }

    public static boolean insertSvg(int id, String content, String categoryCode) {
        int result = new Eql()
                .insert("insertSvg")
                .params(Collections.asMap(
                        "id", id,
                        "content", content,
                        "categoryCode", categoryCode
                ))
                .execute();
        if (result > 0)
            return true;
        return false;
    }

    public static void main(String[] args) {
        insertCategory("1", null, 0);
        insertSvg(1, "11", "001");
    }
}

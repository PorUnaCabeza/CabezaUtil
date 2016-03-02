package util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Zhangxs on 2016/1/7.
 */
public class HtmlModelUtil {
    private Document doc=null;
    public HtmlModelUtil(File in) throws IOException{
        this.doc=Jsoup.parse(in,"utf-8");
    }
    public HtmlModelUtil(String url) throws IOException {
        this.doc=Jsoup.connect(url).get();
    }

    /**
     * 将指定的class属性替换为id属性,若替换后class属性为空，会将class属性删除
     * e.g.：class="wrap-background" -> id="wrap-background"
     * @param className jQuery选择器，如".wrap-background"
     * @param idName jQuery选择器,如"#wrap-background"
     */
    public HtmlModelUtil classConvertToId(String className,String idName){
        Elements elmts=doc.select(className);
        for(Element elmt:elmts){
            elmt.removeClass(className.replaceAll("\\.",""));
            elmt.attr("id",idName.replaceAll("\\.|#",""));
            if(elmt.attr("class").isEmpty())
                elmt.removeAttr("class");
        }
        return this;
    }

    /**
     * 批量替换指定class名字
     * @param oriName jQuery选择器,class原名字
     * @param newName jQuery选择器,更改后class名字
     */
    public HtmlModelUtil classReName(String oriName,String newName){
        doc.select(oriName).removeClass(oriName.replaceAll("\\.","")).addClass(newName.replaceAll("\\.",""));
        return this;
    }

    /**
     * 将页面内所有元素style属性内长度单位px转换为rem  (只会转换style属性内值)
     * @param params 以键值对形式存放的参数,因html可能存在不规则书写，需手动指定替换规则
     *            key:替换起始位置
     *            value:替换结束位置(此处应固定为"px")
     *            e.g.：box-shadow:rgba(0, 0, 0, 0.4) 0px 0px 0px; 应添加：params.put(" ","px");
     *               style="border-radius:50px;"                应添加：params.put(":","px");
     * @param multiple px转换为rem时参照单位
     */
    public HtmlModelUtil pxConvert2Rem(Map<String,String> params,double multiple) {
        NodeVisitor visitor=new NodeVisitor() {
            int pxEndIndex=0;
            int pxStartIndex=0;
            double pxNum=0;
            @Override
            public void head(Node node, int i) {
                if (node.attr("style") != null && !node.attr("style").isEmpty()) {
                    StringBuffer sb = new StringBuffer(node.attr("style"));
                    for (Map.Entry<String, String> entry : params.entrySet()) {
                        replaceByTarget(entry.getKey(), entry.getValue(), sb);
                    }
                    node.attr("style", sb.toString());
                }
            }
            @Override
            public void tail(Node node, int i) {

            }
            public void replaceByTarget(String startTarget,String endTarget,StringBuffer sb){
                pxStartIndex=0;
                pxEndIndex=0;
                while(pxEndIndex!=-1){
                    pxEndIndex=sb.indexOf(endTarget,pxEndIndex+1);
                    pxStartIndex=sb.lastIndexOf(startTarget,pxEndIndex)+1;
                    if(pxEndIndex!=-1){
                        try{
                            pxNum = Double.parseDouble(sb.substring(pxStartIndex, pxEndIndex).trim());
                        }catch(NumberFormatException e){
                            continue;
                        }
                        if(pxNum<=1)
                            continue;
                        double remNum=pxNum/multiple;
                        sb.replace(pxStartIndex, pxEndIndex+2, remNum+"rem");
                        pxStartIndex=0;
                        pxEndIndex=0;
                    }
                }
            }
        };
        NodeTraversor tarversor=new NodeTraversor(visitor);
        tarversor.traverse(doc);
        return this;
    }

    /**
     *  在fatherElement内对childElements添加经过排序的attr属性,属性值为 tag序号,如"sd1","sd2"  (序号从1开始)
     * @param fatherElmtName jQuery选择器
     * @param childElmtName jQuery选择器
     * @param attr 添加的属性名
     * @param tag  属性值前缀
     */
    public HtmlModelUtil gradeChildElmtInFatherElmt(String fatherElmtName,String childElmtName,String attr,String tag){
        Elements elmts=doc.select(fatherElmtName);
        for(Element elmt:elmts){
            Elements imgElmts=elmt.select(childElmtName);
            for(int i=0;i<imgElmts.size();i++){
                Element imgElmt=imgElmts.get(i);
                imgElmt.attr(attr,tag+(i+1));
            }
        }
        return this;
    }

    /**
     * 将width、height属性删除并转换为style属性
     * e.g. :img src="page8-6.png" width="290px" height="290px"转换为：
     *    img src="page8-6.png" style="width:290px;height:290px;"
     * @param selector jQuery选择器
     * @return
     */
    public HtmlModelUtil formatElementStyle(String selector) {
        StringBuffer sb=new StringBuffer();
        Elements elmts = doc.select(selector);
        for(Element elmt:elmts){
            if(elmt.hasAttr("width")||elmt.hasAttr("height")){
                sb.setLength(0);
                sb.append(elmt.attr("style"));
                if(elmt.hasAttr("width")) {
                    sb.append("width:" + elmt.attr("width") + ";");
                    elmt.removeAttr("width");
                }
                if(elmt.hasAttr("height")){
                    sb.append("height:"+elmt.attr("height")+";");
                    elmt.removeAttr("height");
                }
                elmt.attr("style",sb.toString());
            }
        }
        return this;
    }

    /**
     * 使用指定的字符串替换选择器所有匹配目标指定属性内的子字符串
     *
     * @param selector    jQuery选择器
     * @param attr        元素内属性
     * @param target      要被替换的值
     * @param replacement 值的替换序列
     */
    public HtmlModelUtil replaceAttrContent(String selector, String attr, String target, String replacement) {
        Elements elmts = doc.select(selector);
        for (Element elmt : elmts) {
            if (!elmt.attr(attr).isEmpty())
                elmt.attr(attr, elmt.attr(attr).replace(target, replacement));
        }
        return this;
    }

    public HtmlModelUtil printHtml() {
        System.out.println(doc.toString());
        return this;
    }

    /**
     * 将Document保存到文件
     * @param fileName 文件名
     * @throws Exception
     */
    public void saveHtml(String fileName) throws Exception{
        FileOutputStream fos = new FileOutputStream(fileName);
        BufferedOutputStream bos
                = new BufferedOutputStream(fos);
        PrintWriter pw = new PrintWriter(bos, true);
        pw.print(doc.toString());
        pw.close();
    }

    //e.g.
    public static void main(String[] args) throws Exception {
        String fileName="E:\\javaee\\raiyee\\yoga-res\\admin\\dev\\modules\\showPage\\html\\prana-20160229.html";
        File in = new File(fileName);
        HtmlModelUtil hmu = new HtmlModelUtil(in);
        Map<String,String> params=new HashMap<String,String>();
        params.put(":","px");
        params.put(" ","px");
        params.put("\"", "px");

        hmu.formatElementStyle("img")
               /* .classConvertToId(".wrapper-background", "#wrapper-background")
                .classReName(".editable-image", ".editable-img")*/
                .pxConvert2Rem(params, 16)
                /*.gradeChildElmtInFatherElmt(".section", ".editable-img", "id", "sd")
                .replaceAttrContent("img", "src", "../dist/img/", "")
                .replaceAttrContent("div", "style", "../dist/img/", "")*/
                .saveHtml(fileName.replaceAll("\\.html","")+"-rem.html");
    }
}

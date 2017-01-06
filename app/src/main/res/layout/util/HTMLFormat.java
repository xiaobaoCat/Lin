package util;


/**
 * Created by Administrator on 2016/11/15 0015.
 */
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HTMLFormat {
    public static String getNewContent(String htmltext){

        Document doc= Jsoup.parse(htmltext);
        Elements elements=doc.getElementsByTag("img");
        for (Element element : elements) {
            element.attr("width","100%").attr("height","auto");
        }
        return doc.toString();
    }
}

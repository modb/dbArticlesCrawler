package dbArticlesCrawler;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author steven
 * @date 2018/11/7
 * @desc
 */

public class RhCrawler extends WebCrawler {

    /**
     * 正则表达式匹配指定的后缀文件
     */
    private final static Pattern FILTERS = Pattern.compile(
            ".*(\\.(css|js|bmp|gif|jpe?g|png|tiff?|mid|mp2|mp3|mp4|wav|avi|mov|mpeg|ram|m4v|pdf" +
                    "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

    /**
     * 获取参数
     */
    private RhCrawlXml rhCrawlXml;

    @Override
    public void onStart() {
        rhCrawlXml = (RhCrawlXml) myController.getCustomData();
    }

    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();// 得到小写的url
        System.out.println(rhCrawlXml.getStartUrl());
        return !FILTERS.matcher(href).matches() // 正则匹配，过滤掉我们不需要的后缀文件
                && href.startsWith(rhCrawlXml.getStartUrl());
    }

    /**
     * 当一个页面被提取并准备好被你的程序处理时，这个函数被调用。
     */
    @Override
    public void visit(Page page) {
        // String url = page.getWebURL().getURL();// 获取url
        if (page.getParseData() instanceof HtmlParseData) {// 判断是否是html数据
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();//// 强制类型转换，获取html数据对象
            //String text = htmlParseData.getText();//获取页面纯文本（无html标签
            String html = htmlParseData.getHtml();//获取页面Html
            //Set<WebURL> links = htmlParseData.getOutgoingUrls();// 获取页面输出链接
            htmlParse(html, rhCrawlXml.getOrigin(), rhCrawlXml.getBaseUrl(), rhCrawlXml.getCss1(), rhCrawlXml.getCss2(), rhCrawlXml.getCss3(), rhCrawlXml.getCss4());
        }
    }

    public void htmlParse(String html, String origin, String baseUrl, String css1, String css2, String css3, String css4) {
        Document doc = Jsoup.parse(html);
        Elements items = doc.select(css1);
        List<DhArticleStream> dhArticleStreamList = new ArrayList<>();
        for (Element item : items) {
            String title = item.select(css2).text();
            String link = item.select(css3).attr("href");
            String previewContent = item.select(css4).text();
            DhArticleStream dhArticleStream = new DhArticleStream(origin, title, previewContent, link, baseUrl);
            if (link != "" && link != null) {
                dhArticleStreamList.add(dhArticleStream);
            }
        }
        System.out.println("Crawler data from : " + origin + " size : " + items.size());
        PostgreSQLUtil.insertArticle(dhArticleStreamList);
    }

}


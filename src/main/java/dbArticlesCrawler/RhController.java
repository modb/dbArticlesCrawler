package dbArticlesCrawler;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import org.apache.http.message.BasicHeader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.HashSet;

/**
 * @author steven
 * @date 2018/11/7
 * @desc
 */
public class RhController {
    public static void main(String[] args) throws Exception {
            //File crawlXml = new File("/Users/stevenchang/site.xml");
            File crawlXml = new File(args[0]);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(crawlXml);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("seed");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    RhCrawlXml rhCrawlXml = new RhCrawlXml();
                    rhCrawlXml.setOrigin(eElement.getAttribute("origin"));
                    rhCrawlXml.setSeedUrl(eElement.getElementsByTagName("seedUrl").item(0).getTextContent());
                    rhCrawlXml.setStartUrl(eElement.getElementsByTagName("startUrl").item(0).getTextContent());
                    rhCrawlXml.setBaseUrl(eElement.getElementsByTagName("baseUrl").item(0).getTextContent());
                    rhCrawlXml.setCss1(eElement.getElementsByTagName("css1").item(0).getTextContent());
                    rhCrawlXml.setCss2(eElement.getElementsByTagName("css2").item(0).getTextContent());
                    rhCrawlXml.setCss3(eElement.getElementsByTagName("css3").item(0).getTextContent());
                    rhCrawlXml.setCss4(eElement.getElementsByTagName("css4").item(0).getTextContent());
                    crawlStart(rhCrawlXml);
                }
            }
    }

    public static void crawlStart(RhCrawlXml rhCrawlXml) throws Exception {
        //String crawlStorageFolder = "/Users/stevenchang/Downloads";// 定义爬虫数据存储位置
        String crawlStorageFolder = "/u01/dbhub";
        int numberOfCrawlers = 1;// 定义了1个爬虫，也就是1个线程
        CrawlConfig config = new CrawlConfig();// 定义爬虫配置
        config.setCrawlStorageFolder(crawlStorageFolder);// 设置爬虫文件存储位置
        config.setMaxDepthOfCrawling(0);
        HashSet<BasicHeader> collections = new HashSet<BasicHeader>();
        collections.add(new BasicHeader("User-Agent", "emcs"));
        collections.add(new BasicHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8"));
        collections.add(new BasicHeader("Accept-Encoding", "gzip,deflate,sdch"));
        collections.add(new BasicHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6"));
        collections.add(new BasicHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8"));
        collections.add(new BasicHeader("Connection", "keep-alive"));
        config.setDefaultHeaders(collections);
        /*
         * 实例化爬虫控制器。
         */
        PageFetcher pageFetcher = new PageFetcher(config);// 实例化页面获取器
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();// 实例化爬虫机器人配置
        // 实例化爬虫机器人对目标服务器的配置，每个网站都有一个robots.txt文件
        // 规定了该网站哪些页面可以爬，哪些页面禁止爬，该类是对robots.txt规范的实现
        robotstxtConfig.setUserAgentName("emcs");
        robotstxtConfig.setEnabled(false);

        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        // 实例化爬虫控制器
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

        /*
         * 对于每次抓取，您需要添加一些种子网址。 这些是抓取的第一个URL，然后抓取工具开始跟随这些页面中的链接
         */
        controller.addSeed(rhCrawlXml.getSeedUrl());

        /*
         * 启动爬虫，爬虫从此刻开始执行爬虫任务，根据以上配置
         */
        controller.setCustomData(rhCrawlXml);
        controller.startNonBlocking(RhCrawler.class, numberOfCrawlers);
        controller.waitUntilFinish();
    }
}

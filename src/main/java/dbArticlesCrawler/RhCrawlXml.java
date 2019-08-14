package dbArticlesCrawler;

import lombok.Data;

import java.io.Serializable;

/**
 * @author steven
 * @date 2018/11/9
 * @desc
 */
@Data
public class RhCrawlXml implements Serializable {
    private String origin;
    private String seedUrl;
    private String baseUrl;
    private String startUrl;
    private String css1;
    private String css2;
    private String css3;
    private String css4;
}

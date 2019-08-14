package dbArticlesCrawler; /**
 * @author steven
 * @date 2018/11/8
 * @desc
 */

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class DhArticleStream implements Serializable {
    private Long dhId;
    private String origin;
    private String title;
    private String previewContent;
    private String link;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private Date crawlTime;
    private Long viewCount;
    private Integer type;
    private Boolean featured;
    private String baseUrl;

    public DhArticleStream(String origin, String title, String previewContent, String link, String baseUrl) {
        this.origin = origin;
        this.title = title;
        this.previewContent = previewContent;
        this.link = link;
        this.baseUrl = baseUrl;
    }
}

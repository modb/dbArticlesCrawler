package dbArticlesCrawler;

import java.sql.*;
import java.util.Arrays;
import java.util.List;

/**
 * @author steven
 * @date 2018/11/12
 * @desc
 */
public class PostgreSQLUtil {
    public static Connection getConn() {
        Connection conn = null;
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager
                    .getConnection("jdbc:postgresql://192.168.57.4:5432/emcs",
                            "root", "123456");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static void insertArticle(List<DhArticleStream> dhArticleStreamList) {
        Connection conn = getConn();
        String sql = "insert into dh_article_stream(origin, title, preview_content, link, crawl_time, base_url) values" +
                "(?,?,substring(?,0,500),?,localtimestamp,?) ON CONFLICT (link) do nothing";
        PreparedStatement pstmt;
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            for (int i = 0; i < dhArticleStreamList.size(); i++) {
                pstmt.setObject(1, dhArticleStreamList.get(i).getOrigin());
                pstmt.setObject(2, dhArticleStreamList.get(i).getTitle());
                pstmt.setObject(3, dhArticleStreamList.get(i).getPreviewContent());
                pstmt.setObject(4, dhArticleStreamList.get(i).getLink());
                pstmt.setObject(5, dhArticleStreamList.get(i).getBaseUrl());
                pstmt.addBatch();
            }
            int rs[] = pstmt.executeBatch();
            System.out.println("Insert article count : " + Arrays.stream(rs).reduce(0, Integer::sum));
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

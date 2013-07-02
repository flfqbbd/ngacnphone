package gov.pianzong.util;

/**
 * Created by Administrator on 13-7-1.
 */
public class AppConstants {
    private static final  String NEWSLIST_URL_FORMAT = "http://www.cnbeta.com/api/getNewsList.php?limit=20&fromArticleId=%d";
    public  static  String getNewslistUrl(int startId){
        return  String.format(NEWSLIST_URL_FORMAT,startId);
    }
    private static  final String NEWS_CONTENT_URL_FORMAT = "http://www.cnbeta.com/api/getNewsContent.php?articleId=%d";

    public  static  String getNewsContentUrl(int articleId){
        return  String.format(NEWS_CONTENT_URL_FORMAT,articleId);
    }

    private static  final String COMMENT_URL_FORMAT = "http://www.cnbeta.com/capi/phone/comment?article=%d";
    public  static String getCommentUrl(int articleID){
        return String.format(COMMENT_URL_FORMAT,articleID);
    }
    public static final   String ARTICLE_ID = "article_id";
    public static final String COMMENT_COUNT = "comment_count";
}

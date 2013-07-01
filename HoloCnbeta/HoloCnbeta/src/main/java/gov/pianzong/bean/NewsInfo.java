package gov.pianzong.bean;

/**
 * Created by Administrator on 13-6-30.
 */
public class NewsInfo {
    private String title;
    private String pubtime;
    private int ArticleID;
    private int cmtClosed;
    private int cmtnum;
    private String summary;
    private String topicLogo;
    private String theme;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPubtime() {
        return pubtime;
    }

    public void setPubtime(String pubtime) {
        this.pubtime = pubtime;
    }

    public int getArticleID() {
        return ArticleID;
    }

    public void setArticleID(int articleID) {
        this.ArticleID = articleID;
    }

    public int getCmtClosed() {
        return cmtClosed;
    }

    public void setCmtClosed(int cmtClosed) {
        this.cmtClosed = cmtClosed;
    }

    public int getCmtnum() {
        return cmtnum;
    }

    public void setCmtnum(int cmtnum) {
        this.cmtnum = cmtnum;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTopicLogo() {
        return topicLogo;
    }

    public void setTopicLogo(String topicLogo) {
        this.topicLogo = topicLogo;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }
}

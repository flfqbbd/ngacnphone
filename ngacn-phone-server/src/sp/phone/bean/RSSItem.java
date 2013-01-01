package sp.phone.bean;

public class RSSItem {

	public final static String TITLE = "title";
	public final static String DESCRIPTION = "description";
	public final static String LINK = "link";
	public final static String AUTHOR = "author";
	public final static String PUBDATE = "pubdate";
	public final static String GUID = "guid";
	
	private String title;
	private String description;
	private String link;
	private String author;
	private String pubDate;
	private String guid;

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPubDate() {
		return pubDate;
	}

	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}

}

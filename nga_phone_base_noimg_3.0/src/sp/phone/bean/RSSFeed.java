package sp.phone.bean;

import java.util.ArrayList;
import java.util.List;

public class RSSFeed {

	private String title;
	private String link;
	private String description;
	private String lastBuildDate;

	public String getLastBuildDate() {
		return lastBuildDate;
	}

	public void setLastBuildDate(String lastBuildDate) {
		this.lastBuildDate = lastBuildDate;
	}

	private List<RSSItem> items;

	public List<RSSItem> getItems() {
		return items;
	}

	public void setItems(List<RSSItem> items) {
		this.items = items;
	}

	public RSSFeed() {
		items = new ArrayList<RSSItem>();
	}

	public void addRSSItem(RSSItem item) {
		items.add(item);
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

}

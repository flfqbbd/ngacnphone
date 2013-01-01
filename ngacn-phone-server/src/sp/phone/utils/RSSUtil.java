package sp.phone.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParserException;

import sp.phone.bean.RSSFeed;
import sp.phone.bean.RSSItem;

public class RSSUtil {

	private final static int TITLE = 1;
	private final static int LINK = 2;
	private final static int DESCRIPTION = 3;
	private final static int PUBDATE = 4;
	private final static int GUID = 5;
	private final static int AUTHOR = 6;
	private final static int LASTBUILDDATE = 7;
	private RSSFeed feed;
	private RSSItem item;
	private int type;
	private boolean reading_chan = false;

	public RSSFeed getFeed() {
		return feed;
	}

	private void characters(KXmlParser parser) {
		String s = parser.getText();

		switch (type) {
		case TITLE:
			if (reading_chan) {
				feed.setTitle(s);
			} else {
				item.setTitle(s);
			}
			type = 0;
			break;
		case LINK:
			if (reading_chan) {
				feed.setLink(s);
			} else {
				item.setLink(s);
			}
			type = 0;
			break;
		case DESCRIPTION:
			if (reading_chan) {
				feed.setDescription(s);
			} else {
				item.setDescription(s);
			}
			type = 0;
			break;
		case PUBDATE:
			item.setPubDate(s);
			type = 0;
			break;
		case GUID:
			item.setGuid(s);
			type = 0;
			break;
		case AUTHOR:
			item.setAuthor(s);
			type = 0;
			break;
		case LASTBUILDDATE:
			if (reading_chan) {
				feed.setLastBuildDate(s);
			}
			type = 0;
			break;
		}
	}

	private void endElement(KXmlParser parser) {
		String localName = parser.getName();
		if (localName.equals("item")) {
			feed.addRSSItem(item);
		}
	}

	private void startDocument(KXmlParser parser) {
		feed = new RSSFeed();
		item = new RSSItem();

	}

	private void startElement(KXmlParser parser) {
		String localName = parser.getName();
		if (localName.equals("channel")) {
			reading_chan = true;
		}

		if (localName.equals("item")) {
			type = 0;
			item = new RSSItem();
			reading_chan = false;
		} else if (localName.equals("title")) {
			type = TITLE;
			return;
		} else if (localName.equals("link")) {
			type = LINK;
			return;
		} else if (localName.equals("description")) {
			type = DESCRIPTION;
			return;
		} else if (localName.equals("pubdate")) {
			type = PUBDATE;
			return;
		} else if (localName.equals("author")) {
			type = AUTHOR;
			return;
		} else if (localName.equals("guid")) {
			type = GUID;
			return;
		} else if (localName.equals("lastBuildDate")) {
			type = LASTBUILDDATE;
			return;
		} else {
			type = 0;
		}

	}

	public void parseXml(String url) {
		KXmlParser parser = new KXmlParser();
		InputStream is = null;
		try {
			is = new URL(url).openStream();
			parser.setInput(is, "gbk");
			if (parser.getEventType() == KXmlParser.START_DOCUMENT) {
				startDocument(parser);
			}
			boolean keepParsing = true;
			while (keepParsing) {
				int type = parser.next();
				switch (type) {
				case KXmlParser.START_DOCUMENT:
					break;
				case KXmlParser.START_TAG:
					startElement(parser);
					break;
				case KXmlParser.END_TAG:
					endElement(parser);
					break;
				case KXmlParser.TEXT:
					characters(parser);
					break;
				case KXmlParser.END_DOCUMENT:
					keepParsing = false;
					break;
				}
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}

package sp.phone.utils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import sp.phone.bean.Article;
import sp.phone.bean.ArticlePage;
import sp.phone.bean.RSSFeed;
import sp.phone.bean.RSSItem;
import sp.phone.bean.User;
import sp.phone.pojo.DelayMap;

public class PollUtil {
	private ThreadPoolExecutor tpe = new ThreadPoolExecutor(2, 5000, 1,
			TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(),
			new ThreadPoolExecutor.DiscardOldestPolicy()) {
		@Override
		protected void afterExecute(Runnable r, Throwable t) {
			// 每个线程间隔2秒钟
			try {
				Thread.sleep(7000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	};

	private ThreadPoolExecutor tpe_image = new ThreadPoolExecutor(4, 5000, 1,
			TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(),
			new ThreadPoolExecutor.DiscardOldestPolicy()) {
		@Override
		protected void afterExecute(Runnable r, Throwable t) {
			// 每个线程间隔2秒钟
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	};

	/**
	 * 推送主题帖
	 * 
	 * @param uri_topic
	 *            主题帖地址
	 */
	private void poll_topic(String uri_topic) {

		RSSUtil rssUtil = new RSSUtil();
		rssUtil.parseXml(uri_topic);
		RSSFeed rssFeed = rssUtil.getFeed();
		for (RSSItem rssItem : rssFeed.getItems()) {
			final String uri_article = StringUtil.formatURI(rssItem.getGuid())
					+ "&page=1";
			poll_article(uri_article);
		}

	}

	/**
	 * 获取帖子列表
	 * 
	 * @param uri_article
	 *            内容帖子地址
	 */
	private void poll_article(String uri_article) {
		ArticlePage ap = dmap.get(uri_article);
		if (ap == null) {
			do_article(uri_article);
		} else {
			int size = ap.getListArticle().size();
			if (size != 20) {
				do_article(uri_article);
			}
		}
	}

	private void do_article(final String uri_article) {
		tpe.execute(new Runnable() {

			public void run() {

				ArticlePage articlePage = HttpUtil.getArticlePage(uri_article);
				if (articlePage != null) {

					dmap.update(uri_article, articlePage);
					// mapMaker.replace(uri_article, articlePage);
					HashMap<String, String> page = articlePage.getPage();
					if (page != null) { // 继续挖掘
						int page_first = Integer.parseInt(page.get("first")
								.split("page=")[1]);
						int page_last = Integer.parseInt(page.get("last")
								.split("page=")[1]);
						String link_b = page.get("current").split("page=")[0]
								+ "page=";
						for (int i = page_first; i <= page_last; i++) {

							final String link = StringUtil
									.formatURI(HttpUtil.NGA_HOST + link_b + i);

							if (!uri_article.equals(link)) {
								ArticlePage ap = dmap.get(link);
								// ArticlePage ap = mapMaker.get(link);
								if (ap == null) {
									tpe.execute(new Runnable() {

										public void run() {
											ArticlePage ap2 = HttpUtil
													.getArticlePage(link);
											if (ap2 != null) {
												dmap.put(link, ap2);

												// mapMaker.put(link, ap2);
//												List<Article> list = ap2
//														.getlArticle();
//												for (Article article : list) {
//													down(article);
//												}
											}
										}
									});
								} else {
									int size = ap.getListArticle().size();
									if (size != 20) {
										tpe.execute(new Runnable() {

											public void run() {
												ArticlePage ap2 = HttpUtil
														.getArticlePage(link);
												if (ap2 != null) {
													dmap.update(link, ap2);
													// mapMaker.replace(
													// uri_article, ap2);
												}
											}
										});
									}
								}
							}
						}
					}
				}
			}
		});
	}

	private void down(Article article) {
		User user = article.getUser();
		final String uri = user.getAvatarImage();
		final String newImage = ImageUtil.newImage(uri, user.getUserId());
		if (!StringUtil.isEmpty(newImage)) {
			final File file = new File(newImage);
			if (!file.exists()) {
				tpe_image.execute(new Runnable() {

					public void run() {
						System.out.println(uri);
						System.out.println(newImage);
//						HttpUtil.downImage(uri, newImage, 100);
						HttpUtil.downImage2(uri, newImage);
						
//						try {
//							ZoomImage.zoomImage(new URL(uri), file, 100);
//						} catch (MalformedURLException e) {
//							e.printStackTrace();
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
					}
				});
			} else {
				System.out.println("文件已经存在");
			}
		}
	}

	private DelayMap<String, ArticlePage> dmap = null;

	public void poll(int min, final String[] uris,
			DelayMap<String, ArticlePage> dmap) {
		this.dmap = dmap;
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				for (String uri : uris) {
					poll_topic(uri);// 抓取
				}
			}
		}, new Date(), min * 60000);// 6分钟一次
	}

}

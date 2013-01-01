package sp.phone.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipFile;

import sp.phone.bean.Article;
import sp.phone.bean.ArticlePage;
import sp.phone.utils.ActivityUtil;
import sp.phone.utils.HttpUtil;
import sp.phone.utils.StringUtil;

import android.app.Activity;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;

public class ArticleListActivity1 extends Activity {

	ActivityUtil activityUtil = new ActivityUtil(this);

	private TabHost tabHost;
	private ArticlePage articlePage;
	private HashMap<Object, ArticlePage> map_article;

	private MyApp app;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab2);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.title_bar);

		initDate();
		initView();
		setListener();
	}

	ZipFile zf;

	private void initDate() {
		app = ((MyApp) getApplication());
		articlePage = app.getArticlePage();
		map_article = app.getMap_article();
		zf = app.getZf();
	}

	SoundPool soundPool = null;
	private int hitOkSfx;

	private void initView() {

		soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
		// 载入音频流
		hitOkSfx = soundPool.load(this, R.raw.shake, 0);

		TextView titleTV = (TextView) findViewById(R.id.title);
		titleTV.setText(articlePage.getNow().get("title"));

		tabHost = (TabHost) findViewById(android.R.id.tabhost);
		tabHost.setup();
		tabHost.setBackgroundResource(ActivityUtil.bg);
		HashMap<String, String> page = articlePage.getPage();
		if (page == null || page.size() == 0) {
			TabSpec spec = tabHost.newTabSpec("tab" + 1);
			TextView tv = new TextView(ArticleListActivity1.this);
			tv.setText("首页");
			tv.setTextSize(20);
			spec.setIndicator(tv);
			spec.setContent(new tabFactory());
			tabHost.addTab(spec);
		} else {
			// 第一页
			TabSpec ts_first = tabHost.newTabSpec("tab_first");
			TextView tv2 = new TextView(ArticleListActivity1.this);
			tv2.setBackgroundResource(R.drawable.page_first);
			ts_first.setIndicator(tv2);
			ts_first.setContent(new tabFactory());
			tabHost.addTab(ts_first);

			// 列表
			for (HashMap<String, String> hashMap : articlePage.getList()) {
				String num = hashMap.get("num");
				TabSpec spec = tabHost.newTabSpec("tab_" + num);
				TextView tv = new TextView(ArticleListActivity1.this);
				tv.setTextSize(20);
				String now = page.get("num");
				// 定当前tab
				if (num.equals(now)) {
					tv.setText(num);
					tv.setTextColor(R.color.white);
					tv.setGravity(Gravity.CENTER);
					spec.setIndicator(tv);
					spec.setContent(new tabFactory());
					tabHost.addTab(spec);
					tabHost.setCurrentTabByTag("tab_" + num);
				} else {
					tv.setText(num);
					tv.setGravity(Gravity.CENTER);
					spec.setIndicator(tv);
					spec.setContent(new tabFactory2());
					tabHost.addTab(spec);
				}
			}

			String last = page.get("last");
			String current = page.get("current");
			if (!current.equals(last)) {
				// 下一页
				TextView tv3 = new TextView(ArticleListActivity1.this);
				tv3.setBackgroundResource(R.drawable.page_next);
				TabSpec ts_next = tabHost.newTabSpec("tab_next");
				ts_next.setIndicator(tv3);
				ts_next.setContent(new tabFactory2());
				tabHost.addTab(ts_next);
			}
			// 最后一页
			TabSpec ts_last = tabHost.newTabSpec("tab_last");
			TextView tv4 = new TextView(ArticleListActivity1.this);
			tv4.setBackgroundResource(R.drawable.page_last);
			ts_last.setIndicator(tv4);
			ts_last.setContent(new tabFactory2());
			tabHost.addTab(ts_last);
		}
	}

	private void setListener() {
		tabHost.setOnTabChangedListener(changeListener);
	}

	OnTabChangeListener changeListener = new OnTabChangeListener() {
		@Override
		public void onTabChanged(String tabId) {

			soundPool.play(hitOkSfx, 1, 1, 0, 0, 1);

			// 重新加载 数据
			HashMap<String, String> page = articlePage.getPage();
			String urls = HttpUtil.Server;
			if (tabId.equals("tab_first")) {
				urls += page.get("first");
			} else if (tabId.equals("tab_next")) {
				urls += page.get("next");
			} else if (tabId.equals("tab_last")) {
				urls += page.get("last");
			} else {
				String num = tabId.split("_")[1];
				for (HashMap<String, String> hashMap : articlePage.getList()) {
					if (num.equals(hashMap.get("num"))) {
						urls += hashMap.get("link");
						break;
					}
				}
			}

			if (urls.indexOf("\n") != -1) {
				urls = urls.substring(0, urls.length() - 1);
			}

			final String s = urls;

			ArticlePage ap2 = map_article.get(s);
			if (ap2 != null) {
				articlePage = ap2;
				System.gc();
				reBuild();
			} else {
				new Thread() {
					@Override
					public void run() {
						ArticlePage ap = null;
						if (!HttpUtil.HOST_PORT.equals("")) {
							String str = StringUtil.getSaying();
							if (str.indexOf(";") != -1) {
								activityUtil.notice("加速模式", str.split(";")[0]
										+ "-----" + str.split(";")[1]);
							} else {
								activityUtil.notice("加速模式", str);
							}
							String a = s.replace("&", "@");
							ap = HttpUtil.getArticlePageByJson(HttpUtil.HOST
									+ "?uri=" + a);

						}
						if (ap == null) {
							String str = StringUtil.getSaying();
							if (str.indexOf(";") != -1) {
								activityUtil.notice("普通模式", str.split(";")[0]
										+ "-----" + str.split(";")[1]);
							} else {
								activityUtil.notice("普通模式", str);
							}

							ap = HttpUtil.getArticlePage(s);
						}
						if (ap != null) {
							app.setArticlePage(ap);// 设置当前page
							map_article.put(s, ap);// 添加新的数据
							app.setMap_article(map_article);
							articlePage = ap;
							Message message = new Message();
							handler_rebuild.sendMessage(message);
						} else {
							activityUtil.notice("ERROR", "可能遇到了一个广告或者帖子被删除");
						}
						activityUtil.dismiss();

					}
				}.start();
			}

		}
	};

	Handler handler_rebuild = new Handler() {
		public void handleMessage(final Message msg) {
			System.gc();
			reBuild();
			activityUtil.dismiss();
		};
	};

	private void reBuild() {
		tabHost.setOnTabChangedListener(null);
		tabHost.setCurrentTab(0);
		tabHost.clearAllTabs();
		initView();
		setListener();
	}

	class tabFactory2 implements TabContentFactory {
		@Override
		public View createTabContent(String tag) {
			TextView view = new TextView(ArticleListActivity1.this);
			return view;
		}
	}

	class tabFactory implements TabContentFactory {

		@Override
		public View createTabContent(String tag) {
			if (!"tab_f".equals(tag)) {
				ListView listView = new ListView(ArticleListActivity1.this);
				mData = getData(tag);
				ArticleListAdapter adapter = new ArticleListAdapter(
						ArticleListActivity1.this, mData, listView, zf);
				listView.setAdapter(adapter);
				listView.setCacheColorHint(0);
				// listView.setDivider(null);
				// listView.setDividerHeight(4);
				// listView.setDrawingCacheBackgroundColor(0);
				listView.setVerticalScrollBarEnabled(false);
				return listView;
			} else {
				return new TextView(ArticleListActivity1.this);
			}
		}

		private List<HashMap<String, String>> mData;

		private List<HashMap<String, String>> getData(String tag) {
			List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
			for (Article article : articlePage.getListArticle()) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("nickName", article.getUser().getNickName());
				// map.put("title", article.getTitle());
				map.put("userId", article.getUser().getUserId() + "");
				map.put("content", article.getContent());
				// map.put("avatarImage", article.getUser().getAvatarImage());
				map.put("url", article.getUrl());
				map.put("floor", article.getFloor() + "");
				map.put("postTime", article.getLastTime());
				list.add(map);
			}
			return list;
		}
	}

}

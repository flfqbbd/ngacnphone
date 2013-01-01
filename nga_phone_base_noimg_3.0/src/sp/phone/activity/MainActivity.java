package sp.phone.activity;

import android.app.Activity;
import android.os.Bundle;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.zip.ZipFile;

import sp.phone.bean.RSSFeed;
import sp.phone.utils.ActivityUtil;
import sp.phone.utils.HttpUtil;
import sp.phone.utils.RSSUtil;
import sp.phone.utils.StringUtil;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {

	TextView tv_pre;
	TextView tv_now;
	TextView tv_error;
	ActivityUtil activityUtil = new ActivityUtil(this);
	private MyApp app;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		activityUtil.setBG();
		View view = LayoutInflater.from(this).inflate(R.layout.main, null);
		view.setBackgroundResource(ActivityUtil.bg);

		setContentView(view);
		initDate();
		initView();

	}

	private void initView() {
		setTitle("源于一个简单的想法");

		tv_pre = (TextView) findViewById(R.id.tv_pre);
		tv_now = (TextView) findViewById(R.id.tv_now);
		tv_error = (TextView) findViewById(R.id.tv_error);
		GridView gv = (GridView) findViewById(R.id.gride);
		ImageList2 adapter = new ImageList2(this);
		gv.setAdapter(adapter);
	}

	String error = "";
	int error_level = 0;

	private void initDate() {
		app = ((MyApp) getApplication());

		new Thread() {
			public void run() {

				delay("检查本地网络...");

				boolean f = check_net();
				if (f) {
					delay("选定可用网络");

					delay("搜寻加速服务器...");
					boolean status = HttpUtil.selectServer();
					// boolean status = false;
					if (!status) {
						delay("加速:否");
						error += "加速:否";
					} else {
						delay("加速服务器畅通");
					}

					delay("检查缓存目录...");

				} else {
					error_level = 1;
					delay("没有找到可用的网络");
					error += "没有找到可用的网络,";
				}

				if (error.equals("")) {
					delay("初始化完毕.");
				} else {
					delay_error(error);
				}
			};
		}.start();
	}

	private void delay_error(String error) {
		Message message = new Message();
		Bundle b = new Bundle();
		b.putString("error", error);
		message.setData(b);
		handler.sendMessage(message);
	}

	private void delay(String text) {
		Message message = new Message();
		Bundle b = new Bundle();
		b.putString("text_now", text);
		message.setData(b);
		handler.sendMessage(message);
	}

	private String text_pre;

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			Bundle b = msg.getData();
			if (b != null) {
				tv_pre.setText(text_pre);

				String error = b.getString("error");
				if (error != null) {
					tv_now.setVisibility(View.GONE);
					tv_error.setVisibility(View.VISIBLE);
					tv_error.setText(error);
					text_pre = error;
				} else {
					String text_now = b.getString("text_now");
					if (text_now != null) {
						tv_now.setVisibility(View.VISIBLE);
						tv_error.setVisibility(View.GONE);
						tv_now.setText(text_now);

					}
					text_pre = text_now;
				}

			}

		}
	};

	private boolean check_net() {
		ConnectivityManager cManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cManager.getActiveNetworkInfo();
		if (info != null && info.isConnected()) {
			return true;
		} else {
			return false;
		}
	}

	private void getData(final String url) {
		String str = StringUtil.getSaying();
		if (str.indexOf(";") != -1) {
			activityUtil.notice("加速模式", str.split(";")[0] + "-----"
					+ str.split(";")[1]);

		} else {
			activityUtil.notice("加速模式", str);
		}
		new Thread() {
			@Override
			public void run() {
				RSSUtil rssUtil = new RSSUtil();
				rssUtil.parseXml(url);
				RSSFeed rssFeed = rssUtil.getFeed();

				if (rssFeed != null && rssFeed.getItems().size() != 0) {
					// MyApp app = ((MyApp) getApplicationContext());
					app.setRssFeed(rssFeed);

					HashMap<Object, RSSFeed> map = new HashMap<Object, RSSFeed>();
					map.put(StringUtil.getNowPageNum(rssFeed.getLink()),
							rssFeed);
					app.setMap(map);
					Intent intent = new Intent();
					intent
							.setClass(MainActivity.this,
									TopicListActivity1.class);
					startActivity(intent);

				} else {
					activityUtil.notice("ERROR", "没有找到可用网络");
				}
				activityUtil.dismiss();
			}
		}.start();
	}

	int[] image = { R.drawable.hana, R.drawable.tf, R.drawable.dk,
			R.drawable.zs, R.drawable.lr, R.drawable.sm, R.drawable.dz,
			R.drawable.fs, R.drawable.xd, R.drawable.qs, R.drawable.ms,
			R.drawable.ss, };
	String[] names = { "艾泽拉斯议事厅", "台服讨论区", "黑锋要塞", "铁血沙场", "猎手大厅", "风暴祭坛",
			"暗影裂口", "魔法圣堂", "翡翠梦境", "圣光之力", "信仰神殿", "恶魔深渊" };
	String[] urls = { "7", "323", "320", "181", "187", "185", "189", "182",
			"186", "184", "183", "188" };

	// class ImageList extends BaseAdapter {
	// Activity activity;
	//
	// public ImageList(Activity a) {
	// activity = a;
	// }
	//
	// @Override
	// public int getCount() {
	// return image.length;
	// }
	//
	// @Override
	// public Object getItem(int position) {
	// return image[position];
	// }
	//
	// @Override
	// public long getItemId(int position) {
	// return position;
	// }
	//
	// @Override
	// public View getView(final int position, View convertView,
	// ViewGroup parent) {
	// final ImageView iv = new ImageView(activity);
	// iv.setImageResource(image[position]);
	// iv.setAdjustViewBounds(true);
	// if (error_level == 0) {
	// iv.setOnClickListener(new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	//
	// if (position != 0 && !HttpUtil.HOST_PORT.equals("")) {
	// HttpUtil.HOST = HttpUtil.HOST_PORT
	// + HttpUtil.Servlet_timer;
	// }
	//
	// System.out.println("set host:" + HttpUtil.HOST);
	//
	// String url = HttpUtil.Server + "/thread.php?fid="
	// + urls[position] + "&rss=1";
	// if (!StringUtil.isEmpty(url)) {
	// getData(url);
	// }
	// }
	// });
	// }
	// return iv;
	// }
	// }

	class ImageList2 extends BaseAdapter {
		Activity activity;

		public ImageList2(Activity a) {
			activity = a;
		}

		@Override
		public int getCount() {
			return image.length;
		}

		@Override
		public Object getItem(int position) {
			return image[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			final TextView iv = new TextView(activity);
			Drawable draw = getResources().getDrawable(image[position]);
			iv.setCompoundDrawablesWithIntrinsicBounds(null, draw, null, null);
			iv.setText(names[position]);
			iv.setGravity(Gravity.CENTER_HORIZONTAL);
			if (error_level == 0) {
				iv.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {

						if (position != 0 && !HttpUtil.HOST_PORT.equals("")) {
							HttpUtil.HOST = HttpUtil.HOST_PORT
									+ HttpUtil.Servlet_timer;
						}

						System.out.println("set host:" + HttpUtil.HOST);

						String url = HttpUtil.Server + "/thread.php?fid="
								+ urls[position] + "&rss=1";
						if (!StringUtil.isEmpty(url)) {
							getData(url);
						}
					}
				});
			}
			return iv;
		}
	}
}

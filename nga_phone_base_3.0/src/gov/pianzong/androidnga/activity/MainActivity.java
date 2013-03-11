package gov.pianzong.androidnga.activity;

import gov.pianzong.androidnga.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import sp.phone.adapter.BoardPagerAdapter;
import sp.phone.bean.Board;
import sp.phone.bean.BoardCategory;
import sp.phone.bean.BoardHolder;
import sp.phone.bean.PerferenceConstant;
import sp.phone.fragment.LoginFragment;
import sp.phone.interfaces.PageCategoryOwnner;
import sp.phone.task.AppUpdateCheckTask;
import sp.phone.utils.ActivityUtil;
import sp.phone.utils.HttpUtil;
import sp.phone.utils.PhoneConfiguration;
import sp.phone.utils.ReflectionUtil;
import sp.phone.utils.StringUtil;
import sp.phone.utils.ThemeManager;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.android.actionbarcompat.ActionBarActivity;



public class MainActivity extends ActionBarActivity
	implements PerferenceConstant,OnItemClickListener,PageCategoryOwnner{
	static final String TAG = MainActivity.class.getSimpleName();
	static final String RECENT = "最近访问";
	ActivityUtil activityUtil =ActivityUtil.getInstance();
	private MyApp app;
	ViewPager pager;
	View view;
	AppUpdateCheckTask task = null;
	OnItemClickListener onItemClickListenerlistener = new EnterToplistLintener();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.setTheme(R.style.AppTheme);
		Intent intent = getIntent();
		app = ((MyApp) getApplication());
		loadConfig(intent);
		initDate();
		initView();

		task = new AppUpdateCheckTask(this);
		task.execute("");
		
	}


	private void loadConfig(Intent intent) {
		//initUserInfo(intent);
		this.boardInfo = this.loadDefaultBoard();
		
			
		
	}

	
	@Override
	protected void onStop() {
		if(task != null){
			Log.d(TAG,"cancel update check task");
			task.cancel(true);
			task= null;
		}
		super.onStop();
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		
		final int flags = ThemeManager.ACTION_BAR_FLAG;
		/*
		int actionNum = ThemeManager.ACTION_IF_ROOM;//SHOW_AS_ACTION_IF_ROOM
		int i = 0;
		for(i = 0;i< menu.size();i++){
			ReflectionUtil.setShowAsAction(
					menu.getItem(i), actionNum);
		}
		*/
		ReflectionUtil.actionBar_setDisplayOption(this, flags);
		
		return super.onCreateOptionsMenu(menu);
	}
	

	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		int orentation = ThemeManager.getInstance().screenOrentation;
		if(orentation ==ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE||
				orentation ==ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
		{
			if( getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
				setRequestedOrientation(orentation);
		}else{
			if(getRequestedOrientation() ==ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE||
					getRequestedOrientation() ==ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
		}
		
		int width = getResources().getInteger(R.integer.page_category_width);
		pager.setAdapter(
				new BoardPagerAdapter( getSupportFragmentManager(),this,width) );	
		super.onResume();
	}

	public boolean isTablet() {
	    boolean xlarge = ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE);
	    boolean large = ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
	    return (xlarge || large) && ActivityUtil.isGreaterThan_2_3_3();
	}
	private void jumpToLogin() {
		if(isTablet())
		{
			DialogFragment df = new LoginFragment();
			df.show(getSupportFragmentManager(), "login");
			return;
		}

		
		Intent intent = new Intent();
		intent.setClass(MainActivity.this, LoginActivity.class);
		try {
			startActivity(intent);
			if(PhoneConfiguration.getInstance().showAnimation)
			{
				overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
			}
		} catch (Exception e) {

		}

	}

	private void jumpToSetting() {
		Intent intent = new Intent();
		intent.setClass(MainActivity.this, SettingsActivity.class);
		try {
			startActivity(intent);
			if(PhoneConfiguration.getInstance().showAnimation)
				overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
			
		} catch (Exception e) {

			
		}

	}
	
	void jumpToNearby(){
		Intent intent = new Intent();
		intent.setClass(MainActivity.this, NearbyUserActivity.class);
		
			startActivity(intent);
			if(PhoneConfiguration.getInstance().showAnimation)
				overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
		
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.mainmenu_login:
			this.jumpToLogin();
			break;
		case R.id.mainmenu_setting:
			this.jumpToSetting();
			break;
		case R.id.mainmenu_exit:
			//case android.R.id.home: //this is a system id
			//this.finish();
			jumpToNearby();
			break;
		default:
			/*Intent MyIntent = new Intent(Intent.ACTION_MAIN);
			MyIntent.addCategory(Intent.CATEGORY_HOME);
			startActivity(MyIntent);*/
			finish();
			break;

		}
		return true;
	}

	private void initView() {

		setTitle("源于一个简单的想法");
		
		ThemeManager.SetContextTheme(this);
		view = LayoutInflater.from(this).inflate(R.layout.viewpager_main, null);
		view.setBackgroundResource(
			ThemeManager.getInstance().getBackgroundColor());
		setContentView(view);

		pager = (ViewPager) findViewById(R.id.pager);
		


		
		if(app.isNewVersion()){
			new AlertDialog.Builder(this).setTitle("提示")
			.setMessage(StringUtil.getTips())
			.setPositiveButton("知道了", null).show();
			
			app.setNewVersion(false);
			
		}
		
		
	}
	
	
	



	private void initDate() {



		new Thread() {
			public void run() {


				
				
				
				File filebase = new File(HttpUtil.PATH);
				if (!filebase.exists()) {
					delay("创建新的缓存目录");
					filebase.mkdirs();
				} 
				if(ActivityUtil.isGreaterThan_2_1())
				{
					File f = new File(HttpUtil.PATH_AVATAR_OLD);
					if(f.exists()){
						f.renameTo(new File(HttpUtil.PATH_AVATAR));
						delay("移动头像到新位置");
					}
				}

				
				File file = new File(HttpUtil.PATH_NOMEDIA);
				if (!file.exists()) {
					Log.i(getClass().getSimpleName(),"create .nomedia");
					try {
						file.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
					}
				} 
				
				
			}
		}.start();
	
	}
	
	/*public BoardCategory getCategory(int page){
		if(this.boardInfo == null)
			return null;
		return boardInfo.getCategory(page);
	}*/
	private BoardHolder loadDefaultBoard(){
		
		MyApp app = (MyApp) getApplication();
		return app.loadDefaultBoard();
		/*BoardHolder boards = new BoardHolder();
		
		int i= 0;
		
		SharedPreferences share = getSharedPreferences(PERFERENCE,
				MODE_PRIVATE);
		String recentStr = share.getString(RECENT_BOARD, "");
		List<Board> recentList = null;
		if(!StringUtil.isEmpty(recentStr)){
			recentList = JSON.parseArray(recentStr, Board.class);
			if(recentList != null){
				for(int j = 0;j< recentList.size();j++){
					boards.add(recentList.get(j));
				}
			}
		}
		if(recentList != null)
		{
		boards.addCategoryName(i, RECENT);
		i++;
		}
		
		boards.add(new Board(i, "7", "议事厅", R.drawable.p7));
		boards.add(new Board(i, "323", "台服讨论区", R.drawable.p323));
		boards.add(new Board(i, "-7", "大漩涡", R.drawable.p354));
		boards.add(new Board(i, "10", "银色黎明", R.drawable.p10));
		boards.add(new Board(i, "230", "艾泽拉斯风纪委员会", R.drawable.p230));
		boards.add(new Board(i, "387", "潘大力亚之迷雾", R.drawable.p387));
		boards.add(new Board(i, "414", "游戏综合讨论", R.drawable.p414));
		boards.add(new Board(i, "305", "305权贵区", R.drawable.pdefault));
		boards.addCategoryName(i, "综合讨论");
		i++;
		
		boards.add(new Board(i, "390", "五晨寺", R.drawable.p390));
		boards.add(new Board(i, "320", "黑锋要塞", R.drawable.p320));
		boards.add(new Board(i, "181", "铁血沙场", R.drawable.p181));
		boards.add(new Board(i, "182", "魔法圣堂", R.drawable.p182));
		boards.add(new Board(i, "183", "信仰神殿", R.drawable.p183));
		boards.add(new Board(i, "185", "风暴祭坛", R.drawable.p185));
		boards.add(new Board(i, "186", "翡翠梦境", R.drawable.p186));
		boards.add(new Board(i, "187", "猎手大厅", R.drawable.p187));
		boards.add(new Board(i, "184", "圣光之力", R.drawable.p184));
		boards.add(new Board(i, "188", "恶魔深渊", R.drawable.p188));
		boards.add(new Board(i, "189", "暗影裂口", R.drawable.p189));
		boards.addCategoryName(i, "职业讨论区");
		i++;
		
		boards.add(new Board(i, "310", "精英议会", R.drawable.p310));
		boards.add(new Board(i, "190", "任务讨论", R.drawable.p190));
		boards.add(new Board(i, "213", "战争档案", R.drawable.p213));
		boards.add(new Board(i, "218", "副本专区", R.drawable.p218));
		boards.add(new Board(i, "258", "战场讨论", R.drawable.p258));
		boards.add(new Board(i, "272", "竞技场", R.drawable.p272));
		boards.add(new Board(i, "191", "地精商会", R.drawable.p191));
		boards.add(new Board(i, "200", "插件研究", R.drawable.p200));
		boards.add(new Board(i, "240", "BigFoot", R.drawable.p240));
		boards.add(new Board(i, "274", "插件发布", R.drawable.p274));
		boards.add(new Board(i, "315", "战斗统计", R.drawable.p315));
		boards.add(new Board(i, "333", "DKP系统", R.drawable.p333));
		boards.add(new Board(i, "327", "成就讨论", R.drawable.p327));
		boards.add(new Board(i, "388", "幻化讨论", R.drawable.p388));
		boards.add(new Board(i, "411", "宠物讨论", R.drawable.p411));
		boards.add(new Board(i, "255", "公会管理", R.drawable.p10));
		boards.add(new Board(i, "306", "人员招募", R.drawable.p10));
		boards.addCategoryName(i, "冒险心得");
		i++;
		
		boards.add(new Board(i, "264", "卡拉赞剧院", R.drawable.p264));
		boards.add(new Board(i, "8", "大图书馆", R.drawable.p8));
		boards.add(new Board(i, "102", "作家协会", R.drawable.p102));
		boards.add(new Board(i, "124", "壁画洞窟", R.drawable.pdefault));
		boards.add(new Board(i, "254", "镶金玫瑰", R.drawable.p254));
		boards.add(new Board(i, "355", "龟岩兄弟会", R.drawable.p355));
		boards.add(new Board(i, "116", "奇迹之泉", R.drawable.p116));
		boards.addCategoryName(i, "麦迪文之塔");
		i++;
		
		
		boards.add(new Board(i, "173", "帐号安全", R.drawable.p193));
		boards.add(new Board(i, "201", "系统问题", R.drawable.p201));
		boards.add(new Board(i, "334", "硬件配置", R.drawable.p334));
		boards.add(new Board(i, "335", "网站开发", R.drawable.p335));
		boards.addCategoryName(i, "系统软硬件讨论");
		i++;
		
		boards.add(new Board(i, "414", "游戏综合讨论", R.drawable.p414));
		boards.add(new Board(i, "412", " 巫师之怒", R.drawable.p412));
		boards.add(new Board(i, "318", "Diablo III", R.drawable.p318));
		boards.add(new Board(i, "-46468", "坦克世界", R.drawable.p46468));
		boards.add(new Board(i, "332", "战锤40K", R.drawable.p332));
		boards.add(new Board(i, "321", "DotA", R.drawable.p321));
		boards.add(new Board(i, "353", "纽沃斯英雄传", R.drawable.pdefault));
		boards.add(new Board(i, "-2371813", "EVE", R.drawable.p2371813));
		boards.add(new Board(i, "-793427", "斗战神", R.drawable.pdefault));
		boards.add(new Board(i, "416", "火炬之光2", R.drawable.pdefault));
		boards.add(new Board(i, "406", "星际争霸2", R.drawable.pdefault));
		boards.addCategoryName(i, "其他游戏");
		i++;
		
		boards.add(new Board(i, "318", "暗黑破坏神3", R.drawable.p318));
		boards.add(new Board(i, "409", "HC讨论区", R.drawable.p403));
		boards.add(new Board(i, "403", "购买/安装/共享", R.drawable.pdefault));
		boards.add(new Board(i, "351", "装备交易", R.drawable.p401));
		boards.add(new Board(i, "393", "背景故事与文艺作品", R.drawable.p393));
		boards.add(new Board(i, "400", "职业讨论区", R.drawable.pdefault));
		boards.add(new Board(i, "395", "野蛮人", R.drawable.p395));
		boards.add(new Board(i, "396", "猎魔人", R.drawable.p396));
		boards.add(new Board(i, "397", "武僧", R.drawable.p397));
		boards.add(new Board(i, "398", "巫医", R.drawable.p398));
		boards.add(new Board(i, "399", "魔法师", R.drawable.p399));
		boards.addCategoryName(i, "暗黑破坏神");
		i++;
		
		boards.add(new Board(i, "-522474", "综合体育讨论区", R.drawable.pdefault));
		boards.add(new Board(i, "-152678", "英雄联盟", R.drawable.p152678));
		boards.add(new Board(i, "-1068355", "晴风村", R.drawable.pdefault));
		boards.add(new Board(i, "-447601", " 二次元国家地理", R.drawable.houzi));
		boards.add(new Board(i, "-343809", "寂寞的车俱乐部", R.drawable.pdefault));
		boards.add(new Board(i, "-131429", "红茶馆——小说馆", R.drawable.pdefault));
		boards.add(new Board(i, "-46468", " 坦克世界", R.drawable.pdefault));
		boards.add(new Board(i, "-2371813", "NGA驻吉他海四办公室", R.drawable.pdefault));
		boards.add(new Board(i, "-124119", "菠萝方舟 ", R.drawable.pdefault));
		boards.add(new Board(i, "-84", " 模玩之魂", R.drawable.pdefault));
		boards.add(new Board(i, "-187579", " 大旋涡历史博物馆", R.drawable.pdefault));
		boards.add(new Board(i, "-308670", "血库的个人空间", R.drawable.pdefault));
		boards.add(new Board(i, "-112905", "八圣祠", R.drawable.pdefault));
		boards.add(new Board(i, "-8725919", "小窗视界", R.drawable.pdefault));
		boards.add(new Board(i, "-608808", "血腥厨房", R.drawable.pdefault));
		boards.add(new Board(i, "-469608", "影视讨论", R.drawable.pdefault));
		boards.add(new Board(i, "-55912", "音乐讨论", R.drawable.pdefault));
		boards.add(new Board(i, "-353371", "傻乎乎的小宠物", R.drawable.pdefault));
		boards.add(new Board(i, "-538800", "乙女向二次元", R.drawable.pdefault));
		boards.add(new Board(i, "-522679", "Battlefield 3", R.drawable.pdefault));
		boards.add(new Board(i, "-7678526", "麻将科学院", R.drawable.pdefault));
		boards.add(new Board(i, "-202020", "一只IT喵的自我修养", R.drawable.pdefault));
		boards.add(new Board(i, "-444012", "我们的骑迹", R.drawable.pdefault));
		boards.add(new Board(i, "-47218", " 没有刀的漆器", R.drawable.pdefault));
		boards.add(new Board(i, "-349066", "开心茶园", R.drawable.pdefault));
		boards.add(new Board(i, "-314508", "世界尽头的百货公司", R.drawable.pdefault));		
		boards.addCategoryName(i, "个人版面");
		i++;
		
		
		return boards;*/
	}


	private BoardHolder boardInfo;
	





	private void delay(String text) {
		final String msg = text;
		this.runOnUiThread(new Runnable(){

			@Override
			public void run() {
				Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
			}
			
		});
	}



	

	

	

	class EnterToplistLintener implements OnItemClickListener , OnClickListener {
		int position;
		String fidString;

		public EnterToplistLintener(int position, String fidString) {
			super();
			this.position = position;
			this.fidString = fidString;
		}
		public EnterToplistLintener(){//constructoer	
		}

		public void onClick(View v) {

			if (position != 0 && !HttpUtil.HOST_PORT.equals("")) {
				HttpUtil.HOST = HttpUtil.HOST_PORT + HttpUtil.Servlet_timer;
			}
			int fid = 0;
			try {
				fid = Integer.parseInt(fidString);
			} catch (Exception e) {
				final String tag = this.getClass().getSimpleName();
				Log.e(tag, Log.getStackTraceString(e));
				Log.e(tag, "invalid fid " + fidString);
			}
			if (fid == 0) {
				String tip = fidString + "不是合法的板块id";
				Toast.makeText(app, tip, Toast.LENGTH_LONG).show();
				return;
			}

			Log.i(this.getClass().getSimpleName(), "set host:" + HttpUtil.HOST);

			String url = HttpUtil.Server + "/thread.php?fid=" + fidString
					+ "&rss=1";
			PhoneConfiguration config = PhoneConfiguration.getInstance();
			if ( !StringUtil.isEmpty(config.getCookie())) {

				url = url + "&" + config.getCookie().replace("; ", "&");
			}else if(fid<0){
				/*new AlertDialog.Builder(MainActivity.this).setTitle("提示")
				.setMessage("个人板块要登录了才能进去")
				.setPositiveButton("知道了", null).show();*/
				jumpToLogin();
				return;
			}
			
			
			addToRecent();
			if (!StringUtil.isEmpty(url)) {
				Intent intent = new Intent();
				intent.putExtra("tab", "1");
				intent.putExtra("fid", fid);
				intent.setClass(MainActivity.this, config.topicActivityClass);
				//intent.setClass(MainActivity.this, TopicListActivity.class);
				startActivity(intent);
				if(PhoneConfiguration.getInstance().showAnimation)
				{
					overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
				}
			}
		}

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			position = arg2;
			fidString=(String) arg0.getItemAtPosition(position);
			onClick(arg1);
			
		}
		
		private void saveRecent(List<Board> boardList){
			String rescentStr = JSON.toJSONString(boardList);
			SharedPreferences share = getSharedPreferences(PERFERENCE,
					MODE_PRIVATE);
			Editor editor = share.edit();
			editor.putString(RECENT_BOARD, rescentStr);
			editor.commit();
			
		}
		
		private void addToRecent() {
			
			boolean recentAlreadExist = boardInfo.getCategoryName(0).equals(RECENT);
			
			BoardCategory recent = boardInfo.getCategory(0);
			if(recent != null && recentAlreadExist)
				recent.remove(fidString);
			//int i = 0;
			for (int i = 0; i < boardInfo.getCategoryCount(); i++) {
				BoardCategory curr = boardInfo.getCategory(i);
				for (int j = 0; j < curr.size(); j++) {
					Board b = curr.get(j);
					if (b.getUrl().equals(fidString)) {
						Board b1 =new Board(0, b.getUrl(), b.getName(), b
								.getIcon());

						if(!recentAlreadExist){
							List<Board> boardList = new ArrayList<Board>();
							boardList.add(b1);
							saveRecent(boardList);
							boardInfo = loadDefaultBoard();
							return;
						}else{
							recent.addFront(b1);
						}
						recent = boardInfo.getCategory(0);
						this.saveRecent(recent.getBoardList());

						return;
					}//if
				}//for j
				
			}//for i
			
		}
	}




	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		this.onItemClickListenerlistener.onItemClick(parent, view, position, id);
		
	}


	@Override
	public int getCategoryCount() {
		if(boardInfo == null)
			return 0;
		return boardInfo.getCategoryCount();
	}


	@Override
	public String getCategoryName(int position) {
		if(boardInfo == null)
			return "";
		return boardInfo.getCategoryName(position);
	}


	@Override
	public BoardCategory getCategory(int category) {
		if(boardInfo == null)
			return null;
		return boardInfo.getCategory(category);
	}

}



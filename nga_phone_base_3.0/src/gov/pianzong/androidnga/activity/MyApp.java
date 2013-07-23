package gov.pianzong.androidnga.activity;

import gov.pianzong.androidnga.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import sp.phone.bean.Board;
import sp.phone.bean.BoardHolder;
import sp.phone.bean.Bookmark;
import sp.phone.bean.PerferenceConstant;
import sp.phone.bean.User;
import sp.phone.utils.ActivityUtil;
import sp.phone.utils.HttpUtil;
import sp.phone.utils.PhoneConfiguration;
import sp.phone.utils.StringUtil;
import sp.phone.utils.ThemeManager;
import android.annotation.TargetApi;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.os.Environment;
import android.util.Log;

import com.alibaba.fastjson.JSON;

public class MyApp extends Application implements PerferenceConstant {
	final private static String TAG = MyApp.class.getSimpleName();
	public final static int version = 544;
	private PhoneConfiguration config = null;
	boolean newVersion = false;
	static final String RECENT = "������";
	
	
	@Override
	public void onCreate() {
		Log.w(TAG,"app nga androind start");
		//CrashHandler crashHandler = CrashHandler.getInstance();
		//crashHandler.init(getApplicationContext());
		if(config == null)
			config = PhoneConfiguration.getInstance();
		initUserInfo();
		loadConfig();
		if(ActivityUtil.isGreaterThan_2_1())
			initPath();
		
		loadDefaultBoard();
		super.onCreate();
	}
	
	public BoardHolder loadDefaultBoard(){
		
		BoardHolder boards = new BoardHolder();
		
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
		
		boards.add(new Board(i, "7", "������", R.drawable.p7));
		boards.add(new Board(i, "323", "̨��������", R.drawable.p323));
		boards.add(new Board(i, "-7", "������", R.drawable.p354));
        boards.add(new Board(i, "-43", "С����", R.drawable.pdefault));
		boards.add(new Board(i, "10", "��ɫ����", R.drawable.p10));
		boards.add(new Board(i, "230", "������˹���ίԱ��", R.drawable.p230));
		boards.add(new Board(i, "387", "�˴�����֮����", R.drawable.p387));
		boards.add(new Board(i, "414", "��Ϸ�ۺ�����", R.drawable.p414));
		boards.add(new Board(i, "305", "305Ȩ����", R.drawable.pdefault));
		boards.add(new Board(i, "11", "ŵɭ����ǵ�", R.drawable.pdefault));
		boards.addCategoryName(i, "�ۺ�����");
		i++;
		
		boards.add(new Board(i, "390", "�峿��", R.drawable.p390));
		boards.add(new Board(i, "320", "�ڷ�Ҫ��", R.drawable.p320));
		boards.add(new Board(i, "181", "��Ѫɳ��", R.drawable.p181));
		boards.add(new Board(i, "182", "ħ��ʥ��", R.drawable.p182));
		boards.add(new Board(i, "183", "�������", R.drawable.p183));
		boards.add(new Board(i, "185", "�籩��̳", R.drawable.p185));
		boards.add(new Board(i, "186", "����ξ�", R.drawable.p186));
		boards.add(new Board(i, "187", "���ִ���", R.drawable.p187));
		boards.add(new Board(i, "184", "ʥ��֮��", R.drawable.p184));
		boards.add(new Board(i, "188", "��ħ��Ԩ", R.drawable.p188));
		boards.add(new Board(i, "189", "��Ӱ�ѿ�", R.drawable.p189));
		boards.addCategoryName(i, "ְҵ������");
		i++;
		
		boards.add(new Board(i, "310", "��Ӣ���", R.drawable.p310));
		boards.add(new Board(i, "190", "��������", R.drawable.p190));
		boards.add(new Board(i, "213", "ս��", R.drawable.p213));
		boards.add(new Board(i, "218", "����ר��", R.drawable.p218));
		boards.add(new Board(i, "258", "ս������", R.drawable.p258));
		boards.add(new Board(i, "272", "������", R.drawable.p272));
		boards.add(new Board(i, "191", "�ؾ��̻�", R.drawable.p191));
		boards.add(new Board(i, "200", "����о�", R.drawable.p200));
		boards.add(new Board(i, "240", "BigFoot", R.drawable.p240));
		boards.add(new Board(i, "274", "�������", R.drawable.p274));
		boards.add(new Board(i, "315", "ս��ͳ��", R.drawable.p315));
		boards.add(new Board(i, "333", "DKPϵͳ", R.drawable.p333));
		boards.add(new Board(i, "327", "�ɾ�����", R.drawable.p327));
		boards.add(new Board(i, "388", "�û�����", R.drawable.p388));
		boards.add(new Board(i, "411", "��������", R.drawable.p411));
		boards.add(new Board(i, "255", "�������", R.drawable.p10));
		boards.add(new Board(i, "306", "��Ա��ļ", R.drawable.p10));
		boards.addCategoryName(i, "ð���ĵ�");
		i++;
		
		boards.add(new Board(i, "264", "�����޾�Ժ", R.drawable.p264));
		boards.add(new Board(i, "8", "��ͼ���", R.drawable.p8));
		boards.add(new Board(i, "102", "����Э��", R.drawable.p102));
		boards.add(new Board(i, "124", "�ڻ�����", R.drawable.pdefault));
		boards.add(new Board(i, "254", "���õ��", R.drawable.p254));
		boards.add(new Board(i, "355", "�����ֵܻ�", R.drawable.p355));
		boards.add(new Board(i, "116", "�漣֮Ȫ", R.drawable.p116));
		boards.addCategoryName(i, "�����֮��");
		i++;
		
		
		boards.add(new Board(i, "173", "�ʺŰ�ȫ", R.drawable.p193));
		boards.add(new Board(i, "201", "ϵͳ����", R.drawable.p201));
		boards.add(new Board(i, "334", "Ӳ������", R.drawable.p334));
		boards.add(new Board(i, "335", "��վ����", R.drawable.p335));
		boards.addCategoryName(i, "ϵͳ��Ӳ������");
		i++;

        boards.add(new Board(i, "414", "��Ϸ�ۺ�����", R.drawable.p414));
        boards.add(new Board(i, "427", "��������", R.drawable.p427));
        boards.add(new Board(i, "425", " ���Ǳ߼�2", R.drawable.p425));
        boards.add(new Board(i, "426", " �ֻ���Ϸ", R.drawable.p426));
		boards.add(new Board(i, "422", " ¯ʯ��˵", R.drawable.pdefault));
		boards.add(new Board(i, "412", " ��ʦ֮ŭ", R.drawable.p412));
		boards.add(new Board(i, "318", "Diablo III", R.drawable.p318));
		boards.add(new Board(i, "-46468", "̹������", R.drawable.p46468));
		boards.add(new Board(i, "332", "ս��40K", R.drawable.p332));
		boards.add(new Board(i, "321", "DotA", R.drawable.p321));
		boards.add(new Board(i, "353", "Ŧ��˹Ӣ�۴�", R.drawable.pdefault));
		boards.add(new Board(i, "-2371813", "EVE", R.drawable.p2371813));
		boards.add(new Board(i, "-793427", "��ս��", R.drawable.pdefault));
		boards.add(new Board(i, "416", "���֮��2", R.drawable.pdefault));
		boards.add(new Board(i, "406", "�Ǽ����2", R.drawable.pdefault));
		boards.add(new Board(i, "-65653", "����", R.drawable.p65653));
		boards.add(new Board(i, "-235147", "��ս2", R.drawable.p235147));
		boards.add(new Board(i, "-7861121", "���� ", R.drawable.pdefault));
		boards.add(new Board(i, "420", "MT", R.drawable.pdefault));
		boards.add(new Board(i, "-1513130", "��Ѫ�ֵܻ�", R.drawable.pdefault));
        boards.add(new Board(i, "424", "ʥ��ʿ", R.drawable.pdefault));
		boards.addCategoryName(i, "������Ϸ");
		i++;
		
		boards.add(new Board(i, "318", "�����ƻ���3", R.drawable.p318));
		boards.add(new Board(i, "409", "HC������", R.drawable.p403));
		boards.add(new Board(i, "403", "����/��װ/����", R.drawable.pdefault));
		boards.add(new Board(i, "351", "װ������", R.drawable.p401));
		boards.add(new Board(i, "393", "����������������Ʒ", R.drawable.p393));
		boards.add(new Board(i, "400", "ְҵ������", R.drawable.pdefault));
		boards.add(new Board(i, "395", "Ұ����", R.drawable.p395));
		boards.add(new Board(i, "396", "��ħ��", R.drawable.p396));
		boards.add(new Board(i, "397", "��ɮ", R.drawable.p397));
		boards.add(new Board(i, "398", "��ҽ", R.drawable.p398));
		boards.add(new Board(i, "399", "ħ��ʦ", R.drawable.p399));
		boards.addCategoryName(i, "�����ƻ���");
		i++;
		
		boards.add(new Board(i, "-522474", "�ۺ�����������", R.drawable.pdefault));
		boards.add(new Board(i, "-152678", "Ӣ������", R.drawable.p152678));
		boards.add(new Board(i, "-1068355", "����", R.drawable.pdefault));
		boards.add(new Board(i, "-447601", " ����Ԫ��ҵ���", R.drawable.houzi));
		boards.add(new Board(i, "-343809", "��į�ĳ����ֲ�", R.drawable.pdefault));
		boards.add(new Board(i, "-131429", "���ݡ���С˵��", R.drawable.pdefault));
		boards.add(new Board(i, "-46468", " ̹������", R.drawable.pdefault));
		boards.add(new Board(i, "-2371813", "NGAפ�����İ칫��", R.drawable.pdefault));
		boards.add(new Board(i, "-124119", "���ܷ��� ", R.drawable.pdefault));
		boards.add(new Board(i, "-84", " ģ��֮��", R.drawable.pdefault));
		boards.add(new Board(i, "-187579", " ��������ʷ�����", R.drawable.pdefault));
		boards.add(new Board(i, "-308670", "Ѫ��ĸ��˿ռ�", R.drawable.pdefault));
		boards.add(new Board(i, "-112905", "��ʥ��", R.drawable.pdefault));
		boards.add(new Board(i, "-8725919", "С���ӽ�", R.drawable.pdefault));
		boards.add(new Board(i, "-608808", "Ѫ�ȳ�", R.drawable.pdefault));
		boards.add(new Board(i, "-469608", "Ӱ������", R.drawable.pdefault));
		boards.add(new Board(i, "-55912", "��������", R.drawable.pdefault));
		boards.add(new Board(i, "-353371", "ɵ������С����", R.drawable.pdefault));
		boards.add(new Board(i, "-538800", "��Ů�����Ԫ", R.drawable.pdefault));
		boards.add(new Board(i, "-522679", "Battlefield 3", R.drawable.pdefault));
		boards.add(new Board(i, "-7678526", "�齫��ѧԺ", R.drawable.pdefault));
		boards.add(new Board(i, "-202020", "һֻIT������������", R.drawable.pdefault));
		boards.add(new Board(i, "-444012", "���ǵ��Ｃ", R.drawable.pdefault));
		boards.add(new Board(i, "-47218", " û�е�������", R.drawable.pdefault));
		boards.add(new Board(i, "-349066", "���Ĳ�԰", R.drawable.pdefault));
		boards.add(new Board(i, "-314508", "���羡ͷ�İٻ���˾", R.drawable.pdefault));		
		boards.add(new Board(i, "-2671", "�����", R.drawable.pdefault));
		boards.add(new Board(i, "-168888", "����", R.drawable.pdefault));
        boards.add(new Board(i, "-54214", "ʱ�а�", R.drawable.pdefault));
        boards.add(new Board(i, "-970841", "���Ƕ�", R.drawable.pdefault));

        boards.addCategoryName(i, "���˰���");
		//i++;
		
		
		return boards;
	}
	
	@TargetApi(8)
	private void initPath(){
		File baseDir = getExternalCacheDir();
		if(baseDir!= null)
			HttpUtil.PATH = baseDir.getAbsolutePath();
		else
			HttpUtil.PATH = android.os.Environment
					.getExternalStorageDirectory()
					+"/Android/data/gov.pianzong.androidnga";
		HttpUtil.PATH_AVATAR = HttpUtil.PATH +
				 "/nga_cache";
		HttpUtil.PATH_NOMEDIA = HttpUtil.PATH + "/.nomedia";
        HttpUtil.PATH_IMAGES = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
    }

	private void initUserInfo() {
		

		
		PhoneConfiguration config = PhoneConfiguration.getInstance();

		SharedPreferences share = this.getSharedPreferences(PERFERENCE,
				MODE_PRIVATE);

		
			final String uid = share.getString(UID, "");
			final String cid = share.getString(CID, "");
			if (!StringUtil.isEmpty(uid) && !StringUtil.isEmpty(cid) ) {
				config.setUid(uid);
				config.setCid(cid);
				String userListString = share.getString(USER_LIST, "");
				final String name = share.getString(USER_NAME, "");
				config.userName = name;
				if(StringUtil.isEmpty(userListString)){
					
					addToUserList(uid,cid,name);
	
				}
			}

			boolean downImgWithoutWifi = share.getBoolean(DOWNLOAD_IMG_NO_WIFI, false);
			config.setDownImgNoWifi(downImgWithoutWifi);
			boolean downAvatarNoWifi = share.getBoolean(DOWNLOAD_AVATAR_NO_WIFI, false);
			config.setDownAvatarNoWifi(downAvatarNoWifi);

	}

	public void addToUserList(String uid, String cid, String name){
		SharedPreferences share = this.getSharedPreferences(PERFERENCE,
				MODE_PRIVATE);
		
		String userListString = share.getString(USER_LIST, "");
		
		List<User> userList = null;
		//new ArrayList<User>();
		if(StringUtil.isEmpty(userListString)){
			userList = new ArrayList<User>();
		}else
		{
			userList = JSON.parseArray(userListString, User.class);
			for( User u : userList){
				if(u.getUserId().equals(uid)){
					userList.remove(u);
					break;
				}
					
			}
		}
		
		User user = new User();
		user.setCid(cid);
		user.setUserId(uid);
		user.setNickName(name);
		userList.add(0,user);
		
		userListString = JSON.toJSONString(userList);
		share.edit().putString(UID, uid).putString(CID, cid)
		.putString(USER_NAME, name )
		.putString(USER_LIST, userListString).commit();
		
	}
	
	private void loadConfig(){
		
		SharedPreferences share = this.getSharedPreferences(PERFERENCE,
				MODE_PRIVATE);
		if(share.getBoolean(NIGHT_MODE, false))
			ThemeManager.getInstance().setMode(1);
		
		ThemeManager.getInstance().screenOrentation = 
				share.getInt(SCREEN_ORENTATION,ActivityInfo.SCREEN_ORIENTATION_USER);
		
		
		int version_in_config = share.getInt(VERSION, 0);
		if(version_in_config < version){
			newVersion = true;
			Editor editor = share.edit();
			editor.putInt(VERSION, version);
			editor.putBoolean(REFRESH_AFTER_POST, false);
			
			String recentStr = share.getString(RECENT_BOARD, "");
			List<Board> recentList = null;
			if(!StringUtil.isEmpty(recentStr)){
				recentList = JSON.parseArray(recentStr, Board.class);
				if(recentList != null){
					for(int j = 0;j< recentList.size();j++){
						recentList.get(j).setIcon(R.drawable.pdefault);
					}
					recentStr = JSON.toJSONString(recentList);
					editor.putString(RECENT_BOARD,recentStr);
				}
			}
			
			editor.commit();
			
		}
		

		//refresh
		PhoneConfiguration config = PhoneConfiguration.getInstance();
		config.setRefreshAfterPost(
				share.getBoolean(REFRESH_AFTER_POST,false));
		config.setRefreshAfterPost(false);
		
		config.showAnimation = share.getBoolean(SHOW_ANIMATION, true);
		config.useViewCache = share.getBoolean(USE_VIEW_CACHE, true);
		config.showSignature = share.getBoolean(SHOW_SIGNATURE, false);
		config.uploadLocation = share.getBoolean(UPLOAD_LOCATION, false);
		config.showStatic = share.getBoolean(SHOW_STATIC,false);

		//font
		final float defTextSize = 21.0f;//new TextView(this).getTextSize();
		final int defWebSize = 16;//new WebView(this).getSettings().getDefaultFontSize();
		
		final float textSize = share.getFloat(TEXT_SIZE, defTextSize);
		final int webSize = share.getInt(WEB_SIZE, defWebSize);
		config.setTextSize(textSize);
		config.setWebSize(webSize);
		
		boolean notification = share.getBoolean(ENABLE_NOTIFIACTION, true);
		boolean notificationSound = share.getBoolean(NOTIFIACTION_SOUND, true);
		config.notification = notification;
		config.notificationSound = notificationSound;
		
		config.nikeWidth = share.getInt(NICK_WIDTH, 100);
		
		int uiFlag = share.getInt(UI_FLAG, 0);
		config.setUiFlag(uiFlag);
		
		//bookmarks
		String bookmarkJson = share.getString(BOOKMARKS, "");
		List<Bookmark> bookmarks = new ArrayList<Bookmark>();
		try{
		if(!bookmarkJson.equals(""))
			bookmarks=JSON.parseArray(bookmarkJson, Bookmark.class);
		}catch(Exception e){
			Log.e("JSON_error",Log.getStackTraceString(e));
		}
		PhoneConfiguration.getInstance().setBookmarks(bookmarks);
		
		
		
	}
	
	
	
	public boolean isNewVersion() {
		return newVersion;
	}

	public void setNewVersion(boolean newVersion) {
		this.newVersion = newVersion;
	}

	

	

	public PhoneConfiguration getConfig() {
		return config;
	}




}

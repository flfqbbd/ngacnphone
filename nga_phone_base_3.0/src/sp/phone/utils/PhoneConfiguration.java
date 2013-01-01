package sp.phone.utils;

import gov.pianzong.androidnga.activity.ArticleListActivity;
import gov.pianzong.androidnga.activity.FlexibleTopicListActivity;
import gov.pianzong.androidnga.activity.HaArticleListActivity;
import gov.pianzong.androidnga.activity.HaFlexibleTopicListActivity;
import gov.pianzong.androidnga.activity.HaSplitArticleListActivity;
import gov.pianzong.androidnga.activity.HaSplitFlexibleTopicListActivity;
import gov.pianzong.androidnga.activity.SplitArticleListActivity;
import gov.pianzong.androidnga.activity.SplitFlexibleTopicListActivity;

import java.util.ArrayList;
import java.util.List;

import android.location.Location;
import sp.phone.bean.Bookmark;
import sp.phone.bean.PerferenceConstant;

public class PhoneConfiguration implements PerferenceConstant{
	private static PhoneConfiguration instance;
	private boolean refreshAfterPost;
	List<Bookmark> bookmarks;// url<-->tilte
	private float textSize;
	private int webSize;
	public String userName;
	public int nikeWidth = 100;
	public boolean downAvatarNoWifi;
	public boolean downImgNoWifi;
	public boolean notification;
	public boolean notificationSound;
	public long lastMessageCheck = 0;
	public String cid;
	public String uid;
	public boolean showAnimation=false;
	public boolean showSignature = true;
	public boolean useViewCache;
	public Location location = null;
	public boolean uploadLocation = false;
	private int uiFlag = 0;


	public Class<?> topicActivityClass = FlexibleTopicListActivity.class;
	public Class<?> articleActivityClass = ArticleListActivity.class;
	
	
	public int getNikeWidth() {
		return nikeWidth;
	}
	public void setNikeWidth(int nikeWidth) {
		this.nikeWidth = nikeWidth;
	}
	public boolean isDownAvatarNoWifi() {
		return downAvatarNoWifi;
	}
	public void setDownAvatarNoWifi(boolean downAvatarNoWifi) {
		this.downAvatarNoWifi = downAvatarNoWifi;
	}
	public boolean isDownImgNoWifi() {
		return downImgNoWifi;
	}
	public void setDownImgNoWifi(boolean downImgNoWifi) {
		this.downImgNoWifi = downImgNoWifi;
	}
	public boolean isNotification() {
		return notification;
	}
	public void setNotification(boolean notification) {
		this.notification = notification;
	}
	public boolean isNotificationSound() {
		return notificationSound;
	}
	public void setNotificationSound(boolean notificationSound) {
		this.notificationSound = notificationSound;
	}
	public long getLastMessageCheck() {
		return lastMessageCheck;
	}
	public void setLastMessageCheck(long lastMessageCheck) {
		this.lastMessageCheck = lastMessageCheck;
	}
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public float getTextSize() {
		return textSize;
	}
	public void setTextSize(float textSize) {
		this.textSize = textSize;
	}
	public int getWebSize() {
		return webSize;
	}
	public void setWebSize(int webSize) {
		this.webSize = webSize;
	}
	private PhoneConfiguration(){

		bookmarks= new ArrayList<Bookmark>();

		
	}
	public boolean isRefreshAfterPost() {
		return refreshAfterPost;
	}
	public void setRefreshAfterPost(boolean refreshAfterPost) {
		this.refreshAfterPost = refreshAfterPost;
	}
	public static PhoneConfiguration getInstance(){
		if(instance ==null){
			instance = new PhoneConfiguration();
		}
		return instance;
	}
	public List<Bookmark> getBookmarks() {
		return bookmarks;
	}
	
	public void setBookmarks(List<Bookmark> bookmarks) {
		this.bookmarks = bookmarks;
	}
	
	public String getCookie(){
		if( !StringUtil.isEmpty(uid) && !StringUtil.isEmpty(cid)){
			return "ngaPassportUid="+ uid+
					"; ngaPassportCid=" + cid;
		}
		return "";
	}
	
	public boolean addBookmark(String url,String title){
		boolean ret = true;
		for(Bookmark b:bookmarks){
			if(b.getUrl().equals(url))
				return false;
			
		}
		Bookmark newBookmark = new Bookmark();
		newBookmark.setTitle(title);
		newBookmark.setUrl(url);
		bookmarks.add(newBookmark);
		return ret;
	}
	
	public boolean removeBookmark(String url){

		for(Bookmark b:bookmarks){
			if(b.getUrl().equals(url)){
				bookmarks.remove(b);
				return true;
			}
		}
		return false;
		
	}
	

	public int getUiFlag() {
		return uiFlag;
	}

	public void setUiFlag(int uiFlag) {
		this.uiFlag = uiFlag;
		switch (uiFlag) {

		case UI_FLAG_SPLIT:
			topicActivityClass = SplitFlexibleTopicListActivity.class;
			articleActivityClass = SplitArticleListActivity.class;
			break;
		case UI_FLAG_HA:
			topicActivityClass = HaFlexibleTopicListActivity.class;
			articleActivityClass = HaArticleListActivity.class;
			break;
		case (UI_FLAG_SPLIT + UI_FLAG_HA):
			topicActivityClass = HaSplitFlexibleTopicListActivity.class;
			articleActivityClass = HaSplitArticleListActivity.class;
			break;
		case 0:
		default:
			topicActivityClass = FlexibleTopicListActivity.class;
			articleActivityClass = ArticleListActivity.class;
		}
	}

}

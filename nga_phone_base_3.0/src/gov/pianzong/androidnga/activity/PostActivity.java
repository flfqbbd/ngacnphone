package gov.pianzong.androidnga.activity;

import gov.pianzong.androidnga.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import org.apache.commons.io.IOUtils;

import sp.phone.adapter.SpinnerUserListAdapter;
import sp.phone.bean.User;
import sp.phone.forumoperation.HttpPostClient;
import sp.phone.forumoperation.ThreadPostAction;
import sp.phone.fragment.EmotionCategorySelectFragment;
import sp.phone.fragment.EmotionDialogFragment;
import sp.phone.fragment.ExtensionEmotionFragment;
import sp.phone.interfaces.EmotionCategorySelectedListener;
import sp.phone.interfaces.OnEmotionPickedListener;
import sp.phone.task.FileUploadTask;
import sp.phone.utils.ActivityUtil;
import sp.phone.utils.Des;
import sp.phone.utils.PhoneConfiguration;
import sp.phone.utils.ReflectionUtil;
import sp.phone.utils.StringUtil;
import sp.phone.utils.ThemeManager;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.actionbarcompat.ActionBarActivity;

public class PostActivity extends ActionBarActivity
	implements FileUploadTask.onFileUploaded,
	EmotionCategorySelectedListener,
	OnEmotionPickedListener{

	private final String LOG_TAG = Activity.class.getSimpleName();
	static private final String EMOTION_CATEGORY_TAG = "emotion_category";
	static private final String EMOTION_TAG = "emotion";
	private String prefix;
	private EditText titleText;
	private EditText bodyText;
	private ThreadPostAction act; 
	private String action;
	private String tid;
	private int fid;
	//private Button button_commit;
	//private Button button_cancel;
	//private ImageButton button_upload;
	//private ImageButton button_emotion;
	Object commit_lock = new Object();
	private Spinner userList;
	private String REPLY_URL="http://bbs.ngacn.cc/post.php?";
	final int REQUEST_CODE_SELECT_PIC = 1;

	private boolean loading;
	private FileUploadTask  uploadTask = null;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		int orentation = ThemeManager.getInstance().screenOrentation;
		if(orentation ==ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE||
				orentation ==ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
		{
			setRequestedOrientation(orentation);
		}else{
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
		}

		
		super.onCreate(savedInstanceState);
		View v = this.getLayoutInflater().inflate(R.layout.reply, null);
		v.setBackgroundColor(getResources()
				.getColor(
					ThemeManager.getInstance().getBackgroundColor()
				));
		this.setContentView(v);
		
		if(PhoneConfiguration.getInstance().uploadLocation
				&& PhoneConfiguration.getInstance().location == null
				)
		{
			ActivityUtil.reflushLocation(this);
		}

		Intent intent = this.getIntent();
		prefix = intent.getStringExtra("prefix");
		action = intent.getStringExtra("action");
		if(action.equals("new")){
			this.setTitle(R.string.new_thread);
		}else if(action.equals("reply")){
			setTitle(R.string.reply_thread);
			
		}else if(action.equals("modify")){
			setTitle(R.string.modify_thread);
			
		}
		tid = intent.getStringExtra("tid");
		fid = intent.getIntExtra("fid", -7);
		String title = intent.getStringExtra("title");
		String pid = intent.getStringExtra("pid");
		String mention = intent.getStringExtra("mention");
		if(tid == null)
			tid = "";


		
		act = new ThreadPostAction(tid, "", "");
		act.setAction_(action);
		act.setFid_(fid);
		if(!StringUtil.isEmpty(mention))
			act.setMention_(mention);
		if(pid !=null)
			act.setPid_(pid);
		loading = false;
		
		titleText = (EditText) findViewById(R.id.reply_titile_edittext);
		if(title!=null){
			titleText.setText(title);
		}
		titleText.setSelected(true);
		bodyText = (EditText) findViewById(R.id.reply_body_edittext);
		bodyText.setText(prefix);
		if(prefix != null)
			bodyText.setSelection(prefix.length());
		ThemeManager tm = ThemeManager.getInstance();
		if(tm.getMode() == ThemeManager.MODE_NIGHT ){
			bodyText.setBackgroundResource(tm.getBackgroundColor());
			titleText.setBackgroundResource(tm.getBackgroundColor());
			int textColor = this.getResources().getColor(tm.getForegroundColor());
			bodyText.setTextColor(textColor);
			titleText.setTextColor(textColor);
		}

		/*
		button_commit = (Button)findViewById(R.id.reply_commit_button);
		button_cancel = (Button)findViewById(R.id.reply_cancel_button);
		button_upload = (ImageButton) findViewById(R.id.imageButton_upload);
		
		button_commit.setOnClickListener(new ButtonCommitListener(REPLY_URL));
		button_cancel.setOnClickListener( new OnClickListener(){
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PostActivity.this.finish();
		 	}
			}
		);
		
		button_upload.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
                Intent intent = new Intent();  
                intent.setType("image/*");  
                intent.setAction(Intent.ACTION_GET_CONTENT);   
                startActivityForResult(intent,  REQUEST_CODE_SELECT_PIC);  
				
			}
			
		}
		);
		button_emotion = (ImageButton) findViewById(R.id.imageButton_emotion);
		
		button_emotion.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
				Fragment prev = getSupportFragmentManager().findFragmentByTag
						(EMOTION_CATEGORY_TAG);
				if (prev != null) {
		            ft.remove(prev);
		        }

		        DialogFragment newFragment = new EmotionCategorySelectFragment();
		        newFragment.show(ft, EMOTION_CATEGORY_TAG);
				
			}
			
		}
		);*/
		
		
		userList = (Spinner) findViewById(R.id.user_list);
		if (userList != null) {
			SpinnerUserListAdapter adapter = new SpinnerUserListAdapter(this);
			userList.setAdapter(adapter);
			userList.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					User u = (User) parent.getItemAtPosition(position);
					MyApp app = (MyApp) getApplication();
					app.addToUserList(u.getUserId(), u.getCid(),
							u.getNickName());
					PhoneConfiguration.getInstance().setUid(u.getUserId());
					PhoneConfiguration.getInstance().setCid(u.getCid());

				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
				}

			});
		}else{
			this.setNavigation();
		}
	}
	
	@TargetApi(11)
	private void setNavigation(){
		ActionBar actionBar = getActionBar();
		 actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		 
		 final SpinnerUserListAdapter categoryAdapter = new SpinnerUserListAdapter(this);
		 OnNavigationListener callback = new OnNavigationListener(){

			@Override
			public boolean onNavigationItemSelected(int itemPosition,
					long itemId) {
				User u = (User)categoryAdapter.getItem(itemPosition);
				MyApp app = (MyApp) getApplication();
				app.addToUserList(u.getUserId(), u.getCid(),
						u.getNickName());
				PhoneConfiguration.getInstance().setUid(u.getUserId());
				PhoneConfiguration.getInstance().setCid(u.getCid());
				return true;
			}
			 
		 };
		actionBar.setListNavigationCallbacks(categoryAdapter, callback);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		 getMenuInflater().inflate(R.menu.post_menu, menu);
		 final int flags = 7;
		 /*
		  * ActionBar.DISPLAY_SHOW_HOME;//2
			flags |= ActionBar.DISPLAY_USE_LOGO;//1
			flags |= ActionBar.DISPLAY_HOME_AS_UP;//4
		  */
		 ReflectionUtil.actionBar_setDisplayOption(this, flags);
		 return true;
	}

	private ButtonCommitListener commitListener = null;
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case R.id.upload :
            Intent intent = new Intent();  
            intent.setType("image/*");  
            intent.setAction(Intent.ACTION_GET_CONTENT);   
            startActivityForResult(intent,  REQUEST_CODE_SELECT_PIC);  
            break;
		case R.id.emotion:
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			Fragment prev = getSupportFragmentManager().findFragmentByTag
					(EMOTION_CATEGORY_TAG);
			if (prev != null) {
	            ft.remove(prev);
	        }

	        DialogFragment newFragment = new EmotionCategorySelectFragment();
	        newFragment.show(ft, EMOTION_CATEGORY_TAG);
			break;
		case R.id.send:
			if(commitListener == null)
			{
				commitListener = new ButtonCommitListener(REPLY_URL);
			}
			commitListener.onClick(null);
			break;
		default:
			finish();
		}
		return true;
	}

	@Override
	public void onEmotionPicked(String emotion){
		bodyText.setText( bodyText.getText().toString() + emotion);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_CANCELED ||data == null )
			return;
		switch(requestCode)
		{
		case REQUEST_CODE_SELECT_PIC :
				Log.i(LOG_TAG, " select file :" + data.getDataString() );
				uploadTask = new FileUploadTask(this, this, data.getData());
				break;
		default:
				;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}



	@Override
	protected void onResume() {
		
		if(uploadTask != null){
			FileUploadTask temp = uploadTask;
			uploadTask = null;
			if(ActivityUtil.isGreaterThan_2_3_3()){
				RunParallel(temp);
			}
			else
			{
				temp.execute();
			}
		}
		super.onResume();
	}

	private String buildSig()
	{
		StringBuilder sb  = new StringBuilder();
		sb.append("\n[url=https://play.google.com/store/apps/details?id=gov.pianzong.androidnga");
		PhoneConfiguration config = PhoneConfiguration.getInstance();
		if(config.location != null && config.uploadLocation)
		{
			String loc = new StringBuilder().append(config.location.getLatitude())
							.append(",")
							.append(config.location.getLongitude()).toString();
			sb.append("&");
			try {
				sb.append(Des.enCrypto(loc, StringUtil.key));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			sb.append("ffff");
		} 
		sb.append("]----sent from my ")
		.append(android.os.Build.MANUFACTURER).append(" ")
		.append(android.os.Build.MODEL).append(",Android ")
		.append(android.os.Build.VERSION.RELEASE)
		.append("[/url]\n");

		
		return sb.toString();
		
	}
	
	@TargetApi(11)
	private void RunParallel(FileUploadTask task){
		task.executeOnExecutor(FileUploadTask.THREAD_POOL_EXECUTOR);
	}


	class ButtonCommitListener implements OnClickListener{

		private final String url;		
		ButtonCommitListener(String url){
			this.url = url;
		}
		@Override
		public void onClick(View v) {
			synchronized(commit_lock){
				if(loading == true){
					String avoidWindfury = PostActivity.this.getString(R.string.avoidWindfury);
					Toast.makeText(PostActivity.this, avoidWindfury, Toast.LENGTH_SHORT).show();
					return ;
				}
				loading = true;
			}
			
			if(action.equals("reply")){
				handleReply(v);
			}else if(action.equals("new")){
				handleNewThread(v);
			}else if(action.equals("modify")){
				handleNewThread(v);
			}
		}
		public void handleNewThread(View v){
			handleReply(v);
			
		}
		
		public void handleReply(View v1) {


			act.setPost_subject_(titleText.getText().toString());
			String bodyString = bodyText.getText().toString();
			bodyString = bodyString.replaceAll("&nbsp;", " ");
			if(!act.getAction_().equals("modify"))
				act.setPost_content_(bodyString + buildSig());
			else
				act.setPost_content_(bodyString);	
			new ArticlePostTask(PostActivity.this).execute(url,act.toString());

			
			
		}

		
	}
	
	private class ArticlePostTask extends AsyncTask<String, Integer, String>{

		final Context c;
		private final String result_start_tag = "<span style='color:#aaa'>&gt;</span>";
		private final String result_end_tag = "<br/>";
		private boolean keepActivity = false;
		public ArticlePostTask(Context context) {
			super();
			this.c = context;
		}
		
		@Override
		protected void onPreExecute() {
			ActivityUtil.getInstance().noticeSaying(c);
			super.onPreExecute();
		}

		@Override
		protected void onCancelled() {
			synchronized(commit_lock){
				loading = false;
			}
			ActivityUtil.getInstance().dismiss();
			super.onCancelled();
		}

		@Override
		protected void onCancelled(String result) {
			synchronized(commit_lock){
				loading = false;
			}
			ActivityUtil.getInstance().dismiss();
			super.onCancelled();
		}

		@Override
		protected String doInBackground(String... params) {
			if(params.length<2)
				return "parameter error";
			String ret = "网络错误";
			String url = params[0];
			String body = params[1];
			
			HttpPostClient c =  new HttpPostClient(url);
			String cookie = PhoneConfiguration.getInstance().getCookie();
			c.setCookie(cookie);
			
			try {
				InputStream input = null;
				HttpURLConnection conn = c.post_body(body);
				if(conn!=null)
					input = conn.getInputStream();
				else
					keepActivity = true;
				
				if(input != null)
				{
				String html = IOUtils.toString(input, "gbk");
				ret = getReplyResult(html);

				}
				else
					keepActivity = true;
			} catch (IOException e) {
				keepActivity = true;
				Log.e(LOG_TAG, Log.getStackTraceString(e));
				
			}
			return ret;
		}
		
		private String getReplyResult(String html){
			int start = html.indexOf(result_start_tag);
			if(start == -1)
				return "发帖失败";
			start += result_start_tag.length();
			int end = html.indexOf(result_end_tag, start);
			if(start == -1)
				return "发帖失败";
			return html.substring(start, end);
			
			
		}

		@Override
		protected void onPostExecute(String result) {
			String success_results[] = {" 发贴完毕 ... ", " @提醒每24小时不能超过50个"};
			if(keepActivity == false)
			{
				boolean success = false;
				for(int i=0; i< success_results.length; ++i)
				{
					if(success_results[i].equals(result)){
						success = true;
						break;
					}
				}
				if(!success)
					keepActivity = true;
			}
			
			Toast.makeText(c, result,
					Toast.LENGTH_LONG).show();
			PhoneConfiguration.getInstance().setRefreshAfterPost(true);
			ActivityUtil.getInstance().dismiss();
			if(!keepActivity)
				PostActivity.this.finish();
			synchronized(commit_lock){
				loading = false;
			}
				
			super.onPostExecute(result);
		}
		
		
	}

	@Override
	public int finishUpload(String attachments, String attachmentsCheck,
			String picUrl) {
		this.act.appendAttachments_(attachments);
		act.appendAttachments_check_(attachmentsCheck);
		String text =  bodyText.getText().toString() + "\n[img]" +picUrl + "[/img]";
		bodyText.setText( text);
		bodyText.setSelection(text.length());
		return 0;
	}

	@Override
	public void onEmotionCategorySelected(int category) {
		final FragmentManager fm =  getSupportFragmentManager();
		FragmentTransaction ft =fm.beginTransaction();   
		final Fragment categoryFragment  = getSupportFragmentManager().
				findFragmentByTag(EMOTION_CATEGORY_TAG);
		if( categoryFragment != null)
			ft.remove(categoryFragment);
		ft.commit();
		
		ft =fm.beginTransaction();
		final Fragment prev = getSupportFragmentManager().
	    		   findFragmentByTag(EMOTION_TAG);
			if (prev != null) {
	            ft.remove(prev);
	        }

		DialogFragment newFragment = null;
		switch(category){
		case CATEGORY_BASIC:
		        newFragment = new EmotionDialogFragment();
			break;
		case CATEGORY_BAOZOU:
		case CATEGORY_ALI:
		case CATEGORY_DAYANMAO:
		case CATEGORY_LUOXIAOHEI:
		case CATEGORY_ZHAIYIN:
		case CATEGORY_YANGCONGTOU:
		case CATEGORY_ACNIANG:
		case CATEGORY_BIERDE:
			Bundle args = new Bundle();
			args.putInt("index", category-1);
			newFragment = new ExtensionEmotionFragment();
			newFragment.setArguments(args);
			break;
		default:
				
		
		}
		//ft.commit();
		//ft.addToBackStack(null);

		if(newFragment != null){
			ft.commit();
			newFragment.show(fm, EMOTION_TAG);
		}

	}

}

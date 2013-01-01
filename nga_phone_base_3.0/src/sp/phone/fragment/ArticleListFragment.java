package sp.phone.fragment;

import gov.pianzong.androidnga.R;
import gov.pianzong.androidnga.activity.ArticleListActivity;
import gov.pianzong.androidnga.activity.FlexibleTopicListActivity;
import gov.pianzong.androidnga.activity.PostActivity;
import sp.phone.adapter.ArticleListAdapter;
import sp.phone.bean.PerferenceConstant;
import sp.phone.bean.ThreadData;
import sp.phone.bean.ThreadRowInfo;
import sp.phone.interfaces.OnThreadPageLoadFinishedListener;
import sp.phone.interfaces.PagerOwnner;
import sp.phone.interfaces.ResetableArticle;
import sp.phone.task.JsonThreadLoadTask;
import sp.phone.utils.ActivityUtil;
import sp.phone.utils.HttpUtil;
import sp.phone.utils.PhoneConfiguration;
import sp.phone.utils.StringUtil;
import sp.phone.utils.ThemeManager;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class ArticleListFragment extends Fragment
	implements OnThreadPageLoadFinishedListener,PerferenceConstant{
	final static private String TAG = ArticleListFragment.class.getSimpleName();
	static final int QUOTE_ORDER = 0;
	static final int REPLY_ORDER = 1;
	static final int COPY_CLIPBOARD_ORDER = 2;
	static final int SHOW_THISONLY_ORDER = 3;
	static final int SHOW_MODIFY_ORDER = 4;
	static final int SHOW_ALL = 5;
	static final int POST_COMMENT = 6;
	static final int SEARCH_POST = 7;
	static final int SEARCH_SUBJECT = 8;
	private ListView listview=null;
	private ArticleListAdapter articleAdpater;
	private JsonThreadLoadTask task;
	private int page=0;
	private int tid;
	private int pid;
	private int authorid;
	
	private Object mActionModeCallback = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		page = getArguments().getInt("page") + 1;
		tid = getArguments().getInt("id");
		pid = getArguments().getInt("pid", 0);
		authorid = getArguments().getInt("authorid", 0);
		articleAdpater = new ArticleListAdapter(this.getActivity());
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		listview = new ListView(this.getActivity());
		

		listview.setBackgroundResource(ThemeManager.getInstance().getBackgroundColor());
		listview.setDivider(null);
		
		if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
			this.registerForContextMenu(listview);
		} else {
			activeActionMode();
			listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			listview.setOnItemLongClickListener(new OnItemLongClickListener() {

				@TargetApi(11)
				@Override
				public boolean onItemLongClick(AdapterView<?> parent,
						View view, int position, long id) {
					ListView lv = (ListView)parent;
					lv.setItemChecked(position, true);
					if (mActionModeCallback != null)
					{
						getActivity().startActionMode((Callback) mActionModeCallback);
						return true;
					}
					return false;
				}

			});
			

		}
		listview.setDescendantFocusability(ListView.FOCUS_AFTER_DESCENDANTS);
		
		return listview;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {		
		listview.setAdapter(articleAdpater);
		super.onActivityCreated(savedInstanceState);
	}
	
	@TargetApi(11)
	private void activeActionMode(){
		mActionModeCallback = new ActionMode.Callback() {
			
			@Override
			public boolean onCreateActionMode(ActionMode mode, Menu menu) {
				MenuInflater inflater = mode.getMenuInflater();
				if(pid == 0){
					inflater.inflate(R.menu.articlelist_context_menu, menu);
					
				}else{
					inflater.inflate(R.menu.articlelist_context_menu_with_tid, menu);
				}
				return true;
			}

			@Override
			public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
				return false;
			}

			@Override
			public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
				onContextItemSelected(item);
				mode.finish();
				return true;
			}

			@Override
			public void onDestroyActionMode(ActionMode mode) {
				int position = listview.getCheckedItemPosition();
				listview.setItemChecked(position, false);
				
			}
			
		};
	}
	
	@Override
	public void onResume() {
		Log.d(TAG, "onResume pid="+pid+"&page="+page);
		//setHasOptionsMenu(true);
		if (PhoneConfiguration.getInstance().isRefreshAfterPost()) {
			
			PagerOwnner father = null;
			try{
				father = (PagerOwnner) getActivity();
				if (father.getCurrentPage() == page) {
					PhoneConfiguration.getInstance().setRefreshAfterPost(false);
					this.task = null;
				}
			}catch(ClassCastException e){
				Log.e(TAG,"father activity does not implements interface " 
						+ PagerOwnner.class.getName());
				
			}

			
		}
		this.loadPage();
		super.onResume();
	}
	
	
	@TargetApi(11)
	private void RunParallen(JsonThreadLoadTask task, String url){
		task.executeOnExecutor(JsonThreadLoadTask.THREAD_POOL_EXECUTOR, url);
	}
	
	private void loadPage(){
		if(null == this.task){

			Activity activity = getActivity();
			task= new JsonThreadLoadTask(activity,this);
			String url = HttpUtil.Server + 
					"/read.php?"
					+"&page="+page
					+"&lite=js&noprefix";
			if(tid !=0)
				url = url + "&tid="+ tid;
			if(pid !=0){
				url = url + "&pid="+ pid;
			}
			
			if(authorid !=0){
				url = url + "&authorid="+ authorid;
			}
			if(ActivityUtil.isGreaterThan_2_3_3())
				RunParallen(task, url);
			else
				task.execute(url);
		}else{
			ActivityUtil.getInstance().dismiss();
		}
		
		
	}
	

	
	
	

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getActivity().getMenuInflater();
		if(this.pid == 0){
			inflater.inflate(R.menu.articlelist_context_menu, menu);
			
		}else{
			inflater.inflate(R.menu.articlelist_context_menu_with_tid, menu);
		}

		
		
	}
	
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		
		Log.d(TAG, "onContextItemSelected,tid="
				+tid+ ",page="+page );
		PagerOwnner father = null;
		try{
			 father = (PagerOwnner) getActivity();
		}catch(ClassCastException e){
			Log.e(TAG,"father activity does not implements interface " 
					+ PagerOwnner.class.getName());
			return true;
		}
		
		if(father == null)
			return false;
		
		if(father.getCurrentPage() != page){
			return false;
		}
		
	
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		int position = this.listview.getCheckedItemPosition();
		if(info != null){
			position = info.position;
		}
		if(position <0 || position >= listview.getAdapter().getCount()){
			Toast.makeText(getActivity(), "无法确定楼层，选择0楼", 
					Toast.LENGTH_LONG	).show();
			position = 0;
		}
		StringBuffer postPrefix = new StringBuffer();
		String tidStr = String.valueOf(this.tid);

		
		ThreadRowInfo row = (ThreadRowInfo) listview.getItemAtPosition(position);
		if(row == null){
			Toast.makeText(getActivity(), "客户端发生未知错误", Toast.LENGTH_LONG	).show();
			return true;
		}
		String content = row.getContent();
		final String name = row.getAuthor();
		String mention=null;
		Intent intent = new Intent();
		switch(item.getItemId())
		//if( REPLY_POST_ORDER ==item.getItemId())
		{
		case R.id.quote_subject:

			final String quote_regex = "\\[quote\\]([\\s\\S])*\\[/quote\\]";
			content = content.replaceAll(quote_regex, "");
			final String postTime = row.getPostdate();
			// final String url = map.get("url");
			
			if(!content.trim().endsWith("[/url]"))
			{
				if (content.length() > 100) 
					content = content.substring(0, 99) + ".......";
				
					
			}
			content = StringUtil.unEscapeHtml(content);
			mention = name;
			if(row.getPid() == 0){
				postPrefix.append("[quote][tid=");
				postPrefix.append(tidStr);
				postPrefix.append("]");
				postPrefix.append("Topic");
			}else{
				postPrefix.append("[quote][pid=");
				postPrefix.append(row.getPid());
				postPrefix.append("]");//Topic
				postPrefix.append("Reply");
			}
			postPrefix.append("[/pid] [b]Post by ");
			postPrefix.append(name);
			postPrefix.append(" (");
			postPrefix.append(postTime);
			postPrefix.append("):[/b]\n");
			postPrefix.append(content);
			postPrefix.append("[/quote]");

			postPrefix.append("\n[@");
			postPrefix.append(name);
			postPrefix.append("]\n");
		//case R.id.r:	
			
			if(!StringUtil.isEmpty(mention))
				intent.putExtra("mention", mention);
			intent.putExtra("prefix", StringUtil.removeBrTag(postPrefix.toString()) );
			intent.putExtra("tid", tidStr);
			intent.putExtra("action", "reply");	
			intent.setClass(getActivity(), PostActivity.class);
			startActivity(intent);
			if(PhoneConfiguration.getInstance().showAnimation)
				getActivity().overridePendingTransition(R.anim.zoom_enter,
						R.anim.zoom_exit);
			break;
		case R.id.edit :
			Intent intentModify = new Intent();
			intentModify.putExtra("prefix", StringUtil.unEscapeHtml(StringUtil.removeBrTag(content)));
			intentModify.putExtra("tid", tidStr);
			String pid = String.valueOf(row.getPid());//getPid(map.get("url"));
			intentModify.putExtra("pid", pid);
			intentModify.putExtra("title",StringUtil.unEscapeHtml(row.getSubject()));
			intentModify.putExtra("action", "modify");	
			intentModify.setClass(getActivity(), PostActivity.class);
			startActivity(intentModify);
			getActivity().overridePendingTransition(R.anim.zoom_enter,
					R.anim.zoom_exit);
			break;
		case R.id.copy_to_clipboard:	
			//if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB )
			//{
				android.text.ClipboardManager  cbm = (android.text.ClipboardManager) getActivity().getSystemService(Activity.CLIPBOARD_SERVICE);
				cbm.setText(StringUtil.removeBrTag(content));
			//}else{
				//android.content.ClipboardManager  cbm = (android.content.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
				//cbm.setPrimaryClip(ClipData.newPlainText("content", content));
			//}

			Toast.makeText(getActivity(), "已经复制到剪切板", Toast.LENGTH_SHORT).show();
			break;
		case R.id.show_this_person_only:
			Intent intentThis = new Intent();
			intentThis.putExtra("tab", "1");
			intentThis.putExtra("tid",tid );
			intentThis.putExtra("authorid",row.getAuthorid() );
			
			intentThis.setClass(getActivity(), PhoneConfiguration.getInstance().articleActivityClass);
			startActivity(intentThis);
			if(PhoneConfiguration.getInstance().showAnimation)
				getActivity().overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
		
			//restNotifier.reset(0, row.getAuthorid());
			//ActivityUtil.getInstance().noticeSaying(getActivity());

			break;
		case R.id.show_whole_thread:
			ResetableArticle restNotifier = null;
			try{
				 restNotifier= (ResetableArticle)getActivity();
			}catch(ClassCastException e){
				Log.e(TAG,"father activity does not implements interface " 
						+ ResetableArticle.class.getName());
				return true;
			}
			restNotifier.reset(0, 0,row.getLou());
			ActivityUtil.getInstance().noticeSaying(getActivity());
			break;
		case R.id.post_comment:
			final String dialog_tag = "post comment";
			FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
	        Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag(dialog_tag);
	        if (prev != null) {
	            ft.remove(prev);
	        }
			DialogFragment df = new PostCommentDialogFragment();
			Bundle b = new Bundle();
			b.putInt("pid",	row.getPid());
			b.putInt("tid", this.tid);
			df.setArguments(b);
			df.show(ft, dialog_tag);
			
			break;
		case R.id.search_post:
			
			intent.putExtra("searchpost", 1);
		case R.id.search_subject:
			intent.putExtra("authorid", row.getAuthorid());
			intent.setClass(getActivity(), PhoneConfiguration.getInstance().topicActivityClass);
			startActivity(intent);
			if(PhoneConfiguration.getInstance().showAnimation)
				getActivity().overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
		
			break;
		case R.id.item_share:
			intent.setAction(Intent.ACTION_SEND);
			intent.setType("text/plain");
			String shareUrl = "http://bbs.ngacn.cc/read.php?";
			if(row.getPid() != 0){
				shareUrl = shareUrl + "pid="+row.getPid();
			}
			else
			{
				shareUrl = shareUrl + "tid="+tid;
			}
			intent.putExtra(Intent.EXTRA_TEXT, shareUrl);
			String text = getResources().getString(R.string.share);
			getActivity().startActivity(Intent.createChooser(intent, text));
			break;

			
			
		}
		return true;
	}
	@Override
	public void finishLoad(ThreadData data) {
		Log.d(TAG, "finishLoad");
		//ArticleListActivity father = (ArticleListActivity) this.getActivity();
		if(null != data){
			articleAdpater.setData(data);
			//articleAdpater.notifyDataSetChanged();
			articleAdpater.notifyDataSetInvalidated();
			
			OnThreadPageLoadFinishedListener father = null;
			try{
				father = (OnThreadPageLoadFinishedListener)getActivity();
				if(father != null)
					father.finishLoad(data);
			}catch(ClassCastException e){
				Log.e(TAG, "father activity should implements OnThreadPageLoadFinishedListener");
			}

		}
		
	}
	
	
	

}

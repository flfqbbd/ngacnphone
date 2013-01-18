package sp.phone.task;

import gov.pianzong.androidnga.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import org.apache.commons.io.IOUtils;

import sp.phone.forumoperation.HttpPostClient;
import sp.phone.utils.ActivityUtil;
import sp.phone.utils.PhoneConfiguration;
import sp.phone.utils.StringUtil;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class BookmarkTask extends AsyncTask<String, Integer, String> {
	//String url = "http://bbs.ngacn.cc/nuke.php?func=topicfavor&action=del";
	//post tidarray:3092111
	private Context context;
	private final String url = "http://bbs.ngacn.cc/nuke.php";
	
	
	
	public BookmarkTask(Context context) {
		super();
		this.context = context;
	}

	@Override
	protected String doInBackground(String... params) {

		
		String tid = params[0];
		HttpPostClient c =  new HttpPostClient(url);
		String cookie = PhoneConfiguration.getInstance().getCookie();
		c.setCookie(cookie);
		String body ="func=topicfavor&action=add&raw=1&tid="+tid;

		String ret = null;
		try {
			InputStream input = null;
			HttpURLConnection conn = c.post_body(body);
			if(conn!=null)
				input = conn.getInputStream();
			
			if(input != null)
			{
				String html = IOUtils.toString(input, "gbk");
				ret = html;//getPostResult(html);

			}
			
			}catch(IOException e){
				
			}
		return ret;
	}

	@Override
	protected void onPreExecute() {
		ActivityUtil.getInstance().noticeSaying(context);
	}

	@Override
	protected void onPostExecute(String result) {
		ActivityUtil.getInstance().dismiss();
		if(StringUtil.isEmpty(result))
			return;
		String msg = context.getResources().getString(R.string.book_mark_successfully);
		if(result.indexOf("document.createElement('iframe')") < 0){
			 msg = context.getResources().getString(R.string.already_bookmarked);
		}
		//android.R.drawable.ic_search_category_default
		if(!StringUtil.isEmpty(msg)){
			Toast.makeText(context, msg.trim(), Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onCancelled(String result) {
		this.onCancelled();
	}

	@Override
	protected void onCancelled() {
		ActivityUtil.getInstance().dismiss();
	}
	
	

}

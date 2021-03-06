package sp.phone.task;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;

import org.apache.commons.io.IOUtils;

import sp.phone.forumoperation.HttpPostClient;
import sp.phone.utils.PhoneConfiguration;
import sp.phone.utils.StringUtil;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

public class PostCommentTask extends AsyncTask<String, Integer, String> {
	private final int pid;
	private final int tid;
	private static final String postCommentUri = "http://bbs.ngacn.cc/nuke.php?func=comment";
	final private FragmentActivity fragmentActivity;
	public PostCommentTask(int pid, int tid, FragmentActivity fragmentActivity) {

		this.pid = pid;
		this.tid = tid;
		this.fragmentActivity = fragmentActivity;
	}

	@Override
	protected String doInBackground(String... params) {
		String comment = params[0];
		HttpPostClient c =  new HttpPostClient(postCommentUri);
		String cookie = PhoneConfiguration.getInstance().getCookie();
		c.setCookie(cookie);
		final String body = this.buildBody(comment);
		String ret = null;
		try {
			InputStream input = null;
			HttpURLConnection conn = c.post_body(body);
			if(conn!=null)
				input = conn.getInputStream();
			
			if(input != null)
			{
				String html = IOUtils.toString(input, "gbk");
				ret = getPostResult(html);

			}
			
			}catch(IOException e){
				
			}
		return ret;
	}
	
	private String buildBody(String comment){
		StringBuilder sb = new StringBuilder();
		sb.append("info=");

		try {
			sb.append( URLEncoder.encode(comment,"GBK"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		sb.append("&tid=");
		sb.append(tid);
		
		sb.append("&pid=");
		sb.append(pid);
		
		
		return sb.toString();
	}
	
	String getPostResult(String html)
	{
		final String startTag = "<script>window.alert(\"";
		final String EndTag = "\")</script>";
		return StringUtil.getStringBetween(html, 0, startTag, EndTag).result;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}

	@Override
	protected void onPostExecute(String result) {
		if(fragmentActivity != null)
			Toast.makeText(fragmentActivity, result, Toast.LENGTH_SHORT).show();

	}



	@Override
	protected void onCancelled() {
		// TODO Auto-generated method stub
		super.onCancelled();
	}
	
	

}

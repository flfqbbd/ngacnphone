package sp.phone.task;

import gov.pianzong.androidnga.R;
import sp.phone.fragment.ProgressDialogFragment;
import sp.phone.utils.HttpUtil;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;

public class TudouVideoLoadTask extends AsyncTask<String, Integer, String> {

	final FragmentActivity fa ;
	static final String dialogTag = "load_tudou";
	public TudouVideoLoadTask(FragmentActivity fa) {
		super();
		this.fa = fa;
	}
	private boolean startIntent = true;
	@Override
	protected void onPreExecute() {
		//create progress view
		 ProgressDialogFragment pd = new  ProgressDialogFragment();
		 
		Bundle args = new Bundle();
		final String content = fa.getResources().getString(R.string.load_tudou_video);
		args.putString("content", content);
		pd.setArguments(args );
		pd.show(fa.getSupportFragmentManager(), dialogTag);
		super.onPreExecute();
	}

	@Override
	protected void onPostExecute(String result) {
		if(!startIntent)
			return;
		
		if(result != null){
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse(result));
			fa.startActivity(intent);
		}

		this.onCancelled();

		super.onPostExecute(result);
	}

	@Override
	protected void onCancelled(String result) {
		
		this.onCancelled();
	}

	@Override
	protected void onCancelled() {
		FragmentManager fm = fa.getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();

        Fragment prev = fm.findFragmentByTag(dialogTag);
        if (prev != null) {
            ft.remove(prev);
            
        }
        try
        {
        	ft.commit();
        }catch(Exception e){
        	
        }
	}

	@Override
	protected String doInBackground(String... params) {
		if(params.length ==0)
		{
			return null;
		}
		final String uri = "http://vr.tudou.com/v2proxy/dispatch?ip=122.143.3.10&type=9&base=0&pw=&code="
				+ params[0];
		final String jsString = HttpUtil.getHtml(uri, null);
		try{
			JSONObject o = JSONObject.parseObject(jsString);
			if(o != null){
				Object src =  o.get("src");
				if(src != null && src instanceof String){
					return (String) src;
				}
			}
		}catch(Exception e){
			Log.e(this.getClass().getSimpleName(), "can not load tudou video"+ params[0]);
			return null;
		}
		return null;
	}

}

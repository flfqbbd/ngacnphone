package gov.pianzong.holocnbeta;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import gov.pianzong.fragment.NewsContentFragment;
import gov.pianzong.holocnbeta.R;
import gov.pianzong.util.AppConstants;

/**
 * Created by Administrator on 13-7-1.
 */


public class NewsContentActivity extends Activity {
    //private int articleId = 0;
    //private WebView wv = null;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // articleId = getIntent().getIntExtra(AppConstants.ARTICLE_ID,0);
        requestWindowFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.new_content_hoder);
        if(savedInstanceState == null)
        {
            Bundle args = new Bundle(getIntent().getExtras());
            Fragment f = new NewsContentFragment();
            f.setArguments(args);
            getFragmentManager().beginTransaction().replace(R.id.holder,f).commit();
        }

        /*wv = (WebView) LayoutInflater.from(this).inflate(R.layout.news_content,null);
        setContentView(wv);
        wv.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        wv.loadUrl(AppConstants.getNewsContentUrl(articleId));*/
    }


}
package gov.pianzong.holocnbeta;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import gov.pianzong.util.AppConstants;

/**
 * Created by Administrator on 13-7-1.
 */


public class NewsContentActivity extends Activity {
    private int articleId = 0;
    private WebView wv = null;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        articleId = getIntent().getIntExtra(AppConstants.ARTICLE_ID,0);
        wv = (WebView) LayoutInflater.from(this).inflate(R.layout.news_content,null);
        setContentView(wv);
        wv.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        wv.loadUrl(AppConstants.getNewsContentUrl(articleId));
    }
}
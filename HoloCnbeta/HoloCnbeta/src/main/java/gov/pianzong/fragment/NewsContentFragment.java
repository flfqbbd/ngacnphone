package gov.pianzong.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import gov.pianzong.holocnbeta.R;
import gov.pianzong.util.AppConstants;

/**
 * Created by GDB437 on 7/1/13.
 */
public class NewsContentFragment extends Fragment {
    private WebView wv;
    private  int articleId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        wv = (WebView) inflater.inflate(R.layout.news_content,container,false);
        return  wv;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        articleId = getArguments().getInt(AppConstants.ARTICLE_ID,0);


        wv.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        wv.loadUrl(AppConstants.getNewsContentUrl(articleId));
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.news_conetnt_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean ret  = true;
        switch (item.getItemId()){
            case R.id.action_comment:
                break;
            default:
                ret = false;
        }
        return ret;
    }
}

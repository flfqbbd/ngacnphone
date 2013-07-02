package gov.pianzong.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import gov.pianzong.holocnbeta.CommentActivity;
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

        if(articleId != 0)
        {
            wv.setWebViewClient(new WebViewClient(){
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }
            });

            wv.setWebChromeClient(new WebChromeClient(){
                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    getActivity().setProgress(newProgress*100);
                }
            });
            wv.loadUrl(AppConstants.getNewsContentUrl(articleId));
            this.setHasOptionsMenu(true);
        }
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
                Intent intent = new Intent();
                intent.putExtra(AppConstants.ARTICLE_ID,articleId);
                intent.setClass(getActivity(), CommentActivity.class);
                startActivity(intent);
                break;
            default:
                ret = false;
        }
        return ret;
    }
}

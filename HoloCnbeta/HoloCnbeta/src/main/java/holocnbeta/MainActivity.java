package gov.pianzong.holocnbeta;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import gov.pianzong.bean.NewsInfo;
import gov.pianzong.holocnbeta.R;
import gov.pianzong.interfaces.NewsClickedListener;
import gov.pianzong.util.AppConstants;

public class MainActivity extends Activity implements NewsClickedListener {


    boolean dualScreen = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onClick(NewsInfo info) {
        if(dualScreen){
            setDetailFragment(info);
        }
        else
        {
            openNewActivity(info);
        }
    }


    private void openNewActivity(NewsInfo info) {
        Intent intent = new Intent();
        intent.setClass(this,NewsContentActivity.class);
        intent.putExtra(AppConstants.ARTICLE_ID,info.getArticleID());
        intent.putExtra(AppConstants.COMMENT_COUNT,info.getCmtnum());
        this.startActivity(intent);

    }
    private void setDetailFragment(NewsInfo info) {
    }


}

package gov.pianzong.holocnbeta;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;

import gov.pianzong.adapter.HeadlinePagerAdapter;
import gov.pianzong.adapter.NewsListAdapter;
import gov.pianzong.bean.NewsInfo;
import gov.pianzong.interfaces.NewsClickedListener;
import gov.pianzong.task.NewsListLoadTask;
import gov.pianzong.util.AppConstants;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher;

public class MainActivity extends Activity implements NewsClickedListener {

    PullToRefreshAttacher helper;
    boolean dualScreen = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        helper = new PullToRefreshAttacher(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        final String category[] = getResources().getStringArray(R.array.category_list);
        pager.setAdapter(new HeadlinePagerAdapter(category,getFragmentManager()));
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

    @Override
    public void registRefreshableView(View view,PullToRefreshAttacher.OnRefreshListener listener) {

        helper.setRefreshableView(view,listener);
    }

    @Override
    public void startLoad() {
        if(!helper.isRefreshing())
            helper.setRefreshing(true);
    }

    @Override
    public void loadFinish() {
        helper.setRefreshComplete();
    }


    private void openNewActivity(NewsInfo info) {
        Intent intent = new Intent();
        intent.setClass(this, gov.pianzong.holocnbeta.NewsContentActivity.class);
        intent.putExtra(AppConstants.ARTICLE_ID,info.getArticleID());
        intent.putExtra(AppConstants.COMMENT_COUNT,info.getCmtnum());
        this.startActivity(intent);

    }
    private void setDetailFragment(NewsInfo info) {
    }


}

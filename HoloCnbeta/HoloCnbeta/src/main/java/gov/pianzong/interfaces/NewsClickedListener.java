package gov.pianzong.interfaces;


import android.view.View;

import gov.pianzong.bean.NewsInfo;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher;

/**
 * Created by Administrator on 13-7-1.
 */
public interface NewsClickedListener {
    public  void onClick(NewsInfo info);
    public  void registRefreshableView(View v,PullToRefreshAttacher.OnRefreshListener listener);
    public void startLoad();
    public  void  loadFinish();
}

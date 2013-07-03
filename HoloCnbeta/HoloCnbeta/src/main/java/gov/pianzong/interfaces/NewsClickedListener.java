package gov.pianzong.interfaces;


import android.widget.ListView;

import gov.pianzong.bean.NewsInfo;

/**
 * Created by Administrator on 13-7-1.
 */
public interface NewsClickedListener {
    public  void onClick(NewsInfo info);
    public  void registRefreshableView(ListView lv);
    public void startLoad();
    public  void  loadFinish();
}

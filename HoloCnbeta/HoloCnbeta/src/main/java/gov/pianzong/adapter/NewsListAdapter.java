package gov.pianzong.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import gov.pianzong.bean.NewsInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 13-6-30.
 */
public class NewsListAdapter extends BaseAdapter {

    private List<NewsInfo> newsList = new ArrayList<NewsInfo>();
    @Override
    public int getCount() {
        if(newsList == null)
            return 0;
        return newsList.size();
    }

    @Override
    public Object getItem(int i) {
        if(newsList == null || i >= newsList.size())
            return null;
        return newsList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return newsList.get(i).getArticleID();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        TextView tv = null;
        if(view != null)
            tv = (TextView) view;
        else
            tv = new TextView(viewGroup.getContext());
         tv.setText(newsList.get(i).getTitle());
        return tv;
    }

    public void clear()
    {
        newsList.clear();
    }

    public  void append(List<NewsInfo> list){

        newsList.addAll(list);
    }
}

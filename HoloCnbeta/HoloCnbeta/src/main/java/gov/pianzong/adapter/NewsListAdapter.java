package gov.pianzong.adapter;

import gov.pianzong.holocnbeta.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import gov.pianzong.bean.NewsInfo;

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

    private  static class  ViewHolder{
        public  TextView titleView;
    };
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view != null){
            holder = (ViewHolder) view.getTag();
        }
        else
        {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.news_item,viewGroup,false);
            holder = new ViewHolder();
            holder.titleView = (TextView)view.findViewById(R.id.title_text_view);
            view.setTag(holder);
        }
        holder.titleView.setText(newsList.get(i).getTitle());;
        return view;
    }

    public void clear()
    {
        newsList.clear();
    }

    public  void append(List<NewsInfo> list){

        newsList.addAll(list);
    }
}

package gov.pianzong.adapter;

import gov.pianzong.holocnbeta.R;

import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gov.pianzong.bean.NewsInfo;
import gov.pianzong.interfaces.NewsClickedListener;
import gov.pianzong.task.LogoLoadTask;
import gov.pianzong.task.NewsListLoadTask;
import gov.pianzong.util.StringUtil;

/**
 * Created by Administrator on 13-6-30.
 */
public class NewsListAdapter extends BaseAdapter {

    private List<NewsInfo> newsList = new ArrayList<NewsInfo>();
    private boolean isLoading = false;
    final private NewsClickedListener callBack;

    public NewsListAdapter(NewsClickedListener callBack) {
        this.callBack = callBack;
    }

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

    public   static class  ViewHolder{
        public  TextView titleView;
        public  TextView dateView;
        public  TextView commentView;
        public ImageView logoImage;
        public  int position;
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
            holder.dateView = (TextView)view.findViewById(R.id.date_text);
            holder.commentView = (TextView)view.findViewById(R.id.comment_text_view);
            holder.logoImage = (ImageView)view.findViewById(R.id.logo_image_view);
            view.setTag(holder);
        }
        holder.position = i;
        NewsInfo info = newsList.get(i);
        holder.titleView.setText(info.getTitle());
        holder.commentView.setText(String.valueOf(info.getCmtnum()));
        holder.dateView.setText(timeGap(viewGroup.getContext().getResources(),info.getPubtime()));

        asyncLoadLogo(info,holder);

        if(i+1 == getCount() && !isLoading){
            isLoading = true;
            new NewsListLoadTask(this,callBack).executeOnExecutor(NewsListLoadTask.THREAD_POOL_EXECUTOR,info.getArticleID());
        }
        return view;
    }

    private  void asyncLoadLogo(NewsInfo info, ViewHolder holder){
        String url = info.getTopicLogo();
        if(StringUtil.isEmpty(url) || !url.startsWith("http")){
            url = info.getTheme();
        }

        if(StringUtil.isEmpty(url) || !url.startsWith("http")){
            return;
        }
        new LogoLoadTask(holder).executeOnExecutor(LogoLoadTask.THREAD_POOL_EXECUTOR,url);

    }
    private static final int SEC_PER_MIN = 60;
    private static final int SEC_PER_HOUR = 60*SEC_PER_MIN;
    private static final int SEC_PER_DAY = 24*SEC_PER_HOUR;
    private static final int SEC_PER_MONTH = 30*SEC_PER_DAY;
    private  String timeGap(Resources res,String timeString){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long gap = 0;
        try {
            Date date = sdf.parse(timeString);
            gap = (System.currentTimeMillis() - date.getTime() )/1000;
        } catch (ParseException e) {
            Log.e(getClass().getSimpleName(),"invalid date time format" + timeString);
        }
        String ret ;
        ret = res.getString(R.string.minutes_before,gap/SEC_PER_MIN);
        if(gap/SEC_PER_HOUR >0)
        {
           ret = res.getString(R.string.hours_before, gap/SEC_PER_HOUR);
        }
        if(gap / SEC_PER_DAY >0){
            ret = res.getString(R.string.days_before, gap/SEC_PER_DAY);
        }

        if(gap / SEC_PER_MONTH >0){
            ret = res.getString(R.string.months_before, gap/SEC_PER_MONTH);
        }
        return ret;
    }
    public void clear()
    {
        newsList.clear();
    }

    public  void append(List<NewsInfo> list){

        newsList.addAll(list);
        isLoading = false;
    }
}

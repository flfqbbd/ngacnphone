package gov.pianzong.task;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import gov.pianzong.adapter.NewsListAdapter;
import gov.pianzong.bean.NewsInfo;
import gov.pianzong.util.AppConstants;
import gov.pianzong.util.HtppUtil;
import gov.pianzong.util.StringUtil;

/**
 * Created by Administrator on 13-6-30.
 */
public class NewsListLoadTask extends AsyncTask<Integer,Integer,List<NewsInfo>> {
    private NewsListAdapter adapter;
    private int startId = 0;
    public NewsListLoadTask(NewsListAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    protected List<NewsInfo> doInBackground(Integer... params) {
        startId = params[0];
        final  String url = AppConstants.getNewslistUrl(startId);
        String html = HtppUtil.getHtml(url,null,null,0);
       //html = html.replaceAll("\"ArticleID\"","\"articleID\"");
        List<NewsInfo> list = new ArrayList<NewsInfo>();
        if(StringUtil.isEmpty(html))
            return list;
        try {
            JSONArray ja= new JSONArray(html);
            final int length = ja.length();
            for(int i = 0; i< length; ++i){
                JSONObject o = ja.getJSONObject(i);
                NewsInfo info = new NewsInfo();
                info.setTitle( o.getString("title"));
                info.setPubtime(o.getString("pubtime"));
                info.setArticleID(o.getInt("ArticleID"));
                info.setCmtClosed(o.getInt("cmtClosed"));
                info.setCmtnum(o.getInt("cmtnum"));
                info.setSummary(o.getString("summary"));
                info.setTopicLogo(o.getString("topicLogo"));
                info.setTheme(o.getString("theme"));
                list.add(info);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

       // list = com.alibaba.fastjson.JSON.parseArray(html,NewsInfo.class);
        return list;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(List<NewsInfo> list) {
        adapter.append(list);
        if(startId == 0)
        {
            adapter.notifyDataSetInvalidated();
        }
        else
        {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }
}

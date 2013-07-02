package gov.pianzong.task;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import gov.pianzong.adapter.CommentListAdapter;
import gov.pianzong.bean.CommentInfo;
import gov.pianzong.util.AppConstants;
import gov.pianzong.util.HtppUtil;
import gov.pianzong.util.StringUtil;

/**
 * Created by Administrator on 13-7-2.
 */
public class CommentLoadTask extends AsyncTask<Integer,Integer, List<CommentInfo>> {

    private final  CommentListAdapter adapterer;

    public CommentLoadTask(CommentListAdapter adapterer) {
        this.adapterer = adapterer;
    }

    @Override
    protected List<CommentInfo> doInBackground(Integer... articleID) {
        final String url = AppConstants.getCommentUrl(articleID[0]);
        String html = HtppUtil.getHtml(url,null,null,0);
        JSONArray ja= null;
        List<CommentInfo> ret = new ArrayList<CommentInfo>();
        if(StringUtil.isEmpty(html))
            return ret;
        try {
            ja = new JSONArray(html);
            final int length = ja.length();
            for(int i = 0; i< length; ++i){
                JSONObject o = ja.getJSONObject(i);
                CommentInfo info = new CommentInfo();
                info.setName(o.getString("name"));
                info.setDate(o.getString("date"));
                info.setComment(o.getString("comment"));
                info.setTid(o.getInt("tid"));
                info.setAgainst(o.getInt("against"));
                info.setSupport(o.getInt("support"));
                ret.add(info);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ret;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(List<CommentInfo> commentInfos) {
        if(commentInfos == null)
            return;

        adapterer.setList(commentInfos);
        adapterer.notifyDataSetInvalidated();

    }
}

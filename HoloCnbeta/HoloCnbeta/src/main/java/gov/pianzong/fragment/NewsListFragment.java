package gov.pianzong.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import gov.pianzong.adapter.NewsListAdapter;
import gov.pianzong.bean.NewsInfo;
import gov.pianzong.holocnbeta.R;
import gov.pianzong.interfaces.NewsClickedListener;
import gov.pianzong.task.NewsListLoadTask;

/**
 * Created by Administrator on 13-6-30.
 */
public class NewsListFragment extends Fragment {
    private ListView lv;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        lv = (ListView) inflater.inflate(R.layout.news_list,container,false);
        return lv;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        try{
            final  NewsClickedListener listener = (NewsClickedListener) getActivity();
            lv.setOnItemClickListener( new NewsItemClickedListener(listener));
            lv.setAdapter(new NewsListAdapter(listener));
            listener.registRefreshableView(lv);
            new NewsListLoadTask((NewsListAdapter) lv.getAdapter(),listener).executeOnExecutor(NewsListLoadTask.THREAD_POOL_EXECUTOR,0);
        }catch (ClassCastException e){
            StringBuilder sb = new StringBuilder();
            sb.append(getClass().getSimpleName()).append(" should implements ").append(NewsClickedListener.class.getSimpleName());
            Log.e(getClass().getSimpleName(),sb.toString());
        }

    }

    static class NewsItemClickedListener implements AdapterView.OnItemClickListener{
        final  private  NewsClickedListener listener;

        NewsItemClickedListener(NewsClickedListener listener) {
            this.listener = listener;
        }


        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
           NewsInfo info = (NewsInfo) adapterView.getItemAtPosition(i);
           if(listener != null)
                listener.onClick(info);
        }
    }
}

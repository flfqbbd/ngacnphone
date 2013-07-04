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
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher;

/**
 * Created by Administrator on 13-6-30,13-6-30,${PROJECT_NAME}

 */
public class NewsListFragment extends Fragment implements PullToRefreshAttacher.OnRefreshListener{
    private ListView lv;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        lv = (ListView) inflater.inflate(R.layout.news_list,container,false);
        return lv;
    }

    @Override
    public void onRefreshStarted(View view) {
        refresh();
    }
    private  void refresh(){
        try{
            final  NewsClickedListener listener = (NewsClickedListener) getActivity();
            new NewsListLoadTask((NewsListAdapter) lv.getAdapter(),listener).executeOnExecutor(NewsListLoadTask.THREAD_POOL_EXECUTOR,0);
        }catch (ClassCastException e){
            StringBuilder sb = new StringBuilder();
            sb.append(getClass().getSimpleName()).append(" should implements ").append(NewsClickedListener.class.getSimpleName());
            Log.e(getClass().getSimpleName(),sb.toString());
        }

    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        try{
            final  NewsClickedListener listener = (NewsClickedListener) getActivity();

            if (listener != null) {
                lv.setOnItemClickListener( new NewsItemClickedListener(listener));
                lv.setAdapter(new NewsListAdapter(listener));
                listener.registRefreshableView(lv,this);
            }

            refresh();
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

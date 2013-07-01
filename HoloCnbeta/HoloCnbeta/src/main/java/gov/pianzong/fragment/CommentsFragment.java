package gov.pianzong.fragment;

import gov.pianzong.adapter.CommentListAdapter;
import gov.pianzong.holocnbeta.R;
import gov.pianzong.task.CommentLoadTask;
import gov.pianzong.util.AppConstants;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Created by Administrator on 13-7-2.
 */
public class CommentsFragment extends Fragment {

    private ListView lv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        lv = (ListView) inflater.inflate(R.layout.news_list,container,false);
        return lv;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        lv.setAdapter(new CommentListAdapter());
        new CommentLoadTask((CommentListAdapter) lv.getAdapter())
                .executeOnExecutor(CommentLoadTask.THREAD_POOL_EXECUTOR,
                        getArguments().getInt(AppConstants.ARTICLE_ID,0));
    }
}

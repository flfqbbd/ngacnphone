package gov.pianzong.holocnbeta;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.Window;

import gov.pianzong.fragment.CommentsFragment;
import gov.pianzong.fragment.NewsContentFragment;

/**
 * Created by Administrator on 13-7-1.
 */
public class CommentActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_content_hoder);
        if(savedInstanceState == null)
        {
            Bundle args = new Bundle(getIntent().getExtras());
            Fragment f = new CommentsFragment();
            f.setArguments(args);
            getFragmentManager().beginTransaction().replace(R.id.holder,f).commit();
        }
    }
}
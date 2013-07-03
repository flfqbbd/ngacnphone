package gov.pianzong.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by GDB437 on 7/2/13.
 */
public class HotCommentFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(container == null){
            return  null;
        }
        return new TextView(container.getContext());
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ((TextView)view).setText("under construction");
    }
}

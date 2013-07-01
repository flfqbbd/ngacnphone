package gov.pianzong.fragment;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import gov.pianzong.adapter.HeadlinePagerAdapter;
import gov.pianzong.holocnbeta.R;

/**
 * Created by Administrator on 13-6-29.
 */

public class HeadLineFragment extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.headline_viewpager,container,false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ViewPager pager = (ViewPager) view.findViewById(R.id.pager);
        pager.setAdapter(new HeadlinePagerAdapter(getFragmentManager()));
    }
}

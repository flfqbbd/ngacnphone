package gov.pianzong.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;
import gov.pianzong.fragment.NewsListFragment;

/**
 * Created by Administrator on 13-6-30.
 */
public class HeadlinePagerAdapter extends FragmentPagerAdapter {

    public HeadlinePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        return new NewsListFragment();
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return String.valueOf(position+1);
    }
}

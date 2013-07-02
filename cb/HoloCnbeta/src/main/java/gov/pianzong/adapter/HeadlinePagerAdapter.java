package gov.pianzong.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import gov.pianzong.fragment.HotCommentFragment;
import gov.pianzong.fragment.NewsListFragment;

/**
 * Created by Administrator on 13-6-30.
 */
public class HeadlinePagerAdapter extends FragmentPagerAdapter {
    private  final  String category[];
    public HeadlinePagerAdapter(String category[],FragmentManager fm) {
        super(fm);
        this.category =category;

    }

    @Override
    public Fragment getItem(int i) {
        Fragment f = null;
        switch (i)
        {
            case 0:
                f = new NewsListFragment();
                break;
            default:
                f = new HotCommentFragment();

        }
        return f;
    }

    @Override
    public int getCount() {
        return category == null ?0:category.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return category[position];
    }
}

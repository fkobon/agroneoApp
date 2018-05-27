package com.agroneo.app.discuss.forums;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.agroneo.app.discuss.forums.ForumsDb.Forums;

import java.util.ArrayList;
import java.util.List;

public class ForumsPager extends FragmentPagerAdapter {

    private List<ForumsFragment> fragments = new ArrayList<>();

    public ForumsPager(FragmentManager fm) {
        super(fm);
        ForumsFragment discuss = new ForumsFragment();
        discuss.setUrl("/forum", this);
        fragments.add(discuss);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragments.get(position).getTitle();
    }

    public void childrens(List<Forums> childrens) {
        for (Forums children : childrens) {
            ForumsFragment discuss = new ForumsFragment();
            discuss.setUrl(children.url, this);
            discuss.setTitle(children.title);
            fragments.add(discuss);
        }
        notifyDataSetChanged();
    }

}
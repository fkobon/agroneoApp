package com.agroneo.app.discuss.forums;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.agroneo.app.discuss.forums.ForumsDb.Forums;

import java.util.LinkedHashMap;
import java.util.List;

public class ForumsPager extends FragmentPagerAdapter {

    private LinkedHashMap<String, ForumsFragment> fragments = new LinkedHashMap<>();

    public ForumsPager(FragmentManager fm) {
        super(fm);
        ForumsFragment discuss = new ForumsFragment();
        discuss.setUrl("/forum", this);
        fragments.put("ROOT", discuss);
    }

    @Override
    public ForumsFragment getItem(int position) {
        return fragments.get(fragments.keySet().toArray(new String[0])[position]);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return getItem(position).getTitle();
    }

    public void childrens(List<Forums> childrens) {
        for (Forums children : childrens) {
            if (!fragments.containsKey(children._id)) {
                ForumsFragment discuss = new ForumsFragment();
                discuss.setUrl(children.url, this);
                discuss.setTitle(children.title);
                fragments.put(children._id, discuss);
            }
        }
        notifyDataSetChanged();

    }

}
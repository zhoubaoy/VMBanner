package com.test.net.banner;

import android.app.Activity;
import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by steven on 2018/5/14.
 */

public class BannerViewAdapter extends PagerAdapter
{
    private List<View> listBean;

    public BannerViewAdapter(List<View> list){
        if (list == null){
            list = new ArrayList<>();
        }
        this.listBean = list;
    }

    public void setDataList(List<View> list){
        if (list != null && list.size() > 0){
            this.listBean = list;
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position)
    {
        View view = listBean.get(position);
        container.addView(view);
        return view;
    }

    @Override
    public int getItemPosition(Object object)
    {
        return POSITION_NONE;
    }

    @Override
    public int getCount()
    {
        return listBean.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object)
    {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object)
    {
        return view == object;
    }

}

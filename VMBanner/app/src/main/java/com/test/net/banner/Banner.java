package com.test.net.banner;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.test.net.MVideoView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by steven on 2018/5/14.
 */

public class Banner extends RelativeLayout
{
    private ViewPager viewPager;
    private final int UPTATE_VIEWPAGER = 100;
    //图片默认时间间隔
    private int imgDelyed = 2000;
    //每个位置默认时间间隔，因为有视频的原因
    private int delyedTime = 2000;
    //默认显示位置
    private int autoCurrIndex = 0;
    //是否自动播放
    private boolean isAutoPlay = false;
    private Time time;
    private List<BannerModel> bannerModels;
    private List<String> list;
    private List<View> views;
    private BannerViewAdapter mAdapter;

    public Banner(Context context)
    {
        super(context);
        init();
    }

    public Banner(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public Banner(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Banner(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        time = new Time();
        viewPager = new ViewPager(getContext());
        LinearLayout.LayoutParams vp_param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        viewPager.setLayoutParams(vp_param);
        this.addView(viewPager);
    }


    public void setDataList(List<String> dataList){
        if (dataList == null){
            dataList = new ArrayList<>();
        }
        //用于显示的数组
        if (views == null)
        {
            views = new ArrayList<>();
        }else {
            views.clear();
        }

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        RequestOptions options = new RequestOptions();
        options.centerCrop();
        //数据大于一条，才可以循环
        if (dataList.size() > 1)
        {
            autoCurrIndex = 1;
            //循环数组，将首位各加一条数据
            for (int i = 0; i < dataList.size() + 2; i++)
            {
                String url;
                if (i == 0)
                {
                    url = dataList.get(dataList.size() - 1);
                } else if (i == dataList.size() + 1)
                {
                    url = dataList.get(0);
                } else
                {
                    url = dataList.get(i - 1);
                }

                if (MimeTypeMap.getFileExtensionFromUrl(url).equals("mp4"))
                {
                    MVideoView videoView = new MVideoView(getContext());
                    videoView.setLayoutParams(lp);
                    videoView.setVideoURI(Uri.parse(url));
                    videoView.start();
                    views.add(videoView);
                } else
                {
                    ImageView imageView = new ImageView(getContext());
                    imageView.setLayoutParams(lp);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    Glide.with(getContext()).load(url).apply(options).into(imageView);
                    views.add(imageView);
                }
            }
        }else if (dataList.size() == 1){
            autoCurrIndex = 0;
            String url = dataList.get(0);
            if (MimeTypeMap.getFileExtensionFromUrl(url).equals("mp4"))
            {
                MVideoView videoView = new MVideoView(getContext());
                videoView.setLayoutParams(lp);
                videoView.setVideoURI(Uri.parse(url));
                videoView.start();
                //监听视频播放完的代码
                videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mPlayer) {
                        mPlayer.start();
                        mPlayer.setLooping(true);
                    }
                });
                views.add(videoView);
            } else
            {
                ImageView imageView = new ImageView(getContext());
                imageView.setLayoutParams(lp);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                Glide.with(getContext()).load(url).apply(options).into(imageView);
                views.add(imageView);
            }
        }
    }


    public void setImgDelyed(int imgDelyed){
        this.imgDelyed = imgDelyed;
    }

    public void startBanner()
    {
        mAdapter = new BannerViewAdapter(views);
        viewPager.setAdapter(mAdapter);
        viewPager.setOffscreenPageLimit(1);
        viewPager.setCurrentItem(autoCurrIndex);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {

            }

            @Override
            public void onPageSelected(int position)
            {
                Log.d("TAG","position:"+position);
                //当前位置
                autoCurrIndex = position;
                getDelayedTime(position);
            }

            @Override
            public void onPageScrollStateChanged(int state)
            {
                Log.d("TAG",""+state);

                //移除自动计时
                mHandler.removeCallbacks(runnable);

                //ViewPager跳转
                int pageIndex = autoCurrIndex;
                if(autoCurrIndex == 0){
                    pageIndex = views.size()-2;
                }else if(autoCurrIndex == views.size() - 1){
                    pageIndex = 1;
                }
                if (pageIndex != autoCurrIndex) {
                    //无滑动动画，直接跳转
                    viewPager.setCurrentItem(pageIndex, false);
                }

                //停止滑动时，重新自动倒计时
                if (state == 0 && isAutoPlay && views.size() > 1){
                    View view1 = views.get(pageIndex);
                    if (view1 instanceof VideoView){
                        final VideoView videoView = (VideoView) view1;
                        int current = videoView.getCurrentPosition();
                        int duration = videoView.getDuration();
                        delyedTime = duration - current;
                        //某些时候，某些视频，获取的时间无效，就延时10秒，重新获取
                        if (delyedTime <= 0){
                            time.getDelyedTime(videoView,runnable);
                            mHandler.postDelayed(time,imgDelyed);
                        }else {
                            mHandler.postDelayed(runnable,delyedTime);
                        }
                    }else {
                        delyedTime = imgDelyed;
                        mHandler.postDelayed(runnable,delyedTime);
                    }
                    Log.d("TAG",""+pageIndex+"--"+autoCurrIndex);
                }
            }
        });
    }
    //开启自动循环
    public void startAutoPlay(){
        isAutoPlay = true;
        if (views.size() > 1){
            getDelayedTime(autoCurrIndex);
            if (delyedTime <= 0){
                mHandler.postDelayed(time,imgDelyed);
            }else {
                mHandler.postDelayed(runnable,delyedTime);
            }
        }
    }

    /**
     * 发消息，进行循环
     */
    private Runnable runnable = new Runnable()
    {
        @Override
        public void run()
        {
            mHandler.sendEmptyMessage(UPTATE_VIEWPAGER);
        }
    };

    /**
     * 这个类，恩，获取视频长度，以及已经播放的时间
     */
    private class Time implements Runnable{

        private VideoView videoView;
        private Runnable runnable;

        public void getDelyedTime(VideoView videoView,Runnable runnable){
            this.videoView = videoView;
            this.runnable = runnable;
        }
        @Override
        public void run()
        {
            int current = videoView.getCurrentPosition();
            int duration = videoView.getDuration();
            int delyedTime = duration - current;
            mHandler.postDelayed(runnable,delyedTime);
        }
    }

    //接受消息实现轮播
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPTATE_VIEWPAGER:
                    viewPager.setCurrentItem(autoCurrIndex+1);
                    break;
            }
        }
    };

    private class BannerModel{
        public String url;
        public int playTime;
        public int type = 0;
    }

    /**
     * 获取delyedTime
     * @param position 当前位置
     */
    private void getDelayedTime(int position){
        View view1 = views.get(position);
        if (view1 instanceof VideoView){
            VideoView videoView = (VideoView) view1;
            videoView.start();
            videoView.seekTo(0);
            delyedTime = videoView.getDuration();
            time.getDelyedTime(videoView,runnable);
        }else {
            delyedTime = imgDelyed;
        }
    }

    public void dataChange(List<String> list){
        if (list != null && list.size()>0)
        {
            //改变资源时要重新开启循环，否则会把视频的时长赋给图片，或者相反
            //因为delyedTime也要改变，所以要重新获取delyedTime
            mHandler.removeCallbacks(runnable);
            setDataList(list);
            mAdapter.setDataList(views);
            mAdapter.notifyDataSetChanged();
            viewPager.setCurrentItem(autoCurrIndex,false);
            //开启循环
            if (isAutoPlay && views.size() > 1){
                getDelayedTime(autoCurrIndex);
                if (delyedTime <= 0){
                    mHandler.postDelayed(time,imgDelyed);
                }else {
                    mHandler.postDelayed(runnable,delyedTime);
                }
            }
        }
    }

    public void destroy(){
        mHandler.removeCallbacksAndMessages(null);
        mHandler = null;
        time = null;
        runnable = null;
        views.clear();
        views = null;
        viewPager = null;
        mAdapter = null;
    }
}

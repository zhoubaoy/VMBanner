package com.test.net;

import android.app.Application;
import android.content.Context;

import com.danikula.videocache.HttpProxyCacheServer;

/**
 * Created by steven on 2018/5/15.
 */

public class MApplication extends Application
{
    private HttpProxyCacheServer proxy;

    @Override
    public void onCreate()
    {
        super.onCreate();
    }

    public static HttpProxyCacheServer getProxy(Context context) {
        MApplication app = (MApplication) context.getApplicationContext();
        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
    }

    private HttpProxyCacheServer newProxy() {
        HttpProxyCacheServer proxy = new HttpProxyCacheServer.Builder(this)
                .fileNameGenerator(new MyFileNameGenerator())
                .build();
        return proxy;
    }
}

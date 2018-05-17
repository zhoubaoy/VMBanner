package com.test.net.banner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.danikula.videocache.HttpProxyCacheServer;
import com.test.net.*;
import com.test.net.R;

import java.util.ArrayList;
import java.util.List;

public class BannerActivity extends AppCompatActivity
{

    private Banner banner;
    private List<String> list;
    private Button bt;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(com.test.net.R.layout.activity_banner);

        banner = (Banner) findViewById(R.id.banner);
        bt = (Button) findViewById(R.id.bt);

        initData();
        initView();
    }

    private void initData(){
        HttpProxyCacheServer proxy = MApplication.getProxy(getApplicationContext());
        String proxyUrl = proxy.getProxyUrl("http://fs.mv.web.kugou.com/201805151034/301b4249052e4f77917f02c1903e3370/G131/M06/0D/00/ww0DAFr5qtqACRUoAh-sVLABkV8377.mp4");
        String proxyUrl2 = proxy.getProxyUrl("http://fs.mv.web.kugou.com/201805151530/498716f6f332829687bbf077e252a083/G133/M05/1F/19/xQ0DAFrwJs6AHgs6AbSCfVQb4IQ631.mp4");

        list = new ArrayList<>();
        list.add(proxyUrl);
        list.add(proxyUrl2);
        list.add("http://img2.imgtn.bdimg.com/it/u=3817131034,1038857558&fm=27&gp=0.jpg");
        list.add("http://img1.imgtn.bdimg.com/it/u=4194723123,4160931506&fm=200&gp=0.jpg");
        list.add("http://img5.imgtn.bdimg.com/it/u=1812408136,1922560783&fm=27&gp=0.jpg");
    }

    private void initView(){
        banner.setDataList(list);
        banner.setImgDelyed(5000);
        banner.startBanner();
        banner.startAutoPlay();
    }

    public void onClick(View view){

        if (view.getId() == R.id.bt){
            HttpProxyCacheServer proxy = MApplication.getProxy(getApplicationContext());
            String proxyUrl = proxy.getProxyUrl("http://fs.mv.web.kugou.com/201805161024/f6afd843dc2f8537ecd3989c61ba90ad/G129/M03/0D/01/IYcBAFr5Wf6AUjHcAa3LVmX3WWw629.mp4");
            String proxyUrl2 = proxy.getProxyUrl("http://fs.mv.web.kugou.com/201805161041/718bff9a0e01a650c78c71056eb91139/G127/M06/0B/15/vw0DAFrvus2AJpAvAjnbY2rw5mw659.mp4");

            list = new ArrayList<>();
            list.add(proxyUrl2);
            list.add("http://img1.imgtn.bdimg.com/it/u=344091145,309580146&fm=27&gp=0.jpg");
            list.add("http://img2.imgtn.bdimg.com/it/u=3817131034,1038857558&fm=27&gp=0.jpg");
            list.add(proxyUrl);
            list.add("http://img3.imgtn.bdimg.com/it/u=1399356699,3785361628&fm=27&gp=0.jpg");
        }else {
            list = new ArrayList<>();
        }
        banner.dataChange(list);
    }

    @Override
    protected void onDestroy()
    {
        banner.destroy();
        super.onDestroy();
    }
}

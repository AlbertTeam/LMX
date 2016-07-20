package com.example.lenovo.mat;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView image=(ImageView)findViewById(R.id.imageView);
        Bitmap bim= BitmapFactory.decodeResource(getResources(), R.drawable.img);
/*        String path = Environment.getExternalStorageDirectory() + "/lmxlmx/er/leaf.jpg";
        Bitmap bim= BitmapFactory.decodeFile(path);*/
        int w=bim.getWidth();
        int h=bim.getHeight();
        Bitmap bim2=bim.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        int[] inputs=new int[w*h];
        int[] pixels=new int[w*h];

        for (int x=0;x<w;x++)
        {
            for (int y=0;y<h;y++)
            {
                inputs[y*w+x]=bim.getPixel(x,y);
                pixels[y*w+x]=bim.getPixel(x,y);
            }
        }
/*
       //最大类间差分二值化处理
        Leijianfangcha leijian=new Leijianfangcha();
        inputs= leijian.LeijianErZhi(w,h,inputs);
*/

      //迭代二值化处理
        ErZhiClass erzhi=new ErZhiClass();
        inputs=erzhi.ErZhiHua(w, h, inputs);
   /*    //中值滤波
        ZhongzhiClass zhongzhi=new ZhongzhiClass();
        inputs=zhongzhi.lvbo(inputs,w,h);*/


/*        //细化算法提取骨架
        boolean s;
        MatClass matClass=new MatClass();
        s=matClass.matswitch(w, h,inputs);
        inputs= matClass.matresult(s,w,h);

       //骨架部分用红色标出
        for (int x=0;x<w;x++)
        {
            for (int y=0;y<h;y++)
            {
              if (inputs[y*w+x]== Color.WHITE)
                  pixels[y*w+x]=Color.RED;
            }
        }
        //红色骨架在原图形中显示
        for (int x=0;x<w;x++)
        {
            for (int y=0;y<h;y++)
            {
                bim2.setPixel(x,y,pixels[y*w+x]);
            }
        }*/
        for (int x=0;x<w;x++)
        {
            for (int y=0;y<h;y++)
            {
                bim2.setPixel(x,y,inputs[y*w+x]);
            }
        }

       image.setImageBitmap(bim2);
    }
}

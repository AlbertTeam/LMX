package com.example.lenovo.harris;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lenovo.linehough.BilinearZoom;
import com.example.lenovo.linehough.Canny;
import com.example.lenovo.linehough.Gauss;
import com.example.lenovo.linehough.Huiduhua;
import com.example.lenovo.linehough.LineHough;
import com.example.lenovo.linehough.ReturnXY;

public class MainActivity extends AppCompatActivity {
    private int w;
    private int h;
    private  int[] inputs;
    private Bitmap bim2;
    private  Bitmap bim3;
    private Bitmap bim4;
    private ImageView image;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        image = (ImageView) findViewById(R.id.image1);
       // tv = (TextView)findViewById(R.id.textdistance);
        tv=new TextView(this);
        Bitmap bim1 = BitmapFactory.decodeResource(getResources(), R.drawable.leafwhite);
        w = bim1.getWidth();
        h = bim1.getHeight();
        inputs = new int[w * h];
        bim2 = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bim3=Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bim4=bim1.copy(Bitmap.Config.ARGB_8888,true);
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                inputs[y * w + x] = bim1.getPixel(x, y);
                bim3.setPixel(x,y,Color.WHITE);
            }
        }
        image.setImageBitmap(bim1);
        findViewById(R.id.huiduhua).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //灰度化处理
                Huiduhua huiduhua = new Huiduhua();
                inputs = huiduhua.huiDuMethod(w, h, inputs);
                viewImage(w, h, inputs);
            }
        });
        findViewById(R.id.erzhihua).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //二值化处理
                ErZhi erzhi = new ErZhi();
                inputs = erzhi.ErZhiHua(w, h, inputs);
                viewImage(w, h, inputs);
            }
        });
        findViewById(R.id.canny).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Canny边缘检测
                Canny canny = new Canny();
                inputs = canny.Cannyfilter(w, h, inputs);
                viewImage(w, h, inputs);


            }
        });
        findViewById(R.id.gujia).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //骨架提取
                boolean s;
                Mat matClass = new Mat();
                s = matClass.matswitch(w, h, inputs);
                inputs = matClass.matresult(s, w, h);
                viewImage(w, h, inputs);
            }
        });

        findViewById(R.id.linehough).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //检测直线
                LineHough lines = new LineHough();
                lines.init(inputs, w, h);
                int ww = w / 15;
                int hh = h / 15;
                for (int x = 0; x < w; x = x + ww) {
                    for (int y = 0; y < h; y = y + hh) {
                        inputs = lines.process(x, y);
                    }
                }
                viewImage(w, h, inputs);
            }
        });
        findViewById(R.id.harris).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Harris角点检测直线角点
                HarrisCorner harrisCorner = new HarrisCorner();
                inputs = harrisCorner.filter(w, h, inputs);
                for (int x = 0; x < w; x++) {
                    for (int y = 0; y < h; y++) {
                        if (inputs[y * w + x] == Color.RED)
                            inputs[y * w + x] = Color.GREEN;
                        bim2.setPixel(x, y, inputs[y * w + x]);
                    }
                }
                image.setImageBitmap(bim2);
            }
        });
        findViewById(R.id.distance).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double[][]  ax=new double[5][5];
                double[][] ay=new double[5][5];
                double[]  bx=new double[5];
                double[]  by=new double[5];
                double[] kx=new double[5];
                double[] ky=new double[5];
                int[] pointxy = new int[2 * 100];
                double[] d = new double[100];
                ReturnXY returnXY = new ReturnXY();
                int count = 0;
                for (int y = 0; y < h; y++) {
                    for (int x = 0; x < w; x++) {
                        if (inputs[y * w + x] == Color.GREEN) {
                            pointxy = returnXY.xyMethod(x, y);
                            count++;
                            if (count<=4)
                            { ax[count][1] = 1.0;
                            ax[count][2] = (double)x;
                            ax[count][3] = (double)y;
                            ax[count][4] = (double)(x * y);
                                ay[count][1] = 1.0;
                                ay[count][2] = (double)x;
                                ay[count][3] = (double)y;
                                ay[count][4] = (double)(x * y);
                            }
                        }
                    }
                }
                //这里如果直接用ay=ax给ay数组赋值的话，当gauss1.elimination(ax, bx);执行过后，
                // ax数组元素值发生了变化，转换为高斯消元后的上三角矩阵形式，而由于ay=ax这条语句，ay元素值也会发生改变，变成了跟ax一样的高斯消元形式
                //这就导致在执行gauss1.elimination(ay, by);过程中，ay已不用进行消元，于是l总是为0，by元素值不变。
                // 即ay进行了消元化简，by没有进行消元，故之后得到的方程解是错误的。这也是为什么ax，ay要分别赋值的原因。
                //疑问：在消元过程中，我是先将ax传给另一个数组a，实际上是数组a进行了消元，可是主程序中的ax元素值为什么也会发生变化？


                double dis = 119;
                bx[1] = (double)pointxy[0];
                bx[2] = (double)pointxy[0] - dis;
                bx[3] = (double)pointxy[0] - dis;
                bx[4] = (double)pointxy[0];

                by[1] = (double)pointxy[1];
                by[2] = (double)pointxy[1];
                by[3] = pointxy[1] + dis;
                by[4] = pointxy[1] + dis;

                Gauss gauss1 = new Gauss();
                gauss1.elimination(ax, bx);
                kx = gauss1.back();

                gauss1.elimination(ay, by);
                ky = gauss1.back();

                //对原图进行校正
               double newx=0,newy=0;
                int[] countnew=new int[w*h];
                int[] orginput=new int[w*h];

                for (int y = 0; y < h; y++) {
                    for (int x = 0; x < w; x++) {
                        countnew[y * w + x]=bim4.getPixel(x,y);
                        orginput[y * w + x]=bim4.getPixel(x,y);
                    }
                }
                BilinearZoom bilinearZoom=new BilinearZoom(orginput);
                //在原图中进行插值
                for (int y = 0; y < h; y++) {
                    for (int x = 0; x < w; x++) {
                        if (inputs[y * w + x]==Color.BLACK||inputs[y * w + x]==Color.GREEN)
                        {   newx=(kx[1]+kx[2]*x+kx[3]*y+kx[4]*x*y);
                            newy=(ky[1]+ky[2]*x+ky[3]*y+ky[4]*x*y);
                            bilinearZoom.xyBlinear(newx,newy,w,h,countnew);
                        }
                    }
                }

                for (int y = 0; y < h; y++) {
                    for (int x = 0; x < w; x++) {
                            bim4.setPixel(x,y,countnew[y * w + x]);
                    }
                }
               image.setImageBitmap(bim4);


    /*           //对二值图像进行校正
                int newx=0,newy=0;
                int[] countnew=new int[w*h];
                for (int y = 0; y < h; y++) {
                    for (int x = 0; x < w; x++) {
                        if (inputs[y * w + x]==Color.BLACK||inputs[y * w + x]==Color.GREEN)
                        {   newx=(int)(kx[1]+kx[2]*x+kx[3]*y+kx[4]*x*y);
                            newy=(int)(ky[1]+ky[2]*x+ky[3]*y+ky[4]*x*y);
                            countnew[newy*w+newx]=1;//标记所有校正后的像素点，之后再给这些点标为黑色。
                            // 如果校正一个点就给它标为黑色，则在之后的循环检测中，这些点可能就被当成未校正的黑点被再次校正，结果就会出错
                            bim3.setPixel(x,y,Color.BLACK);
                     }
                    }
                }
                for (int y = 0; y < h; y++) {
                    for (int x = 0; x < w; x++) {
                        if (countnew[y*w+x]==1)
                        { inputs[y*w+x]=Color.BLACK;
                            bim3.setPixel(x,y,inputs[y*w+x]);}
                    }
                    }
              image.setImageBitmap(bim3);*/

        /*        //用Textview显示相邻两角点间距离
                tv.setMaxLines(count / 2 + 1);
                String text = "";
                for (int i = 0; i < 3; i++) {
                    d[i] = returnXY.pointsDistance(pointxy[2 * i], pointxy[2 * i + 1], pointxy[2 * i + 2], pointxy[2 * i + 3]);
                    //if (d[i]>110&&d[i]<160)
                    {
                        text += "角点距离(" + pointxy[2 * i] + "," + pointxy[2 * i + 1] + "),(" + pointxy[2 * i + 2] + "," + pointxy[2 * i + 3] + ")=" + d[i] + "\n";
                        inputs[pointxy[2 * i + 1] * w + pointxy[2 * i]] = Color.RED;
                        inputs[pointxy[2 * i + 3] * w + pointxy[2 * i + 2]] = Color.RED;
                    }
                }
                tv.setText(text);
                setContentView(tv);*/
            }
        });
findViewById(R.id.gauss).setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        double[][] a={{0,0,0},{0,1,-1},{0,1,1}};
        double[] b={0,1,2};
        double[] x=new double[3];
        Gauss gauss=new Gauss();
        gauss.elimination(a,b);
        x=gauss.back();
        tv.setText(x[1]+","+x[2]);
        setContentView(tv);

    }
});
        findViewById(R.id.wait).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           viewImage(w, h, inputs);
            }
        });

    }

    public void viewImage(int w,int h,int[] inputs)
    {
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                bim2.setPixel(x, y, inputs[y * w + x]);
            }
        }
        image.setImageBitmap(bim2);
    }

        }



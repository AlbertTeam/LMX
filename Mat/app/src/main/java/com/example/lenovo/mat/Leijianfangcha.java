package com.example.lenovo.mat;

/**
 * Created by lenovo on 2016/6/20.
 */
public class Leijianfangcha {
    public  Leijianfangcha()
    {System.out.println(" Leijianfangcha");}

    public int[]  LeijianErZhi(int w,int h,int[] inputs)
    {
        int[] ray = new int[w*h];
        int[] alpha = new int[w*h];
        int[] newpixel = new int[w*h];

        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                alpha[y*w+x] = inputs[y*w+x] & 0xFF000000;
                int red = (inputs[y*w+x] & 0x00FF0000) >> 16;
                int green = (inputs[y*w+x] & 0x0000FF00) >> 8;
                int blue = inputs[y*w+x] & 0x000000FF;
                int gray = (int) ((float) red * 0.3 + (float) green * 0.59 + (float) blue * 0.11);
                newpixel[y*w+x] = alpha[y*w+x] | (gray << 16) | (gray << 8) | gray;
                ray[y*w+x] = gray;//将像素点灰度值放在数组ray[][]中
            }
        }

        int i,j,t,count1=0,count2=0,sum1=0,sum2=0;
        int total=w*h;
        double bw,fw,bp,fp,zp;
        double[] fangcha=new double[256];
        int [] histogram=new int[256];
        for ( t=0;t<=histogram.length;t++) {
            for (i = 0; i < w; i++)
            {
                for (j = 0; j < h; j++)
                {
                    if (ray[j*w+i] == t)
                        histogram[t]++;
                }
            }
        }

        for (t=0;t<fangcha.length;t++)
        {//得出背景像素点的比例和平均灰度值
            for (i = 0; i < t; i++) {
                count1 += histogram[i];//背景像素点的总个数
                sum1 += histogram[i] * i;//背景像素点的灰度总值
            }
            bw = count1 / total;//背景像素点的比例
            bp = (count1 == 0) ? 0 : (sum1 / count1);//背景像素点的平均灰度值

            for (j = i; j <histogram.length; j++) {
                count2 += histogram[j];//前景像素点的总个数
                sum2 += histogram[j] * j;//前景像素点的灰度总值
            }
            fw = count2 / total;//前景像素点比例
            fp = (count2 == 0) ? 0 : (sum2 / count2);//前景像素点的平均灰度值

            zp = bw * bp + fw * fp; //得出总的平均灰度值

            fangcha[t] = (Math.pow((bp - zp), 2)) * bw + (Math.pow((fp - zp), 2)) * fw;    //得出类间方差
        }
        //获取最大类间方差对应的灰度值，即得到最佳阈值
        double zz=fangcha[0];
        int yz,zuijiayz=0;
        for (yz=1;yz<fangcha.length;yz++)
        {
            if (fangcha[yz]>zz)
            { zz=fangcha[yz];
                zuijiayz=yz;}
        }
        //二值化
        for (i=0;i<w;i++)
        {
            for (j=0;j<h;j++)
            {
                if (ray[j*w+i]>zuijiayz)
                {
                    ray[j*w+i]=255;
                    newpixel[j*w+i]=255|(ray[j*w+i]<<16)|(ray[j*w+i]<<8)|ray[j*w+i];
                }
                else
                {
                    ray[j*w+i]=0;
                    newpixel[j*w+i]=255|(ray[j*w+i]<<16)|(ray[j*w+i]<<8)|ray[j*w+i];
                }

            }
        }
        return newpixel;
    }
}



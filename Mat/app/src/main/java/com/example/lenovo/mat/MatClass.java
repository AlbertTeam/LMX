package com.example.lenovo.mat;

import android.graphics.Color;

/**
 * Created by lenovo on 2016/5/31.
 */
public class MatClass {
    private int[] imgBuf;
    private int[] neighbor;
    private int[] mark;
    private int markNum1;
    private int markNum2;

    public MatClass()
    {System.out.println("MatClass");}

    public boolean matswitch(int w,int h,int[] inputs)
    {
          imgBuf=new int[w*h];
          imgBuf=inputs;
          neighbor=new int[10];
          mark=new int[w*h];
          markNum1=0;
            //第一步
            for(int x=1;x<w-1;x++)
            {
                for(int y=1;y<h-1;y++)
                {
                    //条件1：p必须是边界点，值为1（白），8邻域内至少有1个像素点值为0（黑）
                    if(imgBuf[y*w+x]==Color.BLACK) continue;

                    neighbor[2]= ((imgBuf[(y-1)*w+x]& 0x00ff0000)>>16)/255;
                    neighbor[3]= ((imgBuf[(y-1)*w+x+1]& 0x00ff0000)>>16)/255;
                    neighbor[4]= ((imgBuf[y*w+x+1]& 0x00ff0000)>>16)/255;
                    neighbor[5]= ((imgBuf[(y+1)*w+x+1]& 0x00ff0000)>>16)/255;
                    neighbor[6]= ((imgBuf[(y+1)*w+x]& 0x00ff0000)>>16)/255;
                    neighbor[7]= ((imgBuf[(y+1)*w+x-1]& 0x00ff0000)>>16)/255;
                    neighbor[8]= ((imgBuf[(y)*w+x-1]& 0x00ff0000)>>16)/255;
                    neighbor[9]= ((imgBuf[(y-1)*w+x-1]& 0x00ff0000)>>16)/255;

                    if (neighbor[2]*neighbor[3]*neighbor[4]*neighbor[5]*neighbor[6]*neighbor[7]*neighbor[8]*neighbor[9]!=0) continue;

                    //条件2：2<=N(p）<=6
                    int np=(neighbor[2]+neighbor[3]+neighbor[4]+neighbor[5]+neighbor[6]+neighbor[7]+neighbor[8]+neighbor[9]);
                    if(np<2 || np>6) continue;

                    //条件3：T(p）=1
                    int tp=0;
                    for(int i=3;i<=9;i++)
                    {
                       /* if(neighbor[i]-neighbor[i-1]==Color.WHITE-Color.BLACK )*/
                        if(neighbor[i]-neighbor[i-1]==1 )
                            tp++;
                    }
                    if(neighbor[2]-neighbor[9]==1)
                        tp++;
                    if(tp!=1) continue;

                    //条件4：p2*p4*p6=0
                    if(neighbor[2]*neighbor[4]*neighbor[6]!=0)
                        continue;
                    //条件5：p4*p6*p8=0
                    if(neighbor[4]*neighbor[6]*neighbor[8]!=0)
                        continue;

                    //标记要被删除的点
                    mark[y*w+x]=1;
                    markNum1++;
                }
            }

            //将标记删除的点置为背景色
            if(markNum1>0)
            {
                for(int x=1;x<w-1;x++)
                {
                    for(int y=1;y<h-1;y++)
                    {
                      //删除被标记的点，即置为背景色黑色
                        if(mark[y*w+x]==1)
                        {
                            imgBuf[y*w+x]=Color.BLACK;
                        }
                    }
                }
            }



            //第二步
            markNum2=0;
            for(int x=1;x<w-1;x++)
            {
                for(int y=1;y<h-1;y++)
                {
                    //条件1：p必须是前景点WHITE
                    if(imgBuf[y*w+x]==Color.BLACK) continue;


                    neighbor[2]= ((imgBuf[(y-1)*w+x]& 0x00ff0000)>>16)/255;
                    neighbor[3]= ((imgBuf[(y-1)*w+x+1]& 0x00ff0000)>>16)/255;
                    neighbor[4]= ((imgBuf[y*w+x+1]& 0x00ff0000)>>16)/255;
                    neighbor[5]= ((imgBuf[(y+1)*w+x+1]& 0x00ff0000)>>16)/255;
                    neighbor[6]= ((imgBuf[(y+1)*w+x]& 0x00ff0000)>>16)/255;
                    neighbor[7]= ((imgBuf[(y+1)*w+x-1]& 0x00ff0000)>>16)/255;
                    neighbor[8]= ((imgBuf[(y)*w+x-1]& 0x00ff0000)>>16)/255;
                    neighbor[9]= ((imgBuf[(y-1)*w+x-1]& 0x00ff0000)>>16)/255;

                    if (neighbor[2]*neighbor[3]*neighbor[4]*neighbor[5]*neighbor[6]*neighbor[7]*neighbor[8]*neighbor[9]!=0) continue;

                    //条件2：2<=N(p)<=6
                    int np=(neighbor[2]+neighbor[3]+neighbor[4]+neighbor[5]+neighbor[6]+neighbor[7]+neighbor[8]+neighbor[9]);
                    if(np<2 || np>6) continue;

                    //条件3：T(p)=1
                    int tp=0;
                    for(int i=3;i<=9;i++)
                    {
                        if(neighbor[i]-neighbor[i-1]==1)
                            tp++;
                    }
                    if(neighbor[2]-neighbor[9]==1)
                        tp++;
                    if(tp!=1) continue;

                    //条件4：p2*p4*p8==0
                    if(neighbor[2]*neighbor[4]*neighbor[8]!=0)
                        continue;
                    //条件5：p2*p6*p8==0
                    if(neighbor[2]*neighbor[6]*neighbor[8]!=0)
                        continue;

                    //标记删除
                    mark[y*w+x]=1;
                    markNum2++;
                }
            }

            //将标记删除的点置为背景色BLACK
            if (markNum2>0)
            {
                for (int x = 1; x < w-1; x++)
                {
                    for (int y = 1; y < h-1; y++)
                    {
                        if (mark[y * w + x] == 1)
                        {
                            imgBuf[y * w + x] = Color.BLACK;
                        }
                    }
                }
            }
        //一次周期循环后，不再出现标记删除的点时，说明已生成骨架了
        if (markNum1==0&&markNum2==0)  return false;
        else  return true;

    }


    public int[] matresult(boolean s,int w,int h)
    {
        while (s)
        {
           s= matswitch(w, h, imgBuf);
        }
        return imgBuf;
    }

}

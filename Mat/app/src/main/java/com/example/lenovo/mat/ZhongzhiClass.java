package com.example.lenovo.mat;

/**
 * Created by lenovo on 2016/6/1.
 */
public class ZhongzhiClass {

    public ZhongzhiClass()
    {        System.out.println("ZhongzhiClass");}

    public int[] lvbo(int[] inputs,int w,int h)
    {

        int[] newpixel = new int[w*h];
        //中值滤波处理
        int[] tpRed = new int[9];
        int[] tpGreen = new int[9];
        int[] tpBlue = new int[9];
        for (int i = 1; i < w - 1; i++) {
            for (int j = 1; j < h - 1; j++) {
                int col0 = inputs[(j - 1)*w+i-1]; //(i - 1, j - 1);
                int col1 = inputs[(j)*w+i-1];//(i - 1, j);
                int col2 = inputs[(j + 1)*w+i-1];//(i - 1, j + 1);
                int col3 = inputs[(j - 1)*w+i];//(i, j - 1);
                int col4 =inputs[(j)*w+i];//(i, j);
                int col5 =inputs[(j+1)*w+i];//(i, j + 1);
                int col6 =inputs[(j - 1)*w+i+1];//(i + 1, j - 1);
                int col7 =inputs[(j)*w+i+1];//(i + 1, j);
                int col8 = inputs[(j+1)*w+i+1];//(1 + 1, j + 1);

                tpRed[0] = (col0 & 0x00FF0000) >> 16;
                tpRed[1] = (col1 & 0x00FF0000) >> 16;
                tpRed[2] = (col2 & 0x00FF0000) >> 16;
                tpRed[3] = (col3 & 0x00FF0000) >> 16;
                tpRed[4] = (col4 & 0x00FF0000) >> 16;
                tpRed[5] = (col5 & 0x00FF0000) >> 16;
                tpRed[6] = (col6 & 0x00FF0000) >> 16;
                tpRed[7] = (col7 & 0x00FF0000) >> 16;
                tpRed[8] = (col8 & 0x00FF0000) >> 16;

                tpGreen[0] = (col0 & 0x0000FF00) >> 8;
                tpGreen[1] = (col1 & 0x0000FF00) >> 8;
                tpGreen[2] = (col2 & 0x0000FF00) >> 8;
                tpGreen[3] = (col3 & 0x0000FF00) >> 8;
                tpGreen[4] = (col4 & 0x0000FF00) >> 8;
                tpGreen[5] = (col5 & 0x0000FF00) >> 8;
                tpGreen[6] = (col6 & 0x0000FF00) >> 8;
                tpGreen[7] = (col7 & 0x0000FF00) >> 8;
                tpGreen[8] = (col8 & 0x0000FF00) >> 8;

                tpBlue[0] = col0 & 0x000000FF;
                tpBlue[1] = col1 & 0x000000FF;
                tpBlue[2] = col2 & 0x000000FF;
                tpBlue[3] = col3 & 0x000000FF;
                tpBlue[4] = col4 & 0x000000FF;
                tpBlue[5] = col5 & 0x000000FF;
                tpBlue[6] = col6 & 0x000000FF;
                tpBlue[7] = col7 & 0x000000FF;
                tpBlue[8] = col8 & 0x000000FF;

                for (int rj = 0; rj < 8; rj++) {
                    for (int ri = 0; ri < 8 - rj; ri++) {
                        if (tpRed[ri] > tpRed[ri + 1]) {
                            int Red_Temp = tpRed[ri];
                            tpRed[ri] = tpRed[ri + 1];
                            tpRed[ri + 1] = Red_Temp;
                        }

                        if (tpGreen[ri] > tpGreen[ri + 1]) {
                            int Green_Temp = tpGreen[ri];
                            tpGreen[ri] = tpGreen[ri + 1];
                            tpGreen[ri + 1] = Green_Temp;
                        }

                        if (tpBlue[ri] > tpBlue[ri + 1]) {
                            int Blue_Temp = tpBlue[ri];
                            tpBlue[ri] = tpBlue[ri + 1];
                            tpBlue[ri + 1] = Blue_Temp;
                        }
                    }
                }
                int medianRed = tpRed[4];
                int medianGreen = tpGreen[4];
                int medianBlue = tpBlue[4];
                newpixel[j*w+i] = 255 << 24 | medianRed << 16 | medianGreen << 8 | medianBlue;

            }
        }
        return newpixel ;
    }

}
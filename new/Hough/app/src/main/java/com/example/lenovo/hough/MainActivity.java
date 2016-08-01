package com.example.lenovo.hough;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;


public class MainActivity extends AppCompatActivity {
    final double pi = Math.PI;
    private int[] inPiexl;
    private int[] outPiexl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView image = (ImageView) findViewById(R.id.imageView);
        String path = Environment.getExternalStorageDirectory() + "/lmxlmx/er/leaf.jpg";
        Bitmap bim1 = BitmapFactory.decodeFile(path);
        Bitmap bim2 = bim1.copy(Bitmap.Config.ARGB_8888, true);
        int width = bim1.getWidth();
        int height = bim1.getHeight();
        int ro = (int) Math.sqrt(height * height + width * width);
        int[][] hist = new int[ro][91];
        inPiexl = new int[width * height];
        outPiexl = new int[width*height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                inPiexl[j * width + i] = bim1.getPixel(i, j);
            }
        }
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (inPiexl[y * width + x] == Color.BLACK) {
                    for (int theta = 0; theta < 91; theta++) {
                        int r = (int) (x * Math.cos(theta * pi / 180) + y * Math.sin(theta * pi / 180));
                        if (r >= 0 && r <= ro)
                            hist[r][theta]= hist[r][theta]+1;
                    }
                }
            }
        }
        int histmax =100;
        for (int r = 0; r <= ro; r++) {
            for (int theta = 0; theta < 91; theta++) {
                if (hist[r][theta] >= histmax) {
                    //  histmax = hist[r][theta];
                   drawline(width,height,r, theta);
                }
            }
        }
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bim2.setPixel(x, y, outPiexl[y * width + x]);
            }
        }
        image.setImageBitmap(bim2);
    }

    public void drawline(int width,int height,int r,int theta)
    {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (r == (int) (x * Math.cos(theta * pi / 180) + y * Math.sin(theta * pi / 180)))
                    inPiexl[y * width + x] = Color.RED;
            }
        }
    }


    }




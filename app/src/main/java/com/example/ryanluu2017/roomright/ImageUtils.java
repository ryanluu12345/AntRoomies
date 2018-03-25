package com.example.ryanluu2017.roomright;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by Ryanluu2017 on 3/23/2018.
 */

public class ImageUtils {

    //Downloads the image from the link and then sets the profile picture image view
    private class DownloadImageTask extends AsyncTask<String,Void,Bitmap> {
        ImageView imageView;

        public Bitmap resizeBitmap(Bitmap bm){
            int scaleToUse = 15; // this will be our percentage
            int sizeY = bm.getHeight() * scaleToUse / 100;
            int sizeX = bm.getWidth() * sizeY / bm.getHeight();
            bm= Bitmap.createScaledBitmap(bm,sizeX,sizeY,false);
            return bm;
        }

        public DownloadImageTask(ImageView imageView){
            this.imageView = imageView;
        }

        /*
            doInBackground(Params... params)
                Override this method to perform a computation on a background thread.
         */
        protected Bitmap doInBackground(String...urls){
            String urlOfImage = urls[0];
            Bitmap pic = null;
            try{
                InputStream is = new URL(urlOfImage).openStream();
                /*
                    decodeStream(InputStream is)
                        Decode an input stream into a bitmap.
                 */
                pic = BitmapFactory.decodeStream(is);
                pic= resizeBitmap(pic);


            }catch(Exception e){ // Catch the download exception
                e.printStackTrace();
            }
            return pic;
        }

        /*
            onPostExecute(Result result)
                Runs on the UI thread after doInBackground(Params...).
         */
        protected void onPostExecute(Bitmap result){
            imageView.setImageBitmap(result);
        }
    };


}




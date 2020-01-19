package com.petstore.view.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

    private final String TAG = DownloadImageTask.class.getSimpleName();

    private HttpCallback callbackListener;

    /**
     * execute before http request
     */
    @Override
    protected void onPreExecute() {
        this.callbackListener.onPreExecute();
    }

    /**
     * this method calls when call execute method
     *
     * @param params variable args parameter
     * @return serialized response
     */
    @Override
    protected Bitmap doInBackground(String... params) {
        HttpURLConnection connection = null;

        String my_url = params[0];

        try {
            URL url = new URL(my_url);

            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();

            InputStream is = connection.getInputStream();

            /**
             * decode bitmap from stream
             */
            Bitmap bitmap = BitmapFactory.decodeStream(is);

            /**
             * put bitmap on cache
             */
            BitmapCache.getInstance().putBitmap(my_url, bitmap);

            return bitmap;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * execute after http response
     *
     * @param bitmap serialized response
     */
    @Override
    protected void onPostExecute(final Bitmap bitmap) {
        this.callbackListener.onPostExecute(bitmap);

    }

    /**
     * HttpCallback Interface
     */
    public interface HttpCallback {
        void onPostExecute(Bitmap result);
        void onPreExecute();
    }

    /**
     * method to set http callback interface
     *
     * @param callbackListener HttpCallback instance
     */
    public void setHttpListener(final HttpCallback callbackListener) {
        this.callbackListener = callbackListener;
    }
}

package gov.pianzong.task;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import gov.pianzong.adapter.NewsListAdapter;

/**
 * Created by GDB437 on 7/4/13,HoloCnbeta
 */
public class LogoLoadTask extends AsyncTask<String,Integer,Bitmap> {
    final NewsListAdapter.ViewHolder holder;
    final int position;
    public LogoLoadTask(NewsListAdapter.ViewHolder holder) {
        this.holder = holder;
        this.position = holder.position;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        URL url = null;
        Bitmap bitmap = null;
        try {
            url = new URL(strings[0]);
        } catch (MalformedURLException e) {
            return null;
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if(position == holder.position)
            holder.logoImage.setImageBitmap(bitmap);
    }
}

package com.example.kamhi.ex180;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Looper;
import android.os.ResultReceiver;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Kamhi on 28/10/2017.
 */

public class MyService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    private static final String TAG = "com.example.kamhi.ex180";

    public MyService() {
        super("MyService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG,"The intent has now been started");
        boolean isMainThread = Looper.getMainLooper() == Looper.myLooper();
        if (intent != null) {
            final String action = intent.getAction();
            ResultReceiver receiver = (ResultReceiver) intent.getParcelableExtra("receiver");
            Bundle bundle = new Bundle();
            String destination = intent.getStringExtra("destination");
            try {
                URL url = new URL(intent.getStringExtra("url"));
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                OutputStream outputStream = new FileOutputStream(destination);
                dowloadImage(input, outputStream);
                //Bitmap img = downloadImage(input);
              //  bundle.putParcelable("img", img);
                bundle.putString("img", destination);
                receiver.send(0, bundle);
                outputStream.flush();
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //outputstring
        }
    }

    private void dowloadImage(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] data = new byte[1024];
        int count = 0;
        int total = 0;
        while ((count = inputStream.read(data)) != -1){
            total += count;
            outputStream.write(data, 0, count);
        }
    }

    private Bitmap downloadImage(InputStream inputStream) {
        return BitmapFactory.decodeStream(inputStream);
    }
}

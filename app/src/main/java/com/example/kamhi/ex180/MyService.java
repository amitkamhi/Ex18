package com.example.kamhi.ex180;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Looper;
import android.os.ResultReceiver;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
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
    public static final String EXTRA_RECEIVER = "receiver";
    public static final String ACTION_FETCH_IMG = "fetch_img";
    public static final String ACTION_FETCH_TXT = "fetch_txt";

    public static final String EXTRA_URL = "extra_url";
    public static final String EXTRA_OUT_IMG = "img";
    public static final String EXTRA_OUT_TXT = "txt";

    public MyService() {
        super("MyService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG,"The intent has now been started");
        boolean isMainThread = Looper.getMainLooper() == Looper.myLooper();
        if (intent != null) {
            final String action = intent.getAction();
            ResultReceiver receiver = (ResultReceiver) intent.getParcelableExtra(EXTRA_RECEIVER);
            Bundle bundle = new Bundle();
            try {
                URL url = new URL(intent.getStringExtra(EXTRA_URL));
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();

                Bitmap img = downloadImage(input);
                bundle.putParcelable("img", img);
                receiver.send(0, bundle);

                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Bitmap downloadImage(InputStream inputStream) {
        return BitmapFactory.decodeStream(inputStream);
    }
}

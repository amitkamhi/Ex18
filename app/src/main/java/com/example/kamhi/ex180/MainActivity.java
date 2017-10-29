package com.example.kamhi.ex180;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Random;

//https://github.com/melardev/AndroidBackgroundTuts/tree/master/app/src/main/java/com/melardev/backgrounddemos

public class MainActivity extends Activity {

    private MyService myService;
    private Context context;
    private ImageView imageView;
    private MyIntentServiceReceiver intentReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView)findViewById(R.id.imageView);
        intentReceiver = new MyIntentServiceReceiver(this, new Handler(Looper.getMainLooper()));

        context = this;
        Button changeImage = (Button) findViewById(R.id.changeImage);
        changeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] files = new String[]{
                        "https://www.dropbox.com/s/m3sphvzt85virph/Boston%20City%20Flow.jpg?dl=1",
                        "https://www.dropbox.com/s/bkvpgr2e1tctjkc/Costa%20Rican%20Frog.jpg?dl=1",
                        "https://www.dropbox.com/s/knl90p2l15frns1/Pensive%20Parakeet.jpg?dl=1",
                        "http://nashpia.co.il/files/db05dcffaed944185f5c486b31c9b83c.jpg",
                        "http://www.calcalist.co.il/PicServer2/20122005/336292/60_F.jpg"};
                Random rnd = new Random();
                Intent intent = new Intent(context,MyService.class);
                intent.putExtra("url",files[rnd.nextInt(5)]);
                String destination = getFilesDir() + "/image.jpg";
                intent.putExtra("destination",destination);
                intent.putExtra("receiver", intentReceiver);
                startService(intent);

            }
        });
    }

    private void onImageDownloaded(Bitmap bmp) {
        this.imageView.setImageBitmap(bmp);
    }

    class MyIntentServiceReceiver extends ResultReceiver {

        private final MainActivity mainActivity;

        /**
         * Create a new ResultReceive to receive results.  Your
         * {@link #onReceiveResult} method will be called from the thread running
         * <var>handler</var> if given, or from an arbitrary thread if null.
         *
         * @param mainActivity
         * @param handler
         */
        public MyIntentServiceReceiver(MainActivity mainActivity, Handler handler) {
            super(handler);
            this.mainActivity = mainActivity;
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
 //           mainActivity.onImageDownloaded((Bitmap) resultData.getParcelable("img"));
            mainActivity.onImageDownloaded(BitmapFactory.decodeFile(resultData.getString("img")));

        }
    }
}

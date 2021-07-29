package com.codemybrainsout.rating;

import android.os.Bundle;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.codemybrainsout.ratingdialog.RatingDialog;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private RelativeLayout rlRate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showDialogRate();
    }

    private void showDialogRate() {

        final RatingDialog ratingDialog = new RatingDialog.Builder(this)
                .session(1)
                .date(1)
                .setNameApp(getString(R.string.app_name))
                .setIcon(R.mipmap.ic_launcher)
                .setEmail("namdh1212@gmail.com")
                .isShowButtonLater(true)
                .setTextButtonLater("Maybe Later")
                .ratingButtonColor(R.color.colorPrimary)
                .build();
        //Cancel On Touch Outside
        ratingDialog.setCanceledOnTouchOutside(false);
        //show
        ratingDialog.show();
        //thêm vào activity trong manifest
//        <intent-filter>
//            <action android:name="android.intent.action.SENDTO" />
//            <data android:scheme="mailto" />
//            <category android:name="android.intent.category.DEFAULT" />
//        </intent-filter>

        // thêm vào activity
//        android:windowSoftInputMode="adjustPan|adjustResize"
    }
}

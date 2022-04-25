package com.codemybrainsout.rating;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.codemybrainsout.ratingdialog.MaybeLaterCallback;
import com.codemybrainsout.ratingdialog.RateButtonCallback;
import com.codemybrainsout.ratingdialog.RatingDialog;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private RelativeLayout rlRate;
    Button btnRate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnRate = findViewById(R.id.btnRate);
        btnRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogRate();
            }
        });
    }

    private void showDialogRate() {

        RatingDialog ratingDialog = new RatingDialog.Builder(this)
                .session(1)
                .date(1)
                .setNameApp(getString(R.string.app_name))
                .setIcon(R.mipmap.ic_launcher)
                .setEmail("vapp.helpcenter@gmail.com")
                .isShowButtonLater(true)
                .isClickLaterDismiss(true)
                .setOnlickRate(new RateButtonCallback() {
                    @Override
                    public void onClick(int rate) {
                        Toast.makeText(MainActivity.this,"Rate" + String.valueOf(rate), Toast.LENGTH_SHORT).show();
                    }
                })
                .setTextButtonLater("Maybe Later")
                .setOnlickMaybeLate(new MaybeLaterCallback() {
                    @Override
                    public void onClick() {
                        Toast.makeText(MainActivity.this,"Feedback cannot be left blank", Toast.LENGTH_SHORT).show();
                    }
                })
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

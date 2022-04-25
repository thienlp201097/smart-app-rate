package com.codemybrainsout.ratingdialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.Task;

public class IAReview {
    private static volatile IAReview INSTANCE;

    public static synchronized IAReview getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new IAReview();
        }
        return INSTANCE;
    }

    ReviewManager manager;
    ReviewInfo reviewInfo;

    public void showIAReview(Activity context) {


        manager = ReviewManagerFactory.create(context);
        Task<ReviewInfo> request = manager.requestReviewFlow();
        request.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // We can get the ReviewInfo object
                reviewInfo = task.getResult();
                Task<Void> flow = manager.launchReviewFlow(context, reviewInfo);
                flow.addOnCompleteListener(task2 -> {
                    // The flow has finished. The API does not indicate whether the user
                    // reviewed or not, or even whether the review dialog was shown. Thus, no
                    // matter the result, we continue our app flow.
                });

            } else {
                // There was some problem, log or handle the error code.
                Toast.makeText(context, "" + task.getException().toString(), Toast.LENGTH_SHORT).show();

                String reviewErrorCode = task.getException().toString();
            }
        });
    }


    void openDialogFeedback(Activity activity, String name, int logo, String email, int starnumber) {
//        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        BottomSheetDialog bottomSheerDialog = new BottomSheetDialog(activity, R.style.DialogStyle);

        bottomSheerDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        BottomSheetDialog d = (BottomSheetDialog) dialog;
                        FrameLayout bottomSheet = d.findViewById(R.id.design_bottom_sheet);
                        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                    }
                }, 0);
            }
        });

        View parentView = activity.getLayoutInflater().inflate(R.layout.dialog_feedback, null);
        bottomSheerDialog.setContentView(parentView);
        bottomSheerDialog.show();
        TextView tvcount = parentView.findViewById(R.id.count);
        ImageView imglogo = parentView.findViewById(R.id.logo);
        TextView tvAppName = parentView.findViewById(R.id.tvAppName);
        EditText body = parentView.findViewById(R.id.body);
        RatingBar dialog_rating_rating_bar = parentView.findViewById(R.id.dialog_rating_rating_bar);
        dialog_rating_rating_bar.setRating(starnumber);
//        body.requestFocus();
        body.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvcount.setText(String.valueOf((s.length())) + "/500");

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

//        body.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean hasFocus) {
//                if (hasFocus){
//                    outlinedTextField.setHintEnabled(true);
//                }else{
//                    outlinedTextField.setHintEnabled(false);
//                }
//
//            }
//        });


        tvAppName.setText(name);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            imglogo.setImageDrawable(activity.getResources().getDrawable(logo, activity.getTheme()));
        } else {
            imglogo.setImageDrawable(activity.getResources().getDrawable(logo));
        }

        Button btnCancel = parentView.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheerDialog.dismiss();
            }
        });

        Button btnSubmit = parentView.findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (body.getText().toString().length() <= 0) {
                    Toast.makeText(activity, "Feedback cannot be left blank", Toast.LENGTH_SHORT).show();
                    return;
                }
                bottomSheerDialog.dismiss();

                Intent selectorIntent = new Intent(Intent.ACTION_SENDTO);
                selectorIntent.setData(Uri.parse("mailto:"));
                final Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback App " + name);
                emailIntent.putExtra(Intent.EXTRA_TEXT, "" + body.getText().toString());
                emailIntent.setSelector(selectorIntent);
                activity.startActivity(Intent.createChooser(emailIntent, "Send email..."));


//                Intent intent = new Intent(Intent.ACTION_SENDTO);
//                intent.setData(Uri.parse("mailto:"+email)); // only email apps should handle this
//                intent.putExtra(Intent.EXTRA_EMAIL, email);
//                intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback App "+ name);
//                intent.putExtra(Intent.EXTRA_TEXT   , ""+body.getText().toString());
//                if (intent.resolveActivity(activity.getPackageManager()) != null) {
//                    activity.startActivity(intent);
//                }


//                Intent i = new Intent(Intent.ACTION_SENDTO);
//                i.setType("message/rfc822");
//                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{email});
//                i.putExtra(Intent.EXTRA_SUBJECT, "Feedback App "+ name);
//                i.putExtra(Intent.EXTRA_TEXT   , ""+body.getText().toString());
//                try {
//                   activity.startActivity(Intent.createChooser(i, "Send feedback..."));
//                } catch (android.content.ActivityNotFoundException ex) {
//                    Toast.makeText(activity, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
//                }

            }
        });


//        LayoutInflater inflater = activity.getLayoutInflater();
//
//        View alertLayout = inflater.inflate(R.layout.dialog_feedback, null);
//        androidx.appcompat.app.AlertDialog.Builder alert = new androidx.appcompat.app.AlertDialog.Builder(activity);
//        alert.setView(alertLayout);
////        alert.setCancelable(false);
//        androidx.appcompat.app.AlertDialog dialog = alert.create();
//
//        Button btnSubmit = alertLayout.findViewById(R.id.btnSubmit);
//        btnSubmit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//        dialog.show();
    }
}

package com.kquicho.interestingphotos;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;

/**
 * Created by kquicho on 16-05-14.
 */
public class PhotoActivity extends AppCompatActivity {
    public static final String IMAGE_URI = "imageUri";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        final ImageView imageView = ButterKnife.findById(this, R.id.photo);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            postponeEnterTransition();
        }
        Picasso.with(this.getApplicationContext())
                .load(getIntent().getStringExtra(IMAGE_URI))
                .noFade()
                .into((imageView), new Callback() {
                    @Override
                    public void onSuccess() {
                        scheduleStartPostponedTransition(imageView);
                    }

                    @Override
                    public void onError() {
                        scheduleStartPostponedTransition(imageView);
                    }
                });



    }

    private void scheduleStartPostponedTransition(final View sharedElement) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            sharedElement.getViewTreeObserver().addOnPreDrawListener(
                    new ViewTreeObserver.OnPreDrawListener() {
                        @Override
                        public boolean onPreDraw() {
                            sharedElement.getViewTreeObserver().removeOnPreDrawListener(this);
                            startPostponedEnterTransition();
                            return true;
                        }
                    });
        }

    }


}

package com.kquicho.interestingphotos.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.kquicho.interestingphotos.PhotoActivity;
import com.kquicho.interestingphotos.R;
import com.kquicho.interestingphotos.models.FlickrPhoto;
import com.squareup.picasso.Picasso;

import java.text.MessageFormat;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kquicho on 16-05-13.
 */
public class InterestingPhotosAdapter extends RecyclerView.Adapter<InterestingPhotosAdapter.ViewHolder> {

    private List<FlickrPhoto> mFlickrPhotos;
    private Context mContext;

    public InterestingPhotosAdapter(List<FlickrPhoto> flickrPhotos, Context context) {
        mFlickrPhotos =  flickrPhotos;
        mContext = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.photo) ImageView photo;
        @BindView(R.id.progress_bar) ProgressBar progressBar;
        @BindString(R.string.photo_transition_name) String photoTransitionName;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View contactView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        FlickrPhoto flickrPhoto = mFlickrPhotos.get(position);

        Log.d("test", "test");
        holder.progressBar.setVisibility(View.VISIBLE);
        final String imageUri = buildFlickrPhotoUri(flickrPhoto);
        Picasso.with(mContext)
                .load(imageUri)
                .fit()
                .centerCrop()
                .into(holder.photo, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        holder.progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {

                    }
                });

        holder.photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PhotoActivity.class);
                intent.putExtra(PhotoActivity.IMAGE_URI, imageUri);
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.photo.setTransitionName(holder.photoTransitionName);
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation((Activity) mContext, holder.photo, holder.photoTransitionName);
                    mContext.startActivity(intent, options.toBundle());

                }else{
                    mContext.startActivity(intent);
                }
        }});
    }

    private String buildFlickrPhotoUri(FlickrPhoto photo){
        String photoUriFormat = "https://farm{0}.staticflickr.com/{1}/{2}_{3}.jpg";
        return MessageFormat.format(photoUriFormat, photo.getFarm(), photo.getServer(),
                photo.getId(), photo.getSecret());
    }

    @Override
    public int getItemCount() {
        return mFlickrPhotos.size();
    }


}

package com.kquicho.interestingphotos;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.kquicho.interestingphotos.adapters.InterestingPhotosAdapter;
import com.kquicho.interestingphotos.models.FlickrPhotoList;
import com.kquicho.interestingphotos.models.FlickrPhoto;
import com.kquicho.interestingphotos.models.InterestingnessResponse;
import com.kquicho.interestingphotos.network.FlickrAPIClient;
import com.kquicho.interestingphotos.util.EndlessRecyclerViewScrollListener;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";

    private static final String CURRENT_PAGE = "currentPage";
    private static final String PREVIOUS_ITEM_COUNT = "previousItemCount";
    private static final String FLICKR_PHOTOS = "flickrPhotos";
    private static final String MAX_PAGES = "maxPages";
    private static final int ITEMS_PER_PAGE = 30;
    private static final int SUCCESSFUL = 200;

    private int mMaxPages = -1;
    private boolean mIsDownloadInProgress = false;
    private InterestingPhotosAdapter mAdapter;
    private ArrayList<FlickrPhoto> mFlickrPhotos = new ArrayList<>();
    private EndlessRecyclerViewScrollListener mEndlessRecyclerViewScrollListener;
    @BindView(R.id.progress_bar) ProgressBar mProgressBar;
    @BindString(R.string.apikey) String mApiKey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = ButterKnife.findById(this, R.id.rv_photos);
        ButterKnife.bind(this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        if(savedInstanceState != null){
            int currentPage = savedInstanceState.getInt(CURRENT_PAGE);
            int previousItemCount = savedInstanceState.getInt(PREVIOUS_ITEM_COUNT);
            mMaxPages = savedInstanceState.getInt(MAX_PAGES);
            mEndlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener(gridLayoutManager,
                    currentPage, previousItemCount) {
                @Override
                public void onLoadMore(int page, int totalItemsCount) {
                    Log.d(TAG, "onLoadMore currentTotal: " + totalItemsCount);
                    downloadData(page);
                }
            };

            mFlickrPhotos = Parcels.unwrap(savedInstanceState.getParcelable(FLICKR_PHOTOS));
        }else{
            mEndlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener(gridLayoutManager) {
                @Override
                public void onLoadMore(int page, int totalItemsCount) {
                    Log.d(TAG, "onLoadMore currentTotal: " + totalItemsCount);
                    downloadData(page);
                }
            };
        }
        recyclerView.addOnScrollListener(mEndlessRecyclerViewScrollListener);
        mAdapter = new InterestingPhotosAdapter(mFlickrPhotos, this);
        recyclerView.setAdapter(mAdapter);
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (mFlickrPhotos.size() == 0) {
            downloadData(1);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt(CURRENT_PAGE, mEndlessRecyclerViewScrollListener.getCurrentPage());
        savedInstanceState.putInt(PREVIOUS_ITEM_COUNT, mEndlessRecyclerViewScrollListener.getPreviousTotalItemCount());
        savedInstanceState.putParcelable(FLICKR_PHOTOS, Parcels.wrap(mFlickrPhotos));
        savedInstanceState.putInt(MAX_PAGES, mMaxPages);
        super.onSaveInstanceState(savedInstanceState);
    }


    private void downloadData(final int pageNumber) {
        if(mMaxPages != -1 && mMaxPages < pageNumber){
            Toast.makeText(this, "No more pages to load", Toast.LENGTH_LONG).show();
            return;
        }
        if (!mIsDownloadInProgress) {
            Log.d(TAG, "downloadData pageNumber = " + pageNumber);
            mIsDownloadInProgress = true;
            mProgressBar.setVisibility(View.VISIBLE);

            FlickrAPIClient.FlickrApiInterface apiService = FlickrAPIClient.getFlickrApiClient(mApiKey);

            Call<InterestingnessResponse> call = apiService
                    .getListOfInterestingness(String.valueOf(pageNumber), String.valueOf(ITEMS_PER_PAGE));

            call.enqueue(new Callback<InterestingnessResponse>() {
                @Override
                public void onResponse(Call<InterestingnessResponse> call, Response<InterestingnessResponse> response) {
                    int status = response.code();
                    Log.d(TAG, "onResponse: status = " + status);
                    if(status == SUCCESSFUL) {
                        consumeApiData(response);
                    }
                }

                @Override
                public void onFailure(Call<InterestingnessResponse> call, Throwable t) {
                    Log.e(TAG, t.getMessage());
                }
            });

        }
    }


    private void consumeApiData(Response<InterestingnessResponse> response) {
        int curSize = mAdapter.getItemCount();

        InterestingnessResponse interestingnessResponse = response.body();
        FlickrPhotoList flickrPhotoList = interestingnessResponse.getPhotos();
        mFlickrPhotos.addAll(flickrPhotoList.getPhotoList());

        if(mMaxPages == -1){
            mMaxPages = flickrPhotoList.getPages();
        }

        mIsDownloadInProgress = false;
        mProgressBar.setVisibility(View.GONE);
        mAdapter.notifyItemRangeInserted(curSize, flickrPhotoList.getPhotoList().size());

    }
}
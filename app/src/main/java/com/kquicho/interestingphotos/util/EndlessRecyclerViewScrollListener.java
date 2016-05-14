package com.kquicho.interestingphotos.util;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;


/**
 * Created by kquicho on 16-05-14.
 */

public abstract class EndlessRecyclerViewScrollListener extends RecyclerView.OnScrollListener {
    private int mVisibleThreshold = 1;
    private int mCurrentPage = 1;
    private int mPreviousTotalItemCount = 0;
    private boolean mLoading = true;
    private int mStartingPageIndex = 1;

    private GridLayoutManager mGridLayoutManager;

    public EndlessRecyclerViewScrollListener(GridLayoutManager layoutManager) {
        this.mGridLayoutManager = layoutManager;
    }

    public EndlessRecyclerViewScrollListener(GridLayoutManager layoutManager, int currentPage,
                                             int previousTotalItemCount) {
        this.mGridLayoutManager = layoutManager;
        this.mCurrentPage = currentPage;
        this.mPreviousTotalItemCount = previousTotalItemCount;
        this.mLoading = false;
    }

    @Override
    public void onScrolled(RecyclerView view, int dx, int dy) {
        int lastVisibleItem = mGridLayoutManager.findLastVisibleItemPosition();
        int totalItemCount = mGridLayoutManager.getItemCount();
        if (totalItemCount < mPreviousTotalItemCount) {
            this.mCurrentPage = this.mStartingPageIndex;
            this.mPreviousTotalItemCount = totalItemCount;
            if (totalItemCount == 0) {
                this.mLoading = true;
            }
        }

        if (mLoading && (totalItemCount > mPreviousTotalItemCount)) {
            mLoading = false;
            mPreviousTotalItemCount = totalItemCount;
        }

        if (!mLoading && totalItemCount <= (lastVisibleItem + mVisibleThreshold)) {
            mCurrentPage++;
            onLoadMore(mCurrentPage, totalItemCount);
            mLoading = true;
        }
    }

    public int getCurrentPage() {
        return mCurrentPage;
    }

    public int getPreviousTotalItemCount() {
        return mPreviousTotalItemCount;
    }

    public abstract void onLoadMore(int page, int totalItemsCount);
}

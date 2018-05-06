package com.webgroup.yarik.detipapamama;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CatalogFragment extends Fragment {

    private static final String TAG = "CatalogFragment";
    private RecyclerView mCatalogRecyclerView;
    private LinearLayout mFilterBlock, mSortBlock;
    private ProgressBar mProgressBar;

    private TextView mFilterAction, mSortAction;

    private CatalogAdapter mCatalogAdapter;
    private List<Product> mItems = new ArrayList<>();
    private int page = 1;
    private boolean isLoading = false;
    private boolean isNewSearch = false;
    private ThumbnailDownloader<CatalogHolder> mThumbnailDownloader;

    private MenuItem notifyNew;
    private Sort sort;

    public static CatalogFragment newInstance(){
        return new CatalogFragment();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Settings.context = getContext();
        Settings.activity = getActivity();
        setRetainInstance(true);
        setHasOptionsMenu(true);
        updateItems();

        Handler responseHandler = new Handler();
        mThumbnailDownloader = new ThumbnailDownloader<>(responseHandler);
        mThumbnailDownloader.setThumbnailDownloadListener(
                new ThumbnailDownloader.ThumbnailDownloadListener<CatalogHolder>() {
                    @Override
                    public void onThumbnailDownloaded(CatalogHolder photoHolder,
                                                      Bitmap bitmap) {
                        Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                        photoHolder.bindDrawable(drawable);
                    }
                }
        );

        mThumbnailDownloader.start();
        mThumbnailDownloader.getLooper();
        //Log.i(TAG, "Background thread started");
    }

    public void mFilterBlockHide(){
        float xStart = 0;
        float xEnd = -mFilterBlock.getWidth();
        ObjectAnimator hide = ObjectAnimator
                .ofFloat(mFilterBlock, "x", xStart, xEnd)
                .setDuration(300);
        hide.setInterpolator(new AccelerateInterpolator());
        hide.start();
    }

    public void mSortBlockHide(){
        float xStart = Settings.getScreenSize().x / 2;
        float xEnd = Settings.getScreenSize().x;
        ObjectAnimator hide = ObjectAnimator
                .ofFloat(mSortBlock, "x", xStart, xEnd)
                .setDuration(300);
        hide.setInterpolator(new AccelerateInterpolator());
        hide.start();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_catalog_list, container,
                false);
        Filter filter = new Filter(this, v);
        sort = new Sort(this, v);

        mFilterBlock = (LinearLayout) v.findViewById(R.id.filter_block);
        mFilterBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFilterBlockHide();
            }
        });
        mFilterBlock.setMinimumWidth(Settings.getScreenSize().x / 2);
        mFilterBlock.setTranslationX(-Settings.getScreenSize().x / 2);

        mFilterAction = (TextView) v.findViewById(R.id.action_filter);
        mFilterAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float xStart = mFilterBlock.getTranslationX();
                float xEnd = 0;
                ObjectAnimator show = ObjectAnimator
                        .ofFloat(mFilterBlock, "x", xStart, xEnd)
                        .setDuration(300);
                show.setInterpolator(new AccelerateInterpolator());
                show.start();
            }
        });

        mSortBlock = (LinearLayout) v.findViewById(R.id.sort_block);
        mSortBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSortBlockHide();
            }
        });
        mSortBlock.setMinimumWidth(Settings.getScreenSize().x / 2);
        mSortBlock.setTranslationX(Settings.getScreenSize().x);

        mSortAction = (TextView) v.findViewById(R.id.action_sort);
        mSortAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float xStart = mSortBlock.getTranslationX();
                float xEnd = Settings.getScreenSize().x / 2;
                ObjectAnimator show = ObjectAnimator
                        .ofFloat(mSortBlock, "x", xStart, xEnd)
                        .setDuration(400);
                show.setInterpolator(new AccelerateInterpolator());
                show.start();
                sort.redrawSortItems();
            }
        });

        mCatalogRecyclerView = (RecyclerView) v.findViewById(R.id.catalog_recycler_view);
        mCatalogRecyclerView.setLayoutManager(new LinearLayoutManager
                (getActivity()));

        mProgressBar = (ProgressBar) v.findViewById(R.id.download_progress);

        setupAdapter();

        mCatalogRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                int visibleItemCount = mCatalogRecyclerView.getLayoutManager().getChildCount();
                int totalItemCount = mCatalogRecyclerView.getLayoutManager().getItemCount();
                int pastVisiblesItems = ((LinearLayoutManager) mCatalogRecyclerView.getLayoutManager()).findFirstVisibleItemPosition();

                if ((visibleItemCount + pastVisiblesItems) >= totalItemCount && !isLoading && !isNewSearch && mItems.size() >= 10) {
                    //Log.i(TAG, "Page " + Integer.toString(page));
                    isLoading = true;
                    page++;
                    updateItems();
                }
            }
        });

        return v;
    }


    private void showProgressBar(){
        if(mCatalogRecyclerView != null)
            mCatalogRecyclerView.setVisibility(View.INVISIBLE);

        if(mProgressBar != null)
            mProgressBar.setVisibility(View.VISIBLE);

    }

    private void hideProgressBar(){
        if(mCatalogRecyclerView != null)
            mCatalogRecyclerView.setVisibility(View.VISIBLE);

        if(mProgressBar != null)
            mProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mThumbnailDownloader.clearQueue();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mThumbnailDownloader.quit();
    }

    @Override
    public void onCreateOptionsMenu(android.view.Menu menu, android.view.MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.catalog, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_item_search);

        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                mCatalogRecyclerView.removeAllViewsInLayout();
                QueryPreferences.setStoredQuery(getActivity(), s);
                isNewSearch = true;
                reInitProductList();
                updateItems();
                return true;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = QueryPreferences.getStoredQuery(getActivity());
                searchView.setQuery(query, false);
                isNewSearch = true;
            }
        });

        notifyNew = menu.findItem(R.id.notify_new_updates);
        if (NewProductService.isServiceAlarmOn(getActivity())) {
            notifyNew.setChecked(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_clear:
                reInitProductList();
                QueryPreferences.setStoredQuery(getActivity(), null);
                updateItems();
                return true;

            case R.id.notify_new_updates:
                boolean shouldStartAlarm = !NewProductService.isServiceAlarmOn
                        (getActivity());
                NewProductService.setServiceAlarm(getActivity(), shouldStartAlarm);
                getActivity().invalidateOptionsMenu();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class CatalogHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView mItemImageView;
        private TextView mNameTextView;
        private TextView mPriceTextView;
        private TextView mOldPriceTextView;
        private Product mProduct;

        public CatalogHolder(View itemView) {
            super(itemView);
            mItemImageView = (ImageView) itemView.findViewById(R.id.img);
            mNameTextView = (TextView)itemView.findViewById(R.id.name);
            mPriceTextView = (TextView)itemView.findViewById(R.id.price);
            mOldPriceTextView = (TextView)itemView.findViewById(R.id.old_price);
            itemView.setOnClickListener(this);
        }

        public void bindProductItem(Product productItem) {
            mProduct = productItem;
        }

        public void bindDrawable(Drawable drawable) {
            mItemImageView.setImageDrawable(drawable);
        }

        @Override
        public void onClick(View v) {
            Intent intent = ProductActivity.newIntent(getContext(), mProduct.getId());
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.fade_out,R.anim.fade_out);
        }
    }

    private class CatalogAdapter extends RecyclerView.Adapter<CatalogHolder>{
        private List<Product> mProducts;

        public CatalogAdapter(List<Product> productItems) {
            mProducts = productItems;
        }

        @Override
        public CatalogHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.product_list, viewGroup, false);
            return new CatalogHolder(view);
        }

        @Override
        public void onBindViewHolder(final CatalogHolder holder, int position) {
            Product productItem = mProducts.get(position);

            holder.mNameTextView.setText(productItem.getName());
            holder.mPriceTextView.setText(productItem.getPriceFormat());

            holder.mOldPriceTextView.setText(productItem.getOldPriceFormat());
            holder.mOldPriceTextView.setPaintFlags(holder.mOldPriceTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            Drawable placeholder = getResources().getDrawable(android.R.drawable.ic_menu_camera);
            holder.bindDrawable(placeholder);
            mThumbnailDownloader.queueThumbnail(holder, productItem.getImgUrl());
            holder.bindProductItem(productItem);
        }

        @Override
        public int getItemCount() {
            return mProducts.size();
        }

        public void addProducts(List<Product> items) {
            mProducts.addAll(items);
        }
    }

    private void setupAdapter() {
        if (isAdded()) {
            if (mCatalogAdapter == null || isNewSearch) {
                mCatalogAdapter = new CatalogAdapter(mItems);
                mCatalogRecyclerView.setAdapter(mCatalogAdapter);
            } else {
                mCatalogAdapter.addProducts(mItems);
                mCatalogAdapter.notifyDataSetChanged();
            }
            isLoading = false;
            isNewSearch = false;
        }
        hideProgressBar();
    }

    public void updateItems(){
        showProgressBar();
        String query = QueryPreferences.getStoredQuery(getActivity());
        String sections = QueryPreferences.getStoredSections(getActivity());
        String sort = QueryPreferences.getSortQuery(getActivity());
        new FetchItemsTask(query,sections,sort).execute();
    }

    private class FetchItemsTask extends AsyncTask<Void,Void,List<Product>> {

        private String mQuery;
        private String mSections;
        private String mSort;

        public FetchItemsTask(String query, String sections, String sort) {
            mQuery = query;
            mSections = sections;
            mSort = sort;
            showProgressBar();
        }

        @Override
        protected List<Product> doInBackground(Void... params) {
            if (mQuery == null && mSections == null && mSort == null) {
                return new Fetchr().fetchRecentProduct(page);
            } else {
                return new Fetchr().searchProduct(mQuery,mSections,mSort,page);
            }
        }

        @Override
        protected void onPostExecute(List<Product> items) {
            mItems = items;
            setupAdapter();
        }
    }

    public void reInitProductList(){
        page = 1;
        mItems.clear();
        mCatalogAdapter = null;
    }
}

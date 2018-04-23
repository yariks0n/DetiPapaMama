package com.webgroup.yarik.detipapamama;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class CatalogFragment extends Fragment {

    private static final String TAG = "CatalogFragment";
    private RecyclerView mCatalogRecyclerView;
    private CatalogAdapter mCatalogAdapter;
    private List<Product> mItems = new ArrayList<>();
    private int page = 1;
    private boolean isLoading = false;
    private boolean isNewSearch = false;
    private ThumbnailDownloader<CatalogHolder> mThumbnailDownloader;

    public static CatalogFragment newInstance(){
        return new CatalogFragment();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        Log.i(TAG, "Background thread started");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_catalog_list, container,
                false);
        mCatalogRecyclerView = (RecyclerView) v.findViewById(R.id.catalog_recycler_view);
        mCatalogRecyclerView.setLayoutManager(new LinearLayoutManager
                (getActivity()));
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

        android.view.MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                mCatalogRecyclerView.removeAllViewsInLayout();
                //Log.d(TAG, "QueryTextSubmit: " + s);
                QueryPreferences.setStoredQuery(getActivity(), s);
                isNewSearch = true;
                reInitProductList();
                updateItems();
                return true;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                //Log.d(TAG, "QueryTextChange: " + s);
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

        MenuItem toggleItem = menu.findItem(R.id.detskie_tovary);
        if (toggleItem.isChecked()) {
            toggleItem.setChecked(true);
        } else {
            toggleItem.setChecked(false);
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

            case R.id.detskie_tovary:
                reInitProductList();
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
            Uri address = Uri.parse(mProduct.getUrl());
            Intent i = new Intent(Intent.ACTION_VIEW,address);
            startActivity(i);
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
        public void onBindViewHolder(@NonNull CatalogHolder holder, int position) {
            Product productItem = mProducts.get(position);

            holder.mNameTextView.setText(productItem.getName());
            holder.mPriceTextView.setText(productItem.getPrice()+" руб.");
            if(!productItem.getPrice().equals(productItem.getOldPrice())){
                holder.mOldPriceTextView.setText(productItem.getOldPrice()+" руб.");
                holder.mOldPriceTextView.setPaintFlags(holder.mOldPriceTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }

            holder.bindProductItem(productItem);

            Drawable placeholder = getResources().getDrawable(android.R.drawable.ic_menu_camera);
            holder.bindDrawable(placeholder);
            mThumbnailDownloader.queueThumbnail(holder, productItem.getImgUrl());
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
    }

    private void updateItems(){
        String query = QueryPreferences.getStoredQuery(getActivity());
        String sections = QueryPreferences.getStoredSections(getActivity());
        new FetchItemsTask(query,sections).execute();
    }

    private class FetchItemsTask extends AsyncTask<Void,Void,List<Product>> {

        private String mQuery;
        private String mSections;
        public FetchItemsTask(String query, String sections) {
            mQuery = query;
            mSections = sections;
        }

        @Override
        protected List<Product> doInBackground(Void... params) {
            if (mQuery == null) {
                return new Fetchr().fetchRecentProduct(page);
            } else {
                return new Fetchr().searchProduct(mQuery,mSections,page);
            }
        }

        @Override
        protected void onPostExecute(List<Product> items) {
            mItems = items;
            setupAdapter();
        }
    }

    private void reInitProductList(){
        page = 1;
        mItems.clear();
        mCatalogAdapter = null;
    }
}

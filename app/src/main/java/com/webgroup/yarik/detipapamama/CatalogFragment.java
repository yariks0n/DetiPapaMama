package com.webgroup.yarik.detipapamama;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class CatalogFragment extends Fragment {

    private RecyclerView mCrimeRecyclerView;
    private CatalogAdapter mAdapter;

    public static CatalogFragment newInstance(){
        return new CatalogFragment();
    }

    private class CatalogHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public CatalogHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onClick(View v) {

        }
    }

    private class CatalogAdapter extends RecyclerView.Adapter<CatalogHolder>{

        @NonNull
        @Override
        public CatalogHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull CatalogHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }

}

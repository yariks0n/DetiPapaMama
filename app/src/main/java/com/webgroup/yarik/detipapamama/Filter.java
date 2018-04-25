package com.webgroup.yarik.detipapamama;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Filter {

    private static final String TAG = "Filter";
    private ArrayList<CheckBox> checboxList = new ArrayList<>();
    private LinearLayout filterBlock;
    private Context mContext;
    private FilterSectionsAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private CatalogFragment mCatalogFragment;

    public Filter(CatalogFragment c, View v){
        mContext = v.getContext();
        mCatalogFragment = c;
        filterBlock = (LinearLayout) v.findViewById(R.id.filter_block);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.sections_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager
                (mContext));

        updateItems();
    }

    private class FilterSectionsHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private CheckBox mCheckBox;
        private Section mSection;

        public FilterSectionsHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.filter_section_list, parent, false));
            mCheckBox = (CheckBox) itemView.findViewById(R.id.section_checkbox);
            mCheckBox.setOnClickListener(this);
        }

        public void bind(Section section) {
            mSection = section;
            if (QueryPreferences.isStoredSectionExists(mContext,mSection.getCode())) {
                mCheckBox.setChecked(true);
            } else {
                mCheckBox.setChecked(false);
            }
        }

        @Override
        public void onClick(View v) {
            String code = mSection.getCode();
            if (!QueryPreferences.isStoredSectionExists(mContext,code)) {
                QueryPreferences.addStoredSections(mContext,code);
                mCheckBox.setChecked(true);
            }else{
                QueryPreferences.deleteFromStoredSections(mContext,code);
                mCheckBox.setChecked(false);
            }

            mCatalogFragment.reInitProductList();
            mCatalogFragment.updateItems();
            mCatalogFragment.mFilterBlockHide();
        }

    }

    private class FilterSectionsAdapter extends RecyclerView.Adapter<FilterSectionsHolder> {
        private List<Section> mSections;

        public FilterSectionsAdapter(List<Section> sections) {
            mSections = sections;
        }

        @NonNull
        @Override
        public FilterSectionsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            return new FilterSectionsHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull FilterSectionsHolder holder, int position) {
            Section section = mSections.get(position);
            holder.mCheckBox.setText(section.getName());
            holder.bind(section);
        }

        @Override
        public int getItemCount() {
            return mSections.size();
        }
    }

    private class FetchSectionsFilterTask extends AsyncTask<Void,Void,List<Section>> {

        @Override
        protected List<Section> doInBackground(Void... params) {
            return new Fetchr().fetchSectionsFilter();
        }

        @Override
        protected void onPostExecute(List<Section> items) {
            setupAdapter(items);
        }
    }

    private void setupAdapter(List<Section> sectionsArray) {
        mAdapter = new FilterSectionsAdapter(sectionsArray);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void updateItems(){
        new FetchSectionsFilterTask().execute();
    }
}

package com.webgroup.yarik.detipapamama;

import android.content.Context;
import android.preference.PreferenceManager;

import java.util.Arrays;

public class QueryPreferences {

    private static final String PREF_SEARCH_QUERY = "searchQuery";
    private static final String PREF_SEARCH_SECTIONS = "searchSEctions";
    private static final String PREF_LAST_RESULT_ID = "lastResultId";

    public static String getStoredQuery(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_SEARCH_QUERY, null);
    }
    public static void setStoredQuery(Context context, String query) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_SEARCH_QUERY, query)
                .apply();
    }
    public static String getStoredSections(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_SEARCH_SECTIONS, null);
    }

    public static String[] getStoredSectionsArray(Context context) {
        String[] sections =  PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_SEARCH_SECTIONS, null).split(",");
        return sections;
    }

    public static void setStoredSections(Context context, String query) {
        String[] sections = PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_SEARCH_SECTIONS, null).split(",");
        String[] newSections = new String[sections.length+1];
        if (!Arrays.asList(sections).contains(query)){
            newSections[sections.length+1] = query;
            String str = String.join(",", newSections);
            PreferenceManager.getDefaultSharedPreferences(context)
                    .edit()
                    .putString(PREF_SEARCH_SECTIONS, str)
                    .apply();
        }
    }

    public static String getLastResultId(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_LAST_RESULT_ID, null);
    }
    public static void setLastResultId(Context context, String lastResultId) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_LAST_RESULT_ID, lastResultId)
                .apply();
    }

}

package com.webgroup.yarik.detipapamama;

import android.content.Context;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

public class QueryPreferences {

    private static final String PREF_SEARCH_QUERY = "searchQuery";
    private static final String PREF_SEARCH_SECTIONS = "searchSections";
    private static final String PREF_SORT = "sort";
    private static final String PREF_LAST_RESULT_ID = "lastResultId";

    private static final String ORDER_NAME = "order_name";
    private static final String ORDER_EMAIL = "order_email";
    private static final String ORDER_PHONE = "order_phone";

    public static String getSortQuery(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_SORT, null);
    }
    public static void setSortQuery(Context context, String query) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_SORT, query)
                .apply();
    }

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

    public static ArrayList<String> getStoredSectionsArray(Context context) {
        String[] sections =  PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_SEARCH_SECTIONS, null).split(",");

        ArrayList<String> newArrayList = new ArrayList<>();
        for(int i = 0; i < sections.length; i++){
            newArrayList.add(sections[i]);
        }
        return newArrayList;
    }

    public static void addStoredSections(Context context, String query) {
        String strQuery = PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_SEARCH_SECTIONS, null);
        if(strQuery == null){
            PreferenceManager.getDefaultSharedPreferences(context)
                    .edit()
                    .putString(PREF_SEARCH_SECTIONS, query)
                    .apply();
        }else{
            Log.i("PREFS","query: "+query);
            Log.i("PREFS","strQuery: "+strQuery);
            if(strQuery.equals("")){
                PreferenceManager.getDefaultSharedPreferences(context)
                        .edit()
                        .putString(PREF_SEARCH_SECTIONS, query)
                        .apply();
            }else{
                String[] sections = strQuery.split(",");

                for(int i = 0; i < sections.length; i++){
                    Log.i("PREFS","sections["+i+"]: "+sections[i]);
                }

                if (!Arrays.asList(sections).contains(query)){
                    String resultString = "";
                    for(int i = 0; i < sections.length; i++){
                        resultString += sections[i]+",";
                    }
                    resultString += query;
                    PreferenceManager.getDefaultSharedPreferences(context)
                            .edit()
                            .putString(PREF_SEARCH_SECTIONS, resultString)
                            .apply();
                }
            }

        }
    }

    public static void deleteFromStoredSections(Context context, String query) {

        String strQuery = PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_SEARCH_SECTIONS, null);
        if (strQuery != null) {
            String[] sections = strQuery.split(",");
            String resultString = "";

            for(int i = 0; i < sections.length; i++){
                if(sections[i].equals(query)) continue;
                resultString += sections[i]+",";
            }

            PreferenceManager.getDefaultSharedPreferences(context)
                    .edit()
                    .putString(PREF_SEARCH_SECTIONS, resultString)
                    .apply();

        }
    }

    public static boolean isStoredSectionExists(Context context, String query) {
        String strQuery = PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_SEARCH_SECTIONS, null);

        if(strQuery == null)
            return false;

        String[] sections = strQuery.split(",");
        String[] newSections = new String[sections.length+1];
        if (Arrays.asList(sections).contains(query)){
           return true;
        } else {
           return false;
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


    public static String getOrderName(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(ORDER_NAME, null);
    }
    public static void setOrderName(Context context, String query) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(ORDER_NAME, query)
                .apply();
    }

    public static String getOrderEmail(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(ORDER_EMAIL, null);
    }
    public static void setOrderEmail(Context context, String query) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(ORDER_EMAIL, query)
                .apply();
    }

    public static String getOrderPhone(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(ORDER_PHONE, null);
    }
    public static void setOrderPhone(Context context, String query) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(ORDER_PHONE, query)
                .apply();
    }

}

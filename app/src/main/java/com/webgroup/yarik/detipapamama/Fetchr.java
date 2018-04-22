package com.webgroup.yarik.detipapamama;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Fetchr {

    private static final String TAG = "Fetchr";
    private static final String API_KEY = "522022865a9efdfeecc3accdab49bd52";

    private static final String FETCH_RECENTS_METHOD = "all";
    private static final String SEARCH_METHOD = "name";

    private static final Uri ENDPOINT = Uri
            .parse("http://detipapamama.ru/xapi/v1.0/index.php")
            .buildUpon()
            .appendQueryParameter("api_key", API_KEY)
            .build();

    public byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() +
                        ": with " +
                        urlSpec);
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    public String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    private String buildUrl(String method, String query,int page) {
        Uri.Builder uriBuilder = ENDPOINT.buildUpon().appendQueryParameter("page", Integer.toString(page));

        if (method.equals(SEARCH_METHOD)) {
            uriBuilder.appendQueryParameter("text", query);
        }

        return uriBuilder.build().toString();
    }

    public List<Product> fetchRecentProduct(int page) {
        String url = buildUrl(FETCH_RECENTS_METHOD, null, page);
        return downloadProductItems(url);
    }

    public List<Product> searchProduct(String query,int page) {
        String url = buildUrl(SEARCH_METHOD, query, page);
        return downloadProductItems(url);
    }

    private List<Product> downloadProductItems(String url){
        List<Product> items = new ArrayList<>();

        try {
            String jsonString = getUrlString(url);
            Log.i(TAG, "Received JSON: " + jsonString);
            //JSONObject jsonBody = new JSONObject(jsonString);

            JSONArray jsonArray = new JSONArray(jsonString);
            parseItems(items, jsonArray);

        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
        } catch (JSONException je){
            Log.e(TAG, "Failed to parse JSON", je);
        }

        return items;
    }

    private void parseItems(List<Product> items, JSONArray jsonArray)
            throws IOException, JSONException {

        JSONArray productJsonArray = jsonArray;
        for (int i = 0; i < productJsonArray.length(); i++) {
            JSONObject photoJsonObject = productJsonArray.getJSONObject(i);
            Product item = new Product();
            item.setName(photoJsonObject.getString("name"));
            item.setPrice(photoJsonObject.getString("price"));
            item.setOldPrice(photoJsonObject.getString("old_price"));
            item.setImgUrl(photoJsonObject.getString("img"));
            items.add(item);
        }
    }

}

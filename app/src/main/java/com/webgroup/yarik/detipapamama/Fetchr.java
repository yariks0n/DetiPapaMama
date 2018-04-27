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
    private static final String SEARCH_METHOD = "search";

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

    private String buildUrl(String method, String query, String sections, String sort, int page) {
        Uri.Builder uriBuilder = ENDPOINT.buildUpon().appendQueryParameter("page", Integer.toString(page));

        if (method.equals(SEARCH_METHOD)) {
            uriBuilder.appendQueryParameter("text", query);
            uriBuilder.appendQueryParameter("sections", sections);
            uriBuilder.appendQueryParameter("sort", sort);
        }

        return uriBuilder.build().toString();
    }

    public List<Product> fetchRecentProduct(int page) {
        String url = buildUrl(FETCH_RECENTS_METHOD, null, null,null, page);
        return downloadProductItems(url);
    }

    public List<Product> searchProduct(String query, String sections, String sort, int page) {
        String url = buildUrl(SEARCH_METHOD, query, sections, sort, page);
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

    public List<Section> fetchSectionsFilter() {
        Uri sectionsFilter = Uri
                .parse("http://detipapamama.ru/xapi/v1.0/get_sections_for_filter.php")
                .buildUpon()
                .appendQueryParameter("api_key", API_KEY)
                .build();

        String url = sectionsFilter.toString();
        return downloadSectionsFilter(url);
    }

    private List<Section> downloadSectionsFilter(String url){
        List<Section> items = new ArrayList<>();

        try {
            String jsonString = getUrlString(url);
            Log.i(TAG, "Received JSON: " + jsonString);
            //JSONObject jsonBody = new JSONObject(jsonString);

            JSONArray jsonArray = new JSONArray(jsonString);
            parseSections(items, jsonArray);

        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
        } catch (JSONException je){
            Log.e(TAG, "Failed to parse JSON", je);
        }

        return items;
    }

    public Product fetchProductDetail(String product_id) {
        Uri uri = Uri
                .parse("http://detipapamama.ru/xapi/v1.0/get_detail_product.php")
                .buildUpon()
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("product_id", product_id)
                .build();

        String url = uri.toString();
        return downloadProductDetail(url);
    }

    private Product downloadProductDetail(String url){
        Product item = new Product();

        try {
            String jsonString = getUrlString(url);
            Log.i(TAG, "Received JSON: " + jsonString);
            //JSONObject jsonBody = new JSONObject(jsonString);

            JSONArray jsonArray = new JSONArray(jsonString);
            parseProduct(item, jsonArray);

        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
        } catch (JSONException je){
            Log.e(TAG, "Failed to parse JSON", je);
        }

        return item;
    }

    private void parseItems(List<Product> items, JSONArray jsonArray)
            throws IOException, JSONException {

        JSONArray productJsonArray = jsonArray;
        for (int i = 0; i < productJsonArray.length(); i++) {
            JSONObject photoJsonObject = productJsonArray.getJSONObject(i);
            Product item = new Product();
            item.setId(photoJsonObject.getString("id"));
            item.setName(photoJsonObject.getString("name"));
            item.setPrice(photoJsonObject.getString("price"));
            if(!photoJsonObject.getString("price").equals(photoJsonObject.getString("old_price")))
                item.setOldPrice(photoJsonObject.getString("old_price"));
            item.setImgUrl(photoJsonObject.getString("img"));
            item.setUrl(photoJsonObject.getString("detail_url"));
            items.add(item);
        }
    }

    private void parseSections(List<Section> items, JSONArray jsonArray)
            throws IOException, JSONException {

        JSONArray sectionJsonArray = jsonArray;
        for (int i = 0; i < sectionJsonArray.length(); i++) {
            JSONObject sectionJsonObject = sectionJsonArray.getJSONObject(i);
            Section item = new Section();
            item.setId(sectionJsonObject.getInt("id"));
            item.setName(sectionJsonObject.getString("name"));
            item.setCode(sectionJsonObject.getString("code"));
            items.add(item);
        }
    }

    private void parseProduct(Product item, JSONArray jsonArray)
            throws IOException, JSONException {

        JSONArray productJsonArray = jsonArray;
        JSONObject productJsonData = productJsonArray.getJSONObject(0);

        Log.i(TAG,"Result "+productJsonData.getString("result"));

        if(productJsonData.getString("result").equals("1")){
            JSONObject productJsonObject = productJsonData.getJSONObject("data");
            item.setId(productJsonObject.getString("id"));
            item.setName(productJsonObject.getString("name"));
            item.setPrice(productJsonObject.getString("price"));
            if(!productJsonObject.getString("price").equals(productJsonObject.getString("old_price")))
                item.setOldPrice(productJsonObject.getString("old_price"));
            item.setImgUrl(productJsonObject.getString("img"));
            String[] morePhoto = productJsonObject.getString("more_photo").split(";");
            item.setMorePhoto(morePhoto);
        }else{

        }

    }

}

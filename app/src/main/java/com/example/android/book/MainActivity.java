package com.example.android.book;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    public String placeholder;
    public String bookUrl =
            "https://www.googleapis.com/books/v1/volumes?q="+placeholder+"&maxResults=50";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void updateUi(List<BookList> names) {
        ListView bListView = (ListView) findViewById(R.id.list);
        BookAdapter adapter = new BookAdapter(this, names);
        bListView.setAdapter(adapter);

    }

    private class BookAsyncTask extends AsyncTask<String, Void, List<BookList>> {
        @Override
        protected List<BookList> doInBackground(String... urls) {
            URL url = createUrl(bookUrl);

            String jsonResponse = "";
            try{
                jsonResponse = makeHttpRequest(url);
            }catch (IOException e) {

            }

            List<BookList> listlist = extractFeatureFromJson(jsonResponse);

            return listlist;
        }
        @Override
        protected void onPostExecute(List<BookList> abcd) {
            if (abcd == null) {
                return;
            }

            updateUi(abcd);
        }


        private URL createUrl(String stringUrl) {
            URL url = null;
            try {
                url = new URL(stringUrl);
            } catch (MalformedURLException e) {
                //Log.e(LOG_TAG, "Problem building the URL ", e);
            }
            return url;
        }

        private String makeHttpRequest(URL url) throws IOException {
            String jsonResponse = "";

            if (url == null)
            {
                return jsonResponse;
            }

            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                //urlConnection.setReadTimeout(10000 /* milliseconds */);
                //urlConnection.setConnectTimeout(15000 /* milliseconds */);
                urlConnection.connect();

                if(urlConnection.getResponseCode() == 200) {
                    inputStream = urlConnection.getInputStream();
                    jsonResponse = readFromStream(inputStream);
                }

            } catch (IOException e) {
                // TODO: Handle the exception
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    // function must handle java.io.IOException here
                    inputStream.close();
                }
            }
            return jsonResponse;
        }

        private String readFromStream(InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            }
            return output.toString();
        }

        private List<BookList> extractFeatureFromJson(String earthquakeJSON) {
            ArrayList<BookList> bookl = new ArrayList<>();

            try {
                    JSONObject base = new JSONObject(earthquakeJSON);
                    JSONArray itemArray = base.getJSONArray("items");
                for (int i = 0; i < itemArray.length(); i++) {
                    JSONObject current = itemArray.getJSONObject(i);
                    JSONObject properties = current.getJSONObject("volumeInfo");
                    String name = properties.getString("title");
                    bookl.add(new BookList(name));
                }

            } catch (JSONException e) {
                //Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
            }
            return bookl;
        }

    }

    public void doSomething(View view){
        TextView text = (TextView) findViewById(R.id.editText);
        String result = text.getText().toString();
        placeholder = result;
        bookUrl = "https://www.googleapis.com/books/v1/volumes?q="+placeholder+"&maxResults=40";
        BookAsyncTask task = new BookAsyncTask();
        task.execute();
        placeholder = "";
    }
}

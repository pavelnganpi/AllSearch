package com.paveynganpi.allsearch;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;


public class ImageGrid extends ActionBarActivity {

    private static final String TAG = ImageGrid.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_grid);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_image_grid, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        protected GridView mGridView;//reference to gridview in fragment_image_grid.xml
        protected int start = 0;//variable to change pages from google Api

        //contains extra images urls to supply to ... when need
        protected ArrayList<String> mBigImagesUrls;
        //contains image urls to inject into gridview
        protected  static ArrayList<String> mSmalImagesUrls = new ArrayList<String>();

        protected static String mEditedString;


        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_image_grid, container, false);

            final ArrayList<String> testList = new ArrayList<String>();

            //gets the edited string from MainActivity
            Bundle args = getActivity().getIntent().getExtras();
            mEditedString = args.getString("space");

            String imagesUrl = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q="
                    + mEditedString +"&rsz=8&start=" + start;


            //populate(start,imagesUrl.substring(0,imagesUrl.length()-1));

            if(isNetworkAvailable()) {

                //using okHttp library to connect to imagesUrl and retrieve JSON Data
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(imagesUrl).
                                build();

                Call call = client.newCall(request);

                //runs the below code asynchronously
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        Log.v(TAG, "error from request");

                    }

                    @Override
                    public void onResponse(Response response) throws IOException {

                        try {
                            String jsonData = response.body().string();
                            //Log.v(TAG, jsonData);

                            if (!response.isSuccessful()) {
                                alertUserAboutError();
                            } else {

                                mSmalImagesUrls = getCurrentDetails(jsonData);

                                getActivity().runOnUiThread(new Runnable(){
                                    @Override
                                    public void run(){
                                        mGridView =(GridView) rootView.findViewById(R.id.imagesGrid);//reference to gridview
                                        ImagesGridAdapter adapter = new ImagesGridAdapter(getActivity(),mSmalImagesUrls);
                                        mGridView.setAdapter(adapter);
                                    }
                                });


                            }
                        } catch (IOException | JSONException e) {
                            Log.e(TAG, "Exception caught :", e);
                        }
                    }
                });
            }
            else{
                Toast.makeText(getActivity(), "Network is unavailable", Toast.LENGTH_LONG).show();
            }

            return rootView;
        }

        //get data
        private ArrayList<String> getCurrentDetails(String jsonData) throws JSONException {

            JSONObject jsonObject = new JSONObject(jsonData);
            JSONObject responseData = jsonObject.getJSONObject("responseData");

            ArrayList<String> localList = new ArrayList<String>();

            JSONArray results = responseData.getJSONArray("results");


            for(int i =0;i<results.length(); i++){

                localList.add(results.getJSONObject(i).getString("url"));

            }

            return localList;

        }
//
//        //fill in big list with 100 image urls
//        private void populate(int start,String imagesUrl) {
//
//           for(int i =start;i<100+start;i++) {
//
//               imagesUrl = imagesUrl + i;
//
//               if (isNetworkAvailable()) {
//                   OkHttpClient client = new OkHttpClient();
//                   Request request = new Request.Builder()
//                           .url(imagesUrl).
//                                   build();
//
//                   Call call = client.newCall(request);
//
//                   call.enqueue(new Callback() {
//                       @Override
//                       public void onFailure(Request request, IOException e) {
//
//                       }
//
//                       @Override
//                       public void onResponse(Response response) throws IOException {
//
//                           try {
//                               String jsonData = response.body().string();
//                               //Log.v(TAG, jsonData);
//
//                               if (!response.isSuccessful()) {
//                                   alertUserAboutError();
//                               } else {
//
//                                   try {
//                                       JSONObject jsonObject = new JSONObject(jsonData);
//                                       JSONObject responseData = jsonObject.getJSONObject("responseData");
//                                       JSONArray results = responseData.getJSONArray("results");
//
//                                       for (int index = 0; index < results.length(); index++) {
//
//                                           mBigImagesUrls.add(results.getJSONObject(index).getString("url"));
//
//                                       }
//
//                                   } catch (JSONException e) {
//                                       e.printStackTrace();
//                                   }
//
//                               }
//                           } catch (IOException e) {
//                               Log.e(TAG, "Exception caught :", e);
//                           }
//                       }
//
//                   });
//               }
//           }
//
//           }

        //An AlertDialog to display to user when an error occurs
        private void alertUserAboutError() {

            AlertDialogFragment dialog = new AlertDialogFragment();
            dialog.show(getActivity().getFragmentManager(),"error_dialog");


        }

        //checks if user is connected to a network
        private boolean isNetworkAvailable() {

            ConnectivityManager cm =
                    (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isAvailable = false;

            if(activeNetwork != null && activeNetwork.isConnectedOrConnecting()){
                isAvailable = true;
            }
            return isAvailable;


        }

    }
}

package com.shopmetrixosa.json;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Product> productList = new ArrayList<>();
    ProductAdapter mAdapter;
    FloatingActionButton fabAdd;
    String serverUrl = ("http://192.168.1.32/json/insertdata.php");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        fabAdd = findViewById(R.id.fabAdd);

        fabAdd = null;
        
        
        mAdapter = new ProductAdapter(productList);
        RecyclerView.LayoutManager mLaypouttManger = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLaypouttManger);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        getJSON("http://192.168.1.32/json/getdata.php");
        
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddDialog();
;            }


        });

    }

    private void showAddDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
        View view = layoutInflater.inflate(R.layout.dialog_insert, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilderUserInput.setView(view);

        final EditText addName = view.findViewById(R.id.addName);
        final EditText addPrice = view.findViewById(R.id.addPrice);
        final EditText addUom = view.findViewById(R.id.addUom);

        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Add Product", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String name = addName.getText().toString();
                        String price = addPrice.getText().toString();
                        String uom = addUom.getText().toString();
                        insertData(name, price, uom);

                    }
                })

                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();

    }

    public void insertData(final String name, final String price, final String uom){

        class SendPostReqAsyncTask extends  AsyncTask<String, Void, String>{

            @Override
            protected String doInBackground(String... strings) {
                String nameHolder = name;
                String priceHolder = price;
                String uomHolder = uom;

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("name", nameHolder));
                nameValuePairs.add(new BasicNameValuePair("price", priceHolder));
                nameValuePairs.add(new BasicNameValuePair("uom", uomHolder));

                try {
                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(serverUrl);

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse httpResponse = httpClient.execute(httpPost);

                    HttpEntity httpEntity = httpResponse.getEntity();


                } catch (ClientProtocolException e) {

                } catch (IOException e) {

                }
                return "Data Inserted Successfully";
            }

            @Override
            protected void onPostExecute(String result){
                super.onPostExecute(result);

                productList.clear();
                getJSON("http://192.168.1.32/json/getdata.php");

                Toast.makeText(MainActivity.this, "Submit Successfully", Toast.LENGTH_SHORT).show();
            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(name, price, uom);
    }

    private void getJSON(final String urlWebService){

        class GetJSON extends AsyncTask<Void, Void, String>{

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                try {
                    loadIntoRecyclerView(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
               try{
                   URL url = new URL(urlWebService);

                   HttpURLConnection con = (HttpURLConnection) url.openConnection();

                   StringBuilder sb = new StringBuilder();

                   BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                   String json;

                   while ((json = bufferedReader.readLine()) != null){
                       sb.append(json + "\n");
                   }

                   return sb.toString().trim();
               }catch (Exception e){
                   return null;
               }
            }
        }

        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }


    private void loadIntoRecyclerView(String json)throws JSONException{
        JSONArray jsonArray = new JSONArray(json);

        /*productName = new String[jsonArray.length()];
        productPrice = new String[jsonArray.length()];
        productUom = new String[jsonArray.length()];*/


        for(int i=0; i < jsonArray.length(); i++){
            JSONObject obj = jsonArray.getJSONObject(i);

            Product product = new Product(obj.getString("name"), obj.getString("price"), obj.getString("uom"));
            productList.add(product);

            mAdapter.notifyDataSetChanged();

        }
    }
}

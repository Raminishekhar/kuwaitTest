package quadrant.sampletest;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import quadrant.sampletest.adapter.CartAdapter;
import quadrant.sampletest.array.CartModel;

public class Cart extends Activity {

    RecyclerView cartRecycler;
    CartAdapter cartAdapter;
    List<CartModel> cartlist = new ArrayList<>();
    int id;
    String phone;


    public void  action(String name){

        ActionBar mActionBar = getActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(true);
        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.actoinbar, null);
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.action_title);
        mTitleTextView.setText(name);


        ImageView iv=(ImageView)mCustomView.findViewById(R.id.action_image);

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ImageView add=(ImageView)mCustomView.findViewById(R.id.add);



        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(Cart.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.dialog);



               final EditText edit = (EditText) dialog.findViewById(R.id.mEtname);

                Button dialogButton = (Button) dialog.findViewById(R.id.button);
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {



                        String name=edit.getText().toString();
                        new AddCartTask().execute("https://cart.coderswebsites.com/carts/",name,""+id);
                        dialog.dismiss();
                    }
                });

                dialog.show();


            }
        });


        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        Bundle b=getIntent().getExtras();
        if (b!=null){
            id=b.getInt("id");
            phone=b.getString("phone");
        }

            action("Carts");
        cartRecycler = (RecyclerView) findViewById(R.id.recycler_view);

        cartAdapter = new CartAdapter(cartlist);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        cartRecycler.setLayoutManager(mLayoutManager);
        cartRecycler.setItemAnimator(new DefaultItemAnimator());
        cartRecycler.setAdapter(cartAdapter);

        new getCartTask().execute("https://cart.coderswebsites.com/members/"+id+"/carts/?page=1");

    }





    class getCartTask extends AsyncTask<String, Integer, String> {
        private ProgressDialog pdia;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdia = new ProgressDialog(Cart.this);
            pdia.setMessage("Loading...");
            pdia.setCanceledOnTouchOutside(false);
            pdia.show();
        }

        @Override
        protected String doInBackground(String... values) {

            String s="";
            try
            {
                HttpClient httpClient=new DefaultHttpClient();
                HttpGet httpPost=new HttpGet(values[0]);
                String base64EncodedCredentials = "Basic " + Base64.encodeToString(
                        ("abstract" + ":" + "anonymous").getBytes(),
                        Base64.NO_WRAP);
                httpPost.setHeader("Authorization", base64EncodedCredentials);
                /*List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("name", values[1]));
                nameValuePairs.add(new BasicNameValuePair("ownerId", values[2]));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
*/
                HttpResponse httpResponse=  httpClient.execute(httpPost);
                HttpEntity httpEntity=httpResponse.getEntity();
                s= readResponse(httpResponse);



            }
            catch(Exception e)
            {
                Log.e("Error","In Catch  "+e);
            }
            return s;

        }

        @Override
        protected void onPostExecute(String result) {
            pdia.dismiss();

          //  Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();

            Log.e("response", "" + result);
            try {
                JSONObject jobjmain = new JSONObject(result);

                if (jobjmain.has("count")) {

                 //   String phone=jobjmain.getString("phone");

                    JSONArray jarray=jobjmain.getJSONArray("results");
                    cartlist.clear();
                    for (int i=0;i<jarray.length();i++){

                        JSONObject innerjObj=jarray.getJSONObject(i);

                        String name=innerjObj.getString("name");
                        int id1=innerjObj.getInt("id");
                        CartModel cm=new CartModel(name,id1);

                        cartlist.add(cm);

                        cartAdapter = new CartAdapter(cartlist);


                        cartRecycler.setAdapter(cartAdapter);


                    }

                }else{
                    Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_LONG).show();
                }


            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("Error", "In cpost catch " + e);
            }


        }
    }


    class AddCartTask extends AsyncTask<String, Integer, String> {
        private ProgressDialog pdia;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdia = new ProgressDialog(Cart.this);
            pdia.setMessage("Loading...");
            pdia.setCanceledOnTouchOutside(false);
            pdia.show();
        }

        @Override
        protected String doInBackground(String... values) {

            String s="";
            try
            {
                HttpClient httpClient=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost(values[0]);
                String base64EncodedCredentials = "Basic " + Base64.encodeToString(
                        ("abstract" + ":" + "anonymous").getBytes(),
                        Base64.NO_WRAP);
                httpPost.setHeader("Authorization", base64EncodedCredentials);
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("name", values[1]));
                nameValuePairs.add(new BasicNameValuePair("ownerId", values[2]));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));



                HttpResponse httpResponse=  httpClient.execute(httpPost);

                HttpEntity httpEntity=httpResponse.getEntity();
                s= readResponse(httpResponse);



            }
            catch(Exception e)
            {
                Log.e("Error","In Catch  "+e);
            }
            return s;

        }

        @Override
        protected void onPostExecute(String result) {
            pdia.dismiss();

      //      Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();

            Log.e("response", "" + result);
            try {
                JSONObject jobjmain = new JSONObject(result);


                new getCartTask().execute("https://cart.coderswebsites.com/members/"+id+"/carts/?page=1");

                if (jobjmain.has("id")) {
                    int id = jobjmain.getInt("id");
                    //   String phone=jobjmain.getString("phone");

                    JSONArray jarray=jobjmain.getJSONArray("items");

                    /*for (int i=0;i<jarray.length();i++){


                    }*/

                }else{
                    Toast.makeText(getApplicationContext(),"No service in this country",Toast.LENGTH_LONG).show();
                }


            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("Error", "In cpost catch " + e);
            }


        }
    }
    public String readResponse(HttpResponse res)
    {
        InputStream is=null;
        String return_text="";
        try {
            is=res.getEntity().getContent();
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(is,"UTF-8"));
            String line="";
            StringBuffer sb=new StringBuffer();
            while ((line=bufferedReader.readLine())!=null)
            {
                sb.append(line);
            }
            return_text=sb.toString();
        } catch (Exception e)
        {

        }
        return return_text;

    }


}

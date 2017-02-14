package quadrant.sampletest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
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

public class Register extends AppCompatActivity {


    EditText mEtCountryCode,mEtPhoneNo;

    Button mBtRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEtCountryCode=(EditText)findViewById(R.id.mEtcountry);
        mEtPhoneNo=(EditText)findViewById(R.id.mEtphoneno);
        mBtRegister=(Button)findViewById(R.id.mBtregister);

        mBtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             String country_code=   mEtCountryCode.getText().toString();
                String phone_number=   mEtPhoneNo.getText().toString();

               // if (phone_number.length()==10){

                    new ExecuteTask().execute("https://cart.coderswebsites.com/registration/",country_code+phone_number);
                //}else {

                 //   Toast.makeText(getApplicationContext(),"Please enter a valid number",Toast.LENGTH_LONG).show();
               // }
            }
        });



    }
    class ExecuteTask extends AsyncTask<String, Integer, String> {
        private ProgressDialog pdia;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdia = new ProgressDialog(Register.this);
            pdia.setMessage("Loading...");
            pdia.setCanceledOnTouchOutside(false);
            pdia.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String res = PostData(params);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            pdia.dismiss();

         //    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
/*{
    "id": 96,
    "name": null,
    "phone": " 96599492806",
    "dateOfBirth": null,
    "email": null,
    "image": null,
    "gender": "M",
    "thumbnail": null
}*/

            Log.e("response", "" + result);
            try {
                JSONObject jobjmain = new JSONObject(result);

                if (jobjmain.has("id")) {
                    int id = jobjmain.getInt("id");
                    String phone=jobjmain.getString("phone");

                    startActivity(new Intent(Register.this,Verificaton.class).putExtra("id",id).putExtra("phone",phone));

                }else{
                    Toast.makeText(getApplicationContext(),"No service in this country",Toast.LENGTH_LONG).show();
                }


            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("Error", "In cpost catch " + e);
            }


        }
    }

    public String PostData(String[] values)
    {
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
            nameValuePairs.add(new BasicNameValuePair("phone", values[1]));
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

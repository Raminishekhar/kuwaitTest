package quadrant.sampletest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Verificaton extends AppCompatActivity {


    int id;
    String phone;

    TextView phone_number;
    EditText mEtverify;

    Button mBtResend,mBtVerify,mBtEditnumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verificaton);

        Bundle b=getIntent().getExtras();
        if (b!=null){
            id=b.getInt("id");
            phone=b.getString("phone");
        }
        mBtResend=(Button)findViewById(R.id.mBtResend);
        mBtVerify=(Button)findViewById(R.id.mBtVerify);
        mBtEditnumber=(Button)findViewById(R.id.mbtEditNumber);
        phone_number=(TextView)findViewById(R.id.mTxtphone_number);
        mEtverify=(EditText)findViewById(R.id.mEtVerify);

        phone_number.setText(phone);


        mBtEditnumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        mBtVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(Verificaton.this,Cart.class).putExtra("id",id).putExtra("phone",phone));
            }
        });
        mBtResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Code sent to your number",Toast.LENGTH_LONG).show();
            }
        });
    }
}

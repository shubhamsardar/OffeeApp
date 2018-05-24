package in.co.tripin.offeeuser.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import in.co.tripin.offeeuser.Managers.SharedPrefManager;
import in.co.tripin.offeeuser.R;

public class Settings extends AppCompatActivity {

    EditText editTextHpass;
    Button mButtonHpass;
    private SharedPrefManager mSharedFref;
    private Context mContext;
    private TextView mTxtScanQr;
    private IntentIntegrator qrScan;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setTitle("Offee- Settings");
        initView();
        setListners();
        mContext = this;
        mSharedFref = new SharedPrefManager(mContext);

        //intializing scan object
        qrScan = new IntentIntegrator(this);

    }



    private void initView() {
        editTextHpass = (EditText)findViewById(R.id.editText_ReHpass);
        mButtonHpass = (Button)findViewById(R.id.buttonHpass);
        mTxtScanQr = (TextView)findViewById(R.id.textViewScanqr2);
    }
    private void setListners() {

        mButtonHpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!editTextHpass.getText().toString().isEmpty()){
                    mSharedFref.setUserHpass(editTextHpass.getText().toString());
                    Toast.makeText(mContext,"Password Set!",Toast.LENGTH_LONG).show();
                    finish();

                }else {
                    Toast.makeText(mContext,"Bad Password!",Toast.LENGTH_LONG).show();
                }



            }
        });

        mTxtScanQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qrScan.initiateScan();
            }
        });
    }

    //Getting the scan results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                try {
                    //converting the data to json
                    JSONObject obj = new JSONObject(result.getContents());
                    //setting values to textviews
                    if(obj.has("pass")){
                        mSharedFref.setUserHpass(obj.getString("pass"));
                        Toast.makeText(getApplicationContext(),"Scan Success",Toast.LENGTH_LONG).show();
                        Toast.makeText(mContext,"Password Set!",Toast.LENGTH_LONG).show();
                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    //if control comes here
                    //that means the encoded format not matches
                    //in this case you can display whatever data is available on the qrcode
                    //to a toast
                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}

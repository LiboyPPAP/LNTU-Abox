package com.dhee.abox;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.dhc.absdk.ABRet;
import com.dhc.absdk.ABSDK;

public class HumanActivity extends AppCompatActivity {

    ABSDK absdk;
    ABRet response;
    TextView hs, ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_human);

        hs = findViewById(R.id.HState);
        ll = findViewById(R.id.light);

        absdk = ABSDK.getInstance();

        new GetHdStatusAsyncTask().execute();
    }


    private class GetHdStatusAsyncTask extends AsyncTask<Void, Void, ABRet> {

        @Override
        protected ABRet doInBackground(Void... params) {
            return absdk.getHdStatus("人体活动传感器");
        }

        @Override
        protected void onPostExecute(ABRet response) {
            if (response != null) {
                if (response.getCode().equals("00000")) {
                    String HS = response.getDicDatas().get("status").toString();
                    String LL = response.getDicDatas().get("lightIntensity").toString();
                    if (HS.equals("1")) {
                        hs.setText("Active State: Yes");

                    } else {
                        hs.setText("Active State: No");
                    }

                    switch (LL) {
                        case "1":
                            ll.setText("lightIntensity: Light");
                            break;
                        case "2":
                            ll.setText("lightIntensity: Normal");
                            break;
                        case "3":
                            ll.setText("lightIntensity: Dusky");
                            break;
                        case "4":
                            ll.setText("lightIntensity: Dark");
                            break;
                        default:
                            ll.setText("lightIntensity: Unknown");
                            break;
                    }
                } else {
                    Toast.makeText(HumanActivity.this, "Error Code: " + response.getCode(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}

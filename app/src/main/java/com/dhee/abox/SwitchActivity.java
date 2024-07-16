package com.dhee.abox;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.dhc.absdk.ABRet;
import com.dhc.absdk.ABSDK;
import com.dhee.abox.R;

public class SwitchActivity extends AppCompatActivity {

    ImageView switch_state_turn,open_State;
    boolean state;
    ABSDK absdk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch);
        switch_state_turn = findViewById(R.id.Switch_State_turn);
        open_State = findViewById(R.id.open_State);
        absdk = ABSDK.getInstance();
        new GetSocketStatusTask().execute();
    }

    private class GetSocketStatusTask extends AsyncTask<Void, Void, ABRet> {

        protected ABRet doInBackground(Void... voids) {
            String socketDeviceName = "Zigbee插座控制器";
            return absdk.getSockStatus(socketDeviceName);
        }
        protected void onPostExecute(ABRet response) {
            if (response.getCode().equals("00000")) {
                String status = response.getDicDatas().get("status").toString();
                if (status.equals("1")) {
                    switch_state_turn.setImageResource(R.drawable.icon_switch_on1);
                    open_State.setImageResource(R.drawable.open);
                    state = true;

                } else {
                    switch_state_turn.setImageResource(R.drawable.icon_switch_off1);
                    open_State.setImageResource(0);
                    state = false;
                }
            } else {
                Toast.makeText(SwitchActivity.this, "Get Status Error Code:" + response.getCode(), Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void turn_switch(View view) {

        new ToggleSocketTask().execute();
    }

    private class ToggleSocketTask extends AsyncTask<Void, Void, ABRet> {

        @Override
        protected ABRet doInBackground(Void... voids) {
            String socketDeviceName = "Zigbee插座控制器";
            String controlValue = state ? "0" : "1";
            return absdk.sockCtrl(socketDeviceName, controlValue);
        }

        @Override
        protected void onPostExecute(ABRet response) {
            if (response.getCode().equals("00000")) {
                if (!state) {
                    switch_state_turn.setImageResource(R.drawable.icon_switch_on1);
                    open_State.setImageResource(R.drawable.open);
                }
                else {
                    switch_state_turn.setImageResource(R.drawable.icon_switch_off1);
                    open_State.setImageResource(0);
                }
                state = !state;
            }
            else {
                if (state) {
                    Toast.makeText(SwitchActivity.this, "Opened Error Code:" + response.getCode(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SwitchActivity.this, "Closed Error Code:" + response.getCode(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
package com.dhee.abox;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.dhc.absdk.ABRet;
import com.dhc.absdk.ABSDK;

public class LoginActivity extends AppCompatActivity {
    Button login;
    EditText loginna, loginpw;
    CheckBox eulacheck;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = findViewById(R.id.loginBT);
        loginna = findViewById(R.id.loginNA);
        loginpw = findViewById(R.id.loginPW);
        eulacheck = findViewById(R.id.EULACheck);

    }

    public void loginBT_onClick(View view) {
        String username = loginna.getText().toString(), password = loginpw.getText().toString();
        new LoginTask().execute(username,password);
    }

    class LoginTask extends AsyncTask<String,Void,ABRet>{
        protected  ABRet doInBackground(String... strings){
            ABRet abRet = ABSDK.getInstance().loginWithUsername(strings[0],strings[1]);
            return abRet;
        }
        protected void onPostExecute (ABRet abRet){
            super.onPostExecute(abRet);
            if (!eulacheck.isChecked()) {
                Toast.makeText(LoginActivity.this, "Please agree to the User License Agreement.", Toast.LENGTH_SHORT).show();
            }
            else if (!("00000".equals(abRet.getCode()))){

                Toast.makeText(LoginActivity.this,"Error Code : "+abRet.getCode(),Toast.LENGTH_LONG).show();
            }
                else {

                Toast.makeText(LoginActivity.this,"Login Successful!",Toast.LENGTH_LONG).show();
                Intent i = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(i);

            }

        }
    }
}
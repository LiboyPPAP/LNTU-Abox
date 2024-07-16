package com.dhee.abox;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.dhc.absdk.ABRet;
import com.dhc.absdk.ABSDK;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class Get_tem_Activity2 extends AppCompatActivity {

    Button refresh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_tem2);
        refresh = findViewById(R.id.btnREStatus);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ThStatusTask().execute();
            }


        });
    }
    public void clear(View view) {
        DBhelper dbHelper = new DBhelper(Get_tem_Activity2.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Delete all rows from the table
        db.execSQL("DELETE FROM " + DBhelper.TABLE_NAME);

        // Close the database connection
        db.close();

        // Clear the ListView by setting an empty adapter
        ListView listView = findViewById(R.id.lv);
        listView.setAdapter(null);

        // Show a toast message indicating successful database clearance
        Toast.makeText(Get_tem_Activity2.this, "Database Cleared!", Toast.LENGTH_SHORT).show();
    }




    class ThStatusTask extends AsyncTask<String, Void, ABRet> {

        // 子线程中通过网络获取温湿度（对象）
        @Override
        protected ABRet doInBackground(String... strings) {
            ABRet abRet = ABSDK.getInstance().getThStatus("温湿度传感器");
            return abRet;
        }

        // 在主线程中获取子线程的返回值
        protected void reFresh(ABRet abRet){
            super.onPostExecute(abRet);
            String Temperature = abRet.getDicDatas().get("temperature")+"℃";
            String Humidity = abRet.getDicDatas().get("humidity")+"%";
            String Times = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
            Toast.makeText(Get_tem_Activity2.this, "Refresh Successful!", Toast.LENGTH_SHORT).show();

            DBhelper dbHelper =new DBhelper(Get_tem_Activity2.this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            //Insert data
            db.execSQL("INSERT INTO " + DBhelper.TABLE_NAME + " (" +
                    DBhelper.COLUMN_TIMES + ", " +
                    DBhelper.COLUMN_TEMPERATURE + ", " +
                    DBhelper.COLUMN_HUMIDITY +
                    ") VALUES ('" + Times + "', '" + Temperature + "', '" + Humidity + "')");

            // 查询数据库中的内容
            Cursor cursor = db.rawQuery("SELECT * FROM " + DBhelper.TABLE_NAME, null);

            // 创建一个ArrayList用于存储每个列表项的数据
            ArrayList<HashMap<String, String>> listItems = new ArrayList<>();

            // 遍历Cursor，将数据库中的每一行数据添加到ArrayList中
            while (cursor.moveToNext()) {
                HashMap<String, String> item = new HashMap<>();
                String times = cursor.getString(cursor.getColumnIndex(DBhelper.COLUMN_TIMES));
                String temperature = cursor.getString(cursor.getColumnIndex(DBhelper.COLUMN_TEMPERATURE));
                String humidity = cursor.getString(cursor.getColumnIndex(DBhelper.COLUMN_HUMIDITY));

                item.put("times", times);
                item.put("temperature", temperature);
                item.put("humidity", humidity);
                listItems.add(item);
            }

            // 创建SimpleAdapter，用于将ArrayList中的数据显示在ListView中
            SimpleAdapter adapter = new SimpleAdapter(Get_tem_Activity2.this, listItems,
                    R.layout.layout_lv,
                    new String[]{"times", "temperature", "humidity"},
                    new int[]{R.id.times, R.id.Temper, R.id.Humidi});

            // 将适配器设置到ListView中
            ListView listView = findViewById(R.id.lv);
            listView.setAdapter(adapter);

            // 关闭Cursor和数据库连接
            cursor.close();
            db.close();

        }

        @Override
        protected void onPostExecute(ABRet abRet) {
            super.onPostExecute(abRet);
            if ("00000".equals(abRet.getCode())) {
                ThStatusTask Re = new ThStatusTask();
                Re.reFresh(abRet);

            }
            else {
                Toast.makeText(Get_tem_Activity2.this, "Refresh Failed! " +abRet.getCode(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
package com.example.kfujieda.ocr2;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView pharmacy;
    private TextView date;
    private TextView name;
    private TextView medicine;
    private TextView bun;
    private Button button;
    private Button button2;
    private ProgressDialog progressDialog;
    //Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pharmacy = findViewById(R.id.textPharmacy);
        date = findViewById(R.id.textDate);
        name = findViewById(R.id.textName);
        medicine = findViewById(R.id.textMedicine);
        bun = findViewById(R.id.textBun);

        button = findViewById(R.id.button);
        button.setOnClickListener(this);
        button2 = findViewById(R.id.button2);
        button2.setOnClickListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        URL url = null;
        int i = view.getId();
        if(i == R.id.button) {
            try {
                url = new URL("http://35.221.123.237:3180/?f?f=IMG_0075_.jpg&u=aaa/IMG_0075_.jpg");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        else if(i == R.id.button2) {
            pharmacy.setText("");
            date.setText("");
            name.setText("");
            medicine.setText("");
            bun.setText("");
            return;
        }

        HttpGetTask httpGetTask = new HttpGetTask(new HttpGetTask.AsyncCallback() {
            public void setActivity(Activity activity) {
                this.getActivity();// = activity;
            }

            public Activity getActivity() {
                return this.getActivity();
            }

            // 実行前
            public void preExecute() {
            }

            // 実行後
            public void postExecute(JSONArray jsonArray) {
                if (jsonArray == null) {
                    //showLoadError(); // エラーメッセージを表示
                    return;
                }
                try {
                    // プログレスダイアログを表示する
                    progressDialog = new ProgressDialog(getApplicationContext());
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Loading...");
                    progressDialog.show();

                    Map<String,String> result = new HashMap<String,String>();
                    JSONObject jsonObject = (JSONObject)jsonArray.toJSONObject(jsonArray);
                    StringBuilder stringBuilder = new StringBuilder();

                    int loop = 0;
                    while (loop < jsonObject.length()){
                        JSONObject object= (JSONObject)jsonArray.get(loop);
                        if(!object.has("medicine")){
                            result.put("Medicine",new String(stringBuilder));
                            medicine.setText(result.get("Medicine"));
                            break;
                        }
                        stringBuilder.append(object.get("medicine"));
                        stringBuilder.append("  ");
                        stringBuilder.append(object.get("unit"));
                        stringBuilder.append("\n");
                        loop++;
                    }

                    for(int i = loop; i < jsonObject.length(); i++) {
                        JSONObject object = (JSONObject)jsonArray.get(i);
                        if(object.has("date")){
                            result.put("Date", (String)object.get("date"));
                            date.setText(result.get("Date"));
                            continue;
                        }
                        if(object.has("pharmacy")){
                            result.put("Pharmacy", (String)object.get("pharmacy"));
                            pharmacy.setText(result.get("Pharmacy"));
                            continue;
                        }
                        if(object.has("name")){
                            result.put("Name", (String)object.get("name"));
                            name.setText(result.get("Name"));
                            continue;
                        }
                        if(object.has("bun")){
                            result.put("Bun", (String)object.get("bun"));
                            bun.setText(result.get("Bun"));
                            continue;
                        }
                        if(object.has("address")){
                            result.put("Address", (String)object.get("address"));
                            //bun.setText(result.get("Address"));
                            continue;
                        }
                        if(object.has("tel")){
                            result.put("Tel", (String)object.get("tel"));
                            //bun.setText(result.get("Tel"));
                            continue;
                        }
                        if(object.has("fax")){
                            result.put("Fax", (String)object.get("fax"));
                            //bun.setText(result.get("Fax"));
                            continue;
                        }
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }

            // 実行中
            public void progressUpdate(int progress) {
            }

            // キャンセル
            public void cancel() {
            }
        });
        // 処理を実行
        httpGetTask.execute(url);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}

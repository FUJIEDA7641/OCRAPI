package com.example.kfujieda.ocr2;

import android.os.AsyncTask;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

class HttpGetTask extends AsyncTask<URL, Integer, JSONArray> {

    private AsyncCallback asyncCallback = null;

    public HttpGetTask(AsyncCallback _asyncCallback) {
        asyncCallback = _asyncCallback;
    }

    public interface AsyncCallback {
        void preExecute();
        void postExecute(JSONArray result);
        void progressUpdate(int progress);
        void cancel();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        asyncCallback.preExecute();
    }

    @Override
    protected void onProgressUpdate(Integer... _progress) {
        super.onProgressUpdate(_progress);
        asyncCallback.progressUpdate(_progress[0]);
    }

    @Override
    protected void onPostExecute(JSONArray _result) {
        super.onPostExecute(_result);
        asyncCallback.postExecute(_result);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        asyncCallback.cancel();
    }

    @Override
    protected JSONArray doInBackground(URL... url) {
        HttpURLConnection httpURLConnection = null;
        try {
            httpURLConnection = (HttpURLConnection) url[0].openConnection();
            httpURLConnection.setReadTimeout(100000);
            httpURLConnection.setConnectTimeout(600000);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setDoInput(true);
            httpURLConnection.connect();

            int responseCode = httpURLConnection.getResponseCode();
            if(responseCode == 200) {
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                StringBuffer stringBuffer = new StringBuffer();
                String line;
                while((line = br.readLine()) != null){
                    stringBuffer.append(line);
                }
                try {
                    inputStream.close();
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
                JSONArray jsonArray = new JSONArray(stringBuffer.toString());
                System.out.println(jsonArray);
                return jsonArray;
            }
            else{
                return null;
                //this.doInBackground();
            }
        }
        catch(IOException e) {
            e.printStackTrace();
            return null;
            //this.doInBackground();
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
            //this.doInBackground();
        }
        finally {
            if(httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
        //return null;
    }

    public void setOnCallBack(AsyncCallback _cbj) {
        asyncCallback = _cbj;
    }
}


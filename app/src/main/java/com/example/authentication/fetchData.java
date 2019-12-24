package com.example.authentication;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class fetchData extends AsyncTask<Void,Void,Void> {

  String data ="";
  String dataParsed ="";
  String dataSingleParsed="";

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        database.data.setText(this.dataParsed);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            URL url = new URL("https://api.myjson.com/bins/1848qc");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while(line != null)
            {
                line = bufferedReader.readLine();
                data += line;
            }
            JSONArray JA =new JSONArray(data);
            for(int i=0;i < JA.length(); i++)
            {
                JSONObject JO = (JSONObject) JA.get(i);
                dataSingleParsed = "UserId:" + JO.get("userId")+"\n"+
                                   "Password:" + JO.get("password")+"\n" +
                                   "Id:" + JO.get("id")+"\n"+"\n";
                dataParsed = dataParsed+dataSingleParsed;

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}

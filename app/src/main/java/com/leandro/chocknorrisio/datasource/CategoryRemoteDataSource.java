package com.leandro.chocknorrisio.datasource;

import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class CategoryRemoteDataSource {

    public interface ListCategoriesCallback{

        void onSuccess(List<String> response);

        void onError(String message);

        void onComplete();
    }

    public void findall(ListCategoriesCallback callback){
        new CategoryTask(callback).execute();

    }

    private static class CategoryTask extends AsyncTask<Void,Void, List<String>>{

        private String errorMessage;
        private final ListCategoriesCallback callback;

        public CategoryTask(ListCategoriesCallback callback) {
        this.callback = callback;
        }

        @Override
        protected List<String> doInBackground(Void... voids) {

            List<String> response = new ArrayList<>();
            HttpsURLConnection urlConnection;
            try {
                URL url = new URL(EndPoint.GET_CATEGORIES);
                urlConnection = (HttpsURLConnection) url.openConnection();
                urlConnection.setReadTimeout(2000);
                urlConnection.setConnectTimeout(2000);
                int responseCode = urlConnection.getResponseCode();
                //erros acima de 400
                //acerto 200
                if (responseCode > 400){
                    throw new IOException("Erro na comunicacao do servidor");
                }

                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                JsonReader jsonReader = new JsonReader(new InputStreamReader(in));
                jsonReader.beginArray();
                while (jsonReader.hasNext()){
                    response.add(jsonReader.nextString());
                }
                jsonReader.endArray();

            } catch (MalformedURLException e) {
                errorMessage = e.getMessage();
            } catch (IOException e) {
                errorMessage  = e.getMessage();
            }
            return response;
        }

        @Override
        protected void onPostExecute(List<String> strings) { //MAINTHREAD VISUALIZAÇÃO
            if (errorMessage!= null){
                Log.i("TESTE", errorMessage);
                callback.onError(errorMessage);
            }else{
                Log.i("TESTE", strings.toString());
                callback.onSuccess(strings);
            }
            callback.onComplete();
        }
    }
}

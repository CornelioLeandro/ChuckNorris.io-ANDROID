package com.leandro.chocknorrisio.datasource;

import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;

import com.leandro.chocknorrisio.model.Joke;
import com.leandro.chocknorrisio.presentation.JokePresenter;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class JokeRemoteDataSource  {

    public interface JokesCallback{

        void onSuccess(Joke response);

        void onError(String message);

        void onComplete();
    }

    public void findJokeBy(JokesCallback callback, String category) {
        new JokeTask(callback, category).execute();
    }

    private static class JokeTask extends AsyncTask<Void,Void, Joke> {


        private final JokesCallback callback;
        private final String category;
        String errorMessage;

        public JokeTask(JokesCallback callback, String category) {
            this.callback = callback;
            this.category = category;
        }

        @Override
        protected Joke doInBackground(Void... voids) {

            Joke joke = null;
            HttpsURLConnection urlConnection;

            try {
                String endPoint = String.format("%s?category=%s", EndPoint.GET_JOKE,category);
                URL url = new URL(endPoint);
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

                String iconUrl = null;
                String value = null;

                while (jsonReader.hasNext()){
                    JsonToken token = jsonReader.peek();
                    if (token == JsonToken.NAME){
                        String name = jsonReader.nextName();
                        if (name.equals("category"))
                            jsonReader.skipValue();
                        else if (name.equals("icon_url"))
                            iconUrl = jsonReader.nextString();
                        else if (name.equals("value"))
                            value = jsonReader.nextString();
                        else
                            jsonReader.skipValue();
                    }
                }
                new Joke(iconUrl,value);
                jsonReader.endObject();

            } catch (MalformedURLException e) {
                errorMessage = e.getMessage();
            } catch (IOException e) {
                errorMessage  = e.getMessage();
            }
            return joke;
        }

        @Override
        protected void onPostExecute(Joke joke) { //MAINTHREAD VISUALIZAÇÃO
            if (errorMessage!= null){
                callback.onError(errorMessage);
            }else{
                callback.onSuccess(joke);
            }
            callback.onComplete();
        }
    }
}

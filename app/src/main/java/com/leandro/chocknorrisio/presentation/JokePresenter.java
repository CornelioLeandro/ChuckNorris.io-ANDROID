package com.leandro.chocknorrisio.presentation;

import com.leandro.chocknorrisio.JokeActivity;
import com.leandro.chocknorrisio.datasource.JokeRemoteDataSource;
import com.leandro.chocknorrisio.model.Joke;

public class JokePresenter implements JokeRemoteDataSource.JokesCallback {
    private final JokeActivity view;
    private final JokeRemoteDataSource datasource;

    public JokePresenter(JokeActivity jokeActivity, JokeRemoteDataSource dataSource) {
        this.view = jokeActivity;
        this.datasource = dataSource;
    }

    public void findJokeBy(String category) {
        this.view.showProgressBar();
        this.datasource.findJokeBy(this, category);

    }

    @Override
    public void onSuccess(Joke response) {
        this.view.showJoke(response);
    }

    @Override
    public void onError(String message) {
        this.view.showFailure(message);
    }

    @Override
    public void onComplete() {
        this.view.hideProgressBar();
    }
}

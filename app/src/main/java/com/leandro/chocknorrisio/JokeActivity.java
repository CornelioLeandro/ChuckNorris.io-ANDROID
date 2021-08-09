package com.leandro.chocknorrisio;

import android.app.ProgressDialog;
import android.media.Image;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.leandro.chocknorrisio.datasource.JokeRemoteDataSource;
import com.leandro.chocknorrisio.model.Joke;
import com.leandro.chocknorrisio.presentation.JokePresenter;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class JokeActivity extends AppCompatActivity {

    static final String CATEGORY_KEY = "category_key";
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joke);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String category = getIntent().getExtras().getString(CATEGORY_KEY);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(category);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            JokeRemoteDataSource dataSource = new JokeRemoteDataSource();
            final JokePresenter presenter = new JokePresenter(this, dataSource);

            presenter.findJokeBy(category);

            FloatingActionButton fab = findViewById(R.id.fab);
            fab.setOnClickListener(view -> {
                presenter.findJokeBy(category);

            });
        }
    }

    public void showJoke(Joke joke) {
        TextView txtJoke = findViewById(R.id.txt_joke);
        txtJoke.setText(joke.getValue());
        ImageView iv = findViewById(R.id.img_icon);
        Picasso.get().load(joke.getIconUrl()).into(iv);
    }

    public void showFailure(String messange) {
        Toast.makeText(this, messange, Toast.LENGTH_SHORT).show();
    }

    public void showProgressBar() {
        if (progress == null) {
            progress = new ProgressDialog(this);
            progress.setMessage(getString(R.string.loading));
            progress.setIndeterminate(true);
            progress.setCancelable(false);
        }
        progress.show();
    }

    public void hideProgressBar() {
        if (progress != null) {
            progress.hide();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return false;
        }
    }
}
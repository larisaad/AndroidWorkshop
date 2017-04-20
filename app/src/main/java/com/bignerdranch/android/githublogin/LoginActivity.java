package com.bignerdranch.android.githublogin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.githublogin.model.GitHub;
import com.bignerdranch.android.githublogin.model.LoginData;

import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "LoginActivity";
    private static final String AUTH = "auth";
    private EditText mUsername, mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate() called");

        setContentView(R.layout.activity_main);

        mUsername = (EditText) findViewById(R.id.username);
        mPassword = (EditText) findViewById(R.id.password);
        Button loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(this);

        // check if already logged in
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (preferences.getString(AUTH, null) != null) {
           goToProfileScreen();
        }

        findViewById(R.id.id_container).setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                closeKeyboard(v);
                return false;
            }
        });

    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick() called with: v = [" + v + "]");
        switch (v.getId()) {
            case R.id.login_button:
                performLogin();
                break;
        }
    }
    public void performLogin() {
        //get credentials
        final String authHash = Credentials.basic(mUsername.getText().toString(),
                mPassword.getText().toString());

        Call<LoginData> callable = GitHub.Service.Get()
                .checkAuth(authHash);
        callable.enqueue(new Callback<LoginData>() {

            @Override
            public void onResponse(Call<LoginData> call, Response<LoginData> response) {
                if(response.isSuccessful()) {
                    SharedPreferences preferences = PreferenceManager
                            .getDefaultSharedPreferences(LoginActivity.this);
                    preferences.edit()
                            .putString(AUTH, authHash).
                            putString("username", mUsername.getText().toString())
                            .apply();

                    goToProfileScreen();
                } else {
                    switch(response.code()) {
                        case 403:
                            Toast.makeText(LoginActivity.this, "Invalid username or password",
                                    Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(LoginActivity.this, "Failed to login",
                                    Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginData> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(LoginActivity.this, "No internet connection",
                        Toast.LENGTH_SHORT).show();

            }
        });

    }
    private void closeKeyboard(View v) {
        if(v != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    private void goToProfileScreen() {
        Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
        intent.putExtra("username", mUsername.getText().toString());
        startActivity(intent);
        finish();

    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPaused() called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

}

package com.bignerdranch.android.githublogin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.githublogin.model.GitHub;
import com.bignerdranch.android.githublogin.model.UserData;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ProfileActivity";
    private static final String AUTH = "auth";

    private ImageView mProfilePicture;
    private TextView mName;
    private TextView mOrganization;
    private TextView mBio;
    private TextView mLocation;
    private TextView mEmail;
    private TextView mCreated;
    private TextView mUpdated;
    private TextView mPublicRepos;
    private TextView mPrivateRepos;
    private UserData mDisplayedProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mProfilePicture = (ImageView) findViewById(R.id.img);
        mName = (TextView) findViewById(R.id.name);
        mOrganization = (TextView) findViewById(R.id.organization);
        mBio = (TextView) findViewById(R.id.bio);
        mLocation = (TextView) findViewById(R.id.location);
        mEmail = (TextView) findViewById(R.id.email);
        mCreated = (TextView) findViewById(R.id.created);
        mUpdated = (TextView) findViewById(R.id.updated);
        mPublicRepos = (TextView) findViewById(R.id.public_repos);
        mPrivateRepos = (TextView) findViewById(R.id.private_repos);
        findViewById(R.id.btn_blog).setOnClickListener(this);
        findViewById(R.id.btn_repositories).setOnClickListener(this);

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, ''yy");
        mCreated.setText(dateFormat.format(new Date()));
        mUpdated.setText(dateFormat.format(new Date()));


        fetchProfile();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_blog:
                //  TODO: open a screen displaying the Blog URL
                Toast.makeText(this, "Don't have any blog, srry :)",
                        Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_repositories:
                startActivity(new Intent(this, RepositoryActivity.class));
                break;
        }
    }

    private void fetchProfile() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Call<UserData> profileCall =
                GitHub.Service.Get().getUserData(preferences.getString(AUTH, null));
        profileCall.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {
                if (response.isSuccessful()) {
                    UserData profile = response.body();
                    updateUI(profile);

                } else {
                    switch (response.code()) {
                        case 403:
                            Toast.makeText(ProfileActivity.this, "An error occurred!", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(ProfileActivity.this, "An error occurred!", Toast.LENGTH_SHORT).show();
                            break;

                    }
                }
            }

            @Override
            public void onFailure(Call<UserData> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(ProfileActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void updateUI(UserData profile) {

        mName.setText(profile.getName());
        mDisplayedProfile = profile;
        //  TODO: load an image URL into the ImageView
        mProfilePicture.setImageResource(R.mipmap.pozagit);
        mName.setText(profile.getName());
        mOrganization.setText(profile.getCompany());
        mBio.setText(profile.getBio());
        mLocation.setText(profile.getLocation());
        //setTextUnderlined(mLocation, profile.getLocation());
        //setTextUnderlined(mEmail, profile.getEmail());
        mEmail.setText(profile.getEmail());
        //setTextUnderlined(mCreated, profile.getCreatedAt());
        mCreated.setText(profile.getCreatedAt());
        //setTextUnderlined(mUpdated, profile.getUpdatedAt());
        mUpdated.setText(profile.getUpdatedAt());
        //setTextUnderlined(mPublicRepos, profile.getPublicRepos().toString());
        mPrivateRepos.setText(profile.getPublicRepos().toString());
        //setTextUnderlined(mPrivateRepos, profile.getTotalPrivateRepos().toString());
        mPrivateRepos.setText(profile.getTotalPrivateRepos().toString());
    }

    /**
     * Method that sets a text into a TextView and underlines it as TextView does not have this
     * functionality by default
     *
     * @param textView The TextView to apply the underlined content on
     * @param text     The text to display underlined
     */
    private void setTextUnderlined(TextView textView, String text) {
        SpannableString content = new SpannableString(text);
        content.setSpan(new UnderlineSpan(), 0, content.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        textView.setText(content);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                logout();
                break;
        }
        return true;
    }

    public void logout() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ProfileActivity.this);
        preferences.edit().remove(AUTH).apply();
        startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
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

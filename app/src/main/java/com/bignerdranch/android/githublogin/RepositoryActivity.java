package com.bignerdranch.android.githublogin;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.githublogin.model.GitHub;
import com.bignerdranch.android.githublogin.model.Repository;
import com.bignerdranch.android.githublogin.model.RepositoryData;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bignerdranch.android.githublogin.R.id.recycler_view;
import static com.bignerdranch.android.githublogin.R.id.updated;

public class RepositoryActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private static final String AUTH = "auth";
    private Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //i have to inflate the activity's layout
        setContentView(R.layout.activity_repository); //layout for repository activity

        //this activity has one widget which is a recycler-view
        mRecyclerView = (RecyclerView) findViewById(recycler_view); //my widget

        /*A recycler view must have an ADAPTER and a LAYOUT MANAGER.
        * A recycler view has 3 build-in layouts :linear, grid, staggeredgrid.
        * To build a custom one, i have to extend RecyclerView.LayoutManager class.*/

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter();
        mRecyclerView.setAdapter(adapter);
        fetchRepos();


    }

    void fetchRepos() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Call<List<RepositoryData>> repoCall = GitHub.Service.Get().getRepoData(preferences.getString(AUTH, null));
        repoCall.enqueue(new Callback<List<RepositoryData>>() {
            @Override
            public void onResponse(Call<List<RepositoryData>> call, Response<List<RepositoryData>> response) {
                if (response.isSuccessful()) {
                    List<RepositoryData> data = response.body();
                    adapter.setData(data);
                    adapter.notifyDataSetChanged();

                } else {
                    switch (response.code()) {
                        case 403:
                            Toast.makeText(RepositoryActivity.this, "Forbidden", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(RepositoryActivity.this, "An error occured", Toast.LENGTH_SHORT).show();

                    }
                }
            }

            @Override
            public void onFailure(Call<List<RepositoryData>> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(RepositoryActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
            }
        });

    }

    /*The adapter provides access to the items on the data set, creates views for items and
    * replaces the content of the views with new data items, when the original item is no longer
    * visible.*/
    static class Adapter extends RecyclerView.Adapter {
        List<RepositoryData> mData;

        public List<RepositoryData> getData() {
            return mData;
        }

        //create new views (invoked ny layout manager)
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //create new view
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.view_repository, parent, false);

            return new ViewHolder(view);
        }

        //replace the contens of a view( invoked by layout manager)
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            //-get element from my data set at this position
            //-replace the contens of the view with that element
            ((ViewHolder) holder).bind(mData.get(position));

        }

        @Override
        public int getItemCount() {
            return mData != null ? mData.size() : 0;
        }

        public void setData(List<RepositoryData> data) {
            mData = data;
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {


        private final TextView mCountWatchers;
        private final TextView mNameAndOwner;
        private final TextView mDescription;
        private final LinearLayout mTopics;
        private final CheckBox mIsPublic;

        public ViewHolder(View itemView) {
            super(itemView);
            // Cache all views we will need when binding the model
            mCountWatchers = (TextView) itemView.findViewById(R.id.count_repos);
            mNameAndOwner = (TextView) itemView.findViewById(R.id.name_and_owner);
            mDescription = (TextView) itemView.findViewById(R.id.description);
            mTopics = (LinearLayout) itemView.findViewById(R.id.topics);
            mIsPublic = (CheckBox) itemView.findViewById(R.id.is_public);
        }


        public void bind(RepositoryData repository) {
            // The views are cached, just set the data
            mCountWatchers.setText(String.valueOf(repository.getWatchersCount()));
            mNameAndOwner.setText(itemView.getContext().getString((R.string.repo_name_owner), repository.getName(), repository.getOwner()));
            mDescription.setText(repository.getDescription());
            mIsPublic.setChecked(!repository.getPrivate());
            mTopics.removeAllViews();
//            for(String topic : repository.getTopics()) {
//                TextView topicTV = new TextView( itemView.getContext());
//                topicTV.setText(topic);
//                topicTV.setTextColor(ContextCompat.getColor(itemView.getContext(), android.R.color.black));
//                mTopics.addView(topicTV);
//            }

        }
    }
}

package com.bignerdranch.android.githublogin.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by larisa on 04.04.2017.
 */

public class Repository {
    String mName;
    String mOwner;
    String mDescription;
    boolean mIsPrivate;
    String mDefaultBranch;
    List<String> mTopics;
    int mWatchersCount;
    Date mCreatedAt;
    Date mUpdatedAt;

    static final List<Repository> sMockRepository;

    public Repository(String name, String owner, String description, boolean isPrivate,
                      String defaultBranch, List<String> topics, int watchersCount,
                      Date createdAt, Date updatedAt) {
        mName = name;
        mOwner = owner;
        mDescription = description;
        mIsPrivate = isPrivate;
        mDefaultBranch = defaultBranch;
        mTopics = topics;
        mWatchersCount = watchersCount;
        mCreatedAt = createdAt;
        mUpdatedAt = updatedAt;
    }

    static {
        sMockRepository = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            sMockRepository.add(new Repository(
                    "Repo" + i,
                    "Octocat",
                    "Some repo",
                    false,
                    "master",
                    new ArrayList<String>() {{
                        add("Android");
                        add("iOS");
                    }},
                    i,
                    new Date(),
                    new Date()

            ));
        }
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getOwner() {
        return mOwner;
    }

    public void setOwner(String owner) {
        mOwner = owner;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public boolean isPrivate() {
        return mIsPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        mIsPrivate = aPrivate;
    }

    public String getDefaultBranch() {
        return mDefaultBranch;
    }

    public void setDefaultBranch(String defaultBranch) {
        mDefaultBranch = defaultBranch;
    }

    public List<String> getTopics() {
        return mTopics;
    }

    public void setTopics(List<String> topics) {
        mTopics = topics;
    }

    public int getWatchersCount() {
        return mWatchersCount;
    }

    public void setWatchersCount(int watchersCount) {
        mWatchersCount = watchersCount;
    }

    public Date getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(Date createdAt) {
        mCreatedAt = createdAt;
    }

    public Date getUpdatedAt() {
        return mUpdatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        mUpdatedAt = updatedAt;
    }

    public static List<Repository> getMockRepository() {
        return sMockRepository;
    }
}


package my.beelzik.mobile.scopemvptest.model.gson;


import java.util.List;

import my.beelzik.mobile.scopemvptest.model.Repository;


public class SearchResult {

    private int mTotalCount;
    private boolean mIncompleteResults;
    private List<Repository> mItems;

    public List<Repository> getRepositories() {
        return mItems;
    }
}
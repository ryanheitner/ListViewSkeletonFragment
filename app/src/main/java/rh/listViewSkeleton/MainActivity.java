package rh.listViewSkeleton;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ProgressBar;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import rh.listViewSkeleton.events.ContributorDetailsEvent;
import rh.listViewSkeleton.model.Contributor;

public class MainActivity extends FragmentActivity implements MyListFragment.DownloadInProgressListener {
    static String TAG = "RYAN";
    // This is an example of using butterknife
    @InjectView(R.id.progressBar) ProgressBar mProgressBar;


   // private ProgressBar mProgressBar;

    /* Holds the state information for this activity. */
    protected ActivityState mState = new ActivityState();

    protected static class ActivityState {
        private int nextPage = 0;
        private List<Contributor> streamData = new ArrayList<Contributor>();

        public int getNextPage() {
            return nextPage;
        }

        public void incrementNextPage() {
            this.nextPage++;
        }

        public List<Contributor> getStreamData() {
            return streamData;
        }

        public void setStreamData(List<Contributor> streamData) {
            this.streamData = streamData;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressBar = (ProgressBar)findViewById(R.id.progressBar);

        if (getLastCustomNonConfigurationInstance() instanceof ActivityState) {
            mState = (ActivityState) getLastCustomNonConfigurationInstance();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        BaseApplication.getEventBus().register(this);
    }

    @Override
    public void downloadInProgress(Boolean inProgress) {
        if (inProgress) {
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.setVisibility(View.GONE);
        }
    }



    @Override
    public Object onRetainCustomNonConfigurationInstance(){
            // Return our state so we can later restore it in onCreate() via getLastNonConfigurationInstance();
        return mState;
    }


    @Override
    protected void onPause() {
        super.onPause();
        BaseApplication.getEventBus().unregister(this);
    }
    @Subscribe
    public void onContributorDetailsEvent(ContributorDetailsEvent event){
        DetailFragment fragment = (DetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.detailFragment);
        if (fragment != null && fragment.isInLayout()) {
            // do nothing it is handled in the fragment already
        } else {
            Intent intent = new Intent(getApplicationContext(),
                    DetailActivity.class);
            intent.putExtra(DetailActivity.CONTRIBUTOR, event.contributor);
            startActivity(intent);

        }
    }
}

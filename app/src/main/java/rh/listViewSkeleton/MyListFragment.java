package rh.listViewSkeleton;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.otto.Subscribe;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import rh.listViewSkeleton.adapters.MyAdapter;
import rh.listViewSkeleton.api.GithubApiClient;
import rh.listViewSkeleton.events.ContributorDetailsEvent;
import rh.listViewSkeleton.events.DataReceivedEvent;
import rh.listViewSkeleton.model.Contributor;

public class MyListFragment extends ListFragment {
   // I am using an interface rather than the bus here for practice also see on attach
   DownloadInProgressListener mCallback;


    // Container Activity must implement this interface
    public interface DownloadInProgressListener {
        public void downloadInProgress(Boolean loaded);
    }
    // For this I will use the bus
//    private OnItemSelectedListener listener;
//    public interface OnItemSelectedListener {
//        public void onItemSelected(Contributor contributor);
//    }
   private MainActivity mainActivity;
    static String TAG = "RYAN";

    /**
     * When this amount of items is left in the ListView yet to be displayed we will start downloading more data (if available).
     */
    private static final int RUNNING_LOW_ON_DATA_THRESHOLD = 10;
    private static final int ITEMS_PER_PAGE = 50;
    private MyAdapter mAdapter;
    private boolean mIsDownloadInProgress = false;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (DownloadInProgressListener) activity;
        } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString()
                        + " must implement OnDataLoadedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_row, container, false);

        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        mAdapter = new MyAdapter(BaseApplication.getContext(), 0, mainActivity.mState.getStreamData());
        setListAdapter(mAdapter);

        getListView().setOnScrollListener(mScrollListener);
//        // test OK HTTP
        BaseApplication.getEventBus().register(this);

        setListAdapter(mAdapter);

/*
                         This serves only to Test OK HTTP and the event bus it is not affecting this program
                         See the Location and Locations object this was generated with jsonschema2pojo
*/

        BaseApplication.getEventBus().register(this);
        new BackgroundWebRunner().execute("http://aws.site50.net/locations.json");

    }

    private void downloadInProgress(Boolean inProgress) {
        mIsDownloadInProgress = inProgress;
        mCallback.downloadInProgress(inProgress);
    }

    private void downloadData(final int pageNumber) {
        if (!mIsDownloadInProgress) {
            downloadInProgress(true);

//            mProgressBar.setVisibility(View.VISIBLE);
            GithubApiClient.getTwitchTvApiClient().getContributors("square", "retrofit", new Callback<List<Contributor>>() {
                @Override
                public void success(List<Contributor> contributors, Response response) {
                    consumeApiData(contributors);
                }

                @Override
                public void failure(RetrofitError retrofitError) {
                    consumeApiData(null);
                }
            });

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // Always unregister when an object no longer should be on the bus.
        BaseApplication.getEventBus().unregister(this);
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Toast.makeText(BaseApplication.getContext(),"HI",Toast.LENGTH_SHORT).show();
        // post a bus event
        Contributor contributor = (Contributor)l.getItemAtPosition(position);
        BaseApplication.getEventBus().post(new ContributorDetailsEvent(contributor));
    }

    @Override
    public void onResume() {
        super.onResume();
        // Kick off first download
        if (mainActivity.mState.getNextPage() == 0) {
            downloadData(mainActivity.mState.getNextPage());
        }
    }



    @Subscribe
    public void onDataReceivedEvent(DataReceivedEvent event){
        Gson gson = new GsonBuilder().create();
        Locations locations =  gson.fromJson(event.message,Locations.class);
        Toast.makeText(BaseApplication.getContext(), event.message, Toast.LENGTH_SHORT).show();
        Log.d(TAG,"event:" + event.message);
    }

    private void consumeApiData(List<Contributor> contributors) {
        if (contributors != null) {

            // Add the found streams to our array to render

            mainActivity.mState.getStreamData().addAll(contributors);

            // Tell the adapter that it needs to rerender
            mAdapter.notifyDataSetChanged();

            // Done loading; remove loading indicator
//            mProgressBar.setVisibility(View.GONE);

            // Keep track of what page to download next
            mainActivity.mState.incrementNextPage();
        }
        downloadInProgress(false);
    }
    /**
     * Scroll-handler for the ListView which can auto-load the next page of data.
     * Interface definition for a callback to be invoked when the list or grid has been scrolled.
     */
    private AbsListView.OnScrollListener mScrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            // Nothing to do
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            // Detect if the ListView is running low on data
            if (totalItemCount > 0 && totalItemCount - (visibleItemCount + firstVisibleItem) <= RUNNING_LOW_ON_DATA_THRESHOLD) {
                downloadData(mainActivity.mState.getNextPage());
            }
        }
    };


}
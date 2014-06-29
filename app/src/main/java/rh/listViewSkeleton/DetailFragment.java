package rh.listViewSkeleton;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rh.listViewSkeleton.events.ContributorDetailsEvent;

public class DetailFragment extends Fragment {
   // I am using an interface rather than the bus here for practice also see on attach

    static String TAG = "RYAN";
//    TextView lblLogin;
     @InjectView(R.id.lbl_login)  TextView lblLogin;
     @InjectView(R.id.lbl_contributions)  TextView lblContributions;

    /**
     * When this amount of items is left in the ListView yet to be displayed we will start downloading more data (if available).
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail, container, false);
        ButterKnife.inject(this, view);

        lblContributions.setText("This works fine");
        return view;
    }


    public void setText(String item) {
        TextView view = (TextView) getView().findViewById(R.id.lbl_login);
        view.setText("test");
    }

    @Override
    public void onResume() {
        super.onResume();
        BaseApplication.getEventBus().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        BaseApplication.getEventBus().unregister(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
    @Subscribe
    public void onContributorDetailsEvent(ContributorDetailsEvent event){
        lblLogin.setText(event.contributor.getLogin());
        lblContributions.setText(String.valueOf(event.contributor.getContributions()));
    }



    public void setLblLogin(String login) {
        this.lblLogin.setText(login);
    }

    public void setLblContributions(int contributions) {
        this.lblContributions.setText(String.valueOf(contributions));
    }
}



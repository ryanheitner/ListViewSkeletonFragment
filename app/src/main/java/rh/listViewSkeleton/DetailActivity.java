package rh.listViewSkeleton;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import butterknife.InjectView;
import rh.listViewSkeleton.model.Contributor;

public class DetailActivity extends FragmentActivity  {


    public static final String CONTRIBUTOR = "contributor";
    private Contributor contributor;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Need to check if Activity has been switched to landscape mode
        // If yes, finished and go back to the start Activity
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            finish();
            return;
        }
        setContentView(R.layout.activity_detail);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
             contributor = extras.getParcelable(CONTRIBUTOR);
            DetailFragment detailFragment = (DetailFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.detailFragment);
            detailFragment.setLblLogin(contributor.getLogin());
            detailFragment.setLblContributions(contributor.getContributions());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}

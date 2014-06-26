package rh.listViewSkeleton.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rh.listViewSkeleton.R;
import rh.listViewSkeleton.model.Contributor;

public class MyAdapter extends ArrayAdapter<Contributor> {
    static String TAG = "RYAN2";

    private LayoutInflater mInflater;
    private Context context;
    public MyAdapter(Context context, int textViewResourceId, List<Contributor> objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
//        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater = LayoutInflater.from(this.context);

    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder viewHolder;
        // reuse views
        if (view != null) {
            // Just get our existing holder
            viewHolder = (ViewHolder) view.getTag();
        } else {
            // View doesn't exist so create it and create the holder
            view = mInflater.inflate(R.layout.list_item, parent, false);
            viewHolder = new ViewHolder(view);
//            viewHolder.lblLogin = (TextView) rowview.findViewById(R.id.lbl_login);
//            viewHolder.lblContributions = (TextView) rowview.findViewById(R.id.lbl_contributions);

            view.setTag(viewHolder);
        }

        // Populate via the holder for speed
        Contributor contributor =  super.getItem(position);
        // Populate the item contents
        viewHolder.lblLogin.setText(contributor.getLogin());
        viewHolder.lblContributions.setText(String.valueOf(contributor.getContributions()));

        // Load the screen cap image on a background thread
        Picasso.with(context)
                .load("http://i.imgur.com/DvpvklR.png")
                .resize(50, 50)
                .centerCrop()
                .into(viewHolder.imageView);

        return view;
    }

    // Holder class used to efficiently recycle view positions
    static final class ViewHolder {
        @InjectView(R.id.lbl_login)  TextView lblLogin;
        @InjectView(R.id.lbl_contributions) TextView lblContributions;
        @InjectView(R.id.image_view) ImageView imageView;

        public ViewHolder(View view) {
            ButterKnife.inject(this,view);
        }
//        public TextView lblLogin;
//        public TextView lblContributions;

    }
}

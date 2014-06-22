package rh.twitchtvclient.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import rh.twitchtvclient.R;
import rh.twitchtvclient.model.JustinTvStreamData;

import java.util.List;

public class JustinTvStreamAdapter extends ArrayAdapter<JustinTvStreamData> {
    static String TAG = "RYAN2";

    private LayoutInflater mInflater;

    public JustinTvStreamAdapter(Context context, int textViewResourceId, List<JustinTvStreamData> objects) {
        super(context, textViewResourceId, objects);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowview = convertView;
        Holder holder;
        // reuse views
        if (rowview == null) {
            // View doesn't exist so create it and create the holder
            rowview = mInflater.inflate(R.layout.grid_item, parent, false);

            holder = new Holder();
            holder.screenCapThumbnailImage = (ImageView) rowview.findViewById(R.id.imgScreencapThumbnail);
            holder.lblTitleText = (TextView) rowview.findViewById(R.id.lblTitle);
            holder.lblGame = (TextView) rowview.findViewById(R.id.lblGame);
            holder.lblUser = (TextView) rowview.findViewById(R.id.lblUser);
            holder.lblViewers = (TextView) rowview.findViewById(R.id.lblViewers);
            holder.channelThumbnailImage = (ImageView) rowview.findViewById(R.id.imgChannelThumbnail);

            rowview.setTag(holder);
        } else {
            // Just get our existing holder
            holder = (Holder) rowview.getTag();
        }

        // Populate via the holder for speed
        JustinTvStreamData stream =  super.getItem(position);
//        Log.d(TAG, "Title:" + stream.getTitle());
//        Log.d(TAG, "Game:" + stream.getMeta_game());
//        Log.d(TAG, "Login:" + stream.getChannel().getLogin());

        // Populate the item contents
        holder.lblTitleText.setText(stream.getTitle());
        holder.lblGame.setText(stream.getMeta_game());
        holder.lblUser.setText(stream.getChannel().getLogin());
        int totalViewers = stream.getChannel_count().intValue();
        holder.lblViewers.setText(getContext().getResources().getQuantityString(R.plurals.viewers, totalViewers, totalViewers));

        // Load the screen cap image on a background thread
        Picasso.with(getContext())
                .load(stream.getChannel().getScreen_cap_url_medium())
                .placeholder(R.drawable.white)
                .into(holder.screenCapThumbnailImage);

        // Load the channel thumbnail image on a background thread
        Picasso.with(getContext())
                .load(stream.getChannel().getImage_url_medium())
                .placeholder(R.drawable.transparent)
                .into(holder.channelThumbnailImage);

        return rowview;
    }

    // Holder class used to efficiently recycle view positions
    private static final class Holder {
        public ImageView screenCapThumbnailImage;

        public ImageView channelThumbnailImage;

        public TextView lblTitleText;

        public TextView lblGame;

        public TextView lblUser;

        public TextView lblViewers;
    }
}

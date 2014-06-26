package rh.listViewSkeleton.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Contributor implements Parcelable {

    private String login;
	private int contributions;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public int getContributions() {
        return contributions;
    }

    public void setContributions(int contributions) {
        this.contributions = contributions;
    }

    @Override
    public int describeContents() {
        // 99.9% of the time you can just ignore this
        return 0;
    }


    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Contributor> CREATOR = new Parcelable.Creator<Contributor>() {
        public Contributor createFromParcel(Parcel in) {
            return new Contributor(in);
        }

        public Contributor[] newArray(int size) {
            return new Contributor[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private Contributor(Parcel in) {
        readFromParcel( in );
    }
    // write your object's data to the passed-in Parcel
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(login);
        dest.writeInt(contributions);
    }
    private void readFromParcel(Parcel in ) {

        login = in .readString();
        contributions = in .readInt();
    }

}

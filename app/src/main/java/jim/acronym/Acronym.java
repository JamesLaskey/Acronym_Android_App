package jim.acronym;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jim on 7/26/14.
 */
public class Acronym implements Parcelable{

    protected String acronym;
    protected String[] words;
    protected String desc;

    protected Acronym(String acronym, String[] words, String desc){
        this.acronym = acronym;
        this.words = words;
        this.desc = desc;
    }

    public Acronym(Parcel parcel) {
        this.acronym = parcel.readString();
        words = new String[parcel.readInt()];
        parcel.readStringArray(words);
        this.desc = parcel.readString();
    }

    //parcelable creator
    public static final Parcelable.Creator<Acronym> CREATOR = new Parcelable.Creator<Acronym>() {

        @Override
        public Acronym createFromParcel(Parcel parcel) {
            return new Acronym(parcel);
        }

        @Override
        public Acronym[] newArray(int i) {
            return new Acronym[i];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        int wordsSize = words.length;
        out.writeString(acronym);
        out.writeInt(wordsSize);
        out.writeStringArray(words);
        out.writeString(desc);
    }


}

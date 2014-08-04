package jim.acronym;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;

/**
 * Created by jim on 7/27/14.
 */
public class Word implements Parcelable{
    protected String word;
    protected String firstLetter;
    protected String definition;

    public Word(String word, String firstLetter, String definition) {
        this.word = word;
        this.firstLetter = firstLetter;
        this.definition = definition;
    }

    //for recreating word from parcel for transferring inside bundle/intent/etc
    public Word(Parcel parcel) {
        this.word = parcel.readString();
        this.firstLetter = parcel.readString();
        this.definition = parcel.readString();
    }



    @Override
    public String toString() {
        return word;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(word);
        parcel.writeString(firstLetter);
        parcel.writeString(definition);
    }

    public static final Creator CREATOR = new Creator<Word>() {

        @Override
        public Word createFromParcel(Parcel parcel) {
            return new Word(parcel);
        }

        @Override
        public Word[] newArray(int i) {
            return new Word[i];
        }

    };
}

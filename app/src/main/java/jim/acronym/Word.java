package jim.acronym;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by jim on 7/27/14.
 */
public class Word implements Parcelable{
    private static final String APIURL = "http://developer.wordnik.com/v4";
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

    /**
     * gets a definition for the word from Merriam Webster's dictionary api
     * @param context an activity context needed to access the shared preferences and system services
     * @return
     */
    public String getOnlineDefinition(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(AcronymSettings.PREFS_NAME, 0);
        String apiKey = preferences.getString("apiKey", "");
        if(apiKey == "") return null;
        try {
            apiKey = URLEncoder.encode(apiKey, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }

        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()) {
            new GetOnlineDefinition().execute(APIURL, word, apiKey);
        }else {
            return null;
        }
        return "";
    }

    private class GetOnlineDefinition extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String url = strings[0] + "/word.json/" + strings[1] + "/definitions";
            try {
                String resp = downloadURL(url);
                System.out.println(resp);
                //TODO custom exceptions covering other cases
                if(resp == null) {
                    return "No Definitions Found";
                }
                return resp;
            } catch (IOException e) {
                return "Error Occurred";
            }
        }


        //TODO move this into general class for access by other classes than just Word
        /**
         * hits the given url and returns the response
         * @param myUrl the full url as a string to attempt to reach
         * @return the response as a String or null if connection was refused
         * @throws IOException
         */
        private String downloadURL(String myUrl) throws IOException {
            BufferedReader input = null;

            try {
                URL url = new URL(myUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(2500);
                connection.setRequestMethod("GET");
                connection.connect();

                if(connection.getResponseCode() != 200) {
                    System.out.println(connection.getResponseCode());
                    return null;
                }
                input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder resp = new StringBuilder();
                String line = input.readLine();
                while(line != null) {
                    resp.append(line);
                    line = input.readLine();
                }
                input.close();
                return resp.toString();
            }finally {
                if(input != null){
                    input.close();
                }
            }
        }

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

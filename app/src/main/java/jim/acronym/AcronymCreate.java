package jim.acronym;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

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
import java.util.LinkedList;

public class AcronymCreate extends Activity {

    private static final String APIURL = "http://api.wordnik.com:80/v4/word.json/";
    private final Context context = this;
    private final Dialog dialog = new Dialog(context);
    private EditText dialogCustomDef;
    private ListView onlineDefList;
    private Button dialogCustomDone;


    private LinkedList<Word> words;
    private AcronymCreateListViewAdapter acrAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        words = new LinkedList<Word>();
        if(savedInstanceState != null) {
            Parcelable[] parsedWords = savedInstanceState.getParcelableArray("words");
            for(Parcelable pword : parsedWords) {
                words.addLast((Word) pword);
            }
        }

        setContentView(R.layout.activity_acronym_create);

        ListView wordList = (ListView) findViewById(R.id.create_acr_list);
        acrAdapter = new AcronymCreateListViewAdapter(this, R.layout.acr_create_list_item, words, wordList);

        wordList.setAdapter(acrAdapter);

        dialog.setContentView(R.layout.word_def_select);
        dialogCustomDef = (EditText) findViewById(R.id.word_def_select_custom);
        onlineDefList = (ListView) findViewById(R.id.word_def_select_dictionary);
        onlineDefList.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1));
        dialogCustomDone = (Button) findViewById(R.id.word_def_select_done);

        wordList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {
                dialogCustomDef.setText("");
                getOnlineDefinition(context, words.get(position));

                onlineDefList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        view.setBackgroundColor(Color.MAGENTA);
                        dialogCustomDone.setTag(i);
                    }
                });
                dialog.show();
                dialogCustomDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String customDef = dialogCustomDef.getText().toString();
                        if(customDef.length() > 0) {
                            words.get(position).definition = customDef;
                        }else if(dialogCustomDone.getTag() != null) {
                            ListAdapter adapter = onlineDefList.getAdapter();
                            words.get(position).definition = (String) adapter.getItem((Integer) dialogCustomDone.getTag());
                        }
                        System.out.println(words.get(position).definition);
                    }
                });
            }
        });

        acrAdapter.notifyDataSetChanged();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.acronym_create, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }else if(id == R.id.action_save) {
            return saveAcronym();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * gets a definition for the word from Merriam Webster's dictionary api
     * @param context an activity context needed to access the shared preferences and system services
     * @param word
     * @return
     */
    public String[] getOnlineDefinition(Context context, Word word) {
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
            new GetOnlineDefinition().execute(APIURL, word.word, apiKey);
        }else {
            return null;
        }
        return null;
    }

    private class GetOnlineDefinition extends AsyncTask<String, Void, String[]> {

        @Override
        protected String[] doInBackground(String... strings) {
            String url = strings[0] + strings[1] + "/definitions?limit=10&api_key=" + strings[2];
            try {
                String resp = downloadURL(url);
                if(resp == null) {
                    return null;
                }
                System.out.println(resp);
                String[] defs = recoverDefsFromJSON(resp);
                System.out.println(defs.length);
                //TODO custom exceptions covering other cases

                return defs;
            } catch (IOException e) {
                return null;
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

        /**
         * retrieves word definitions from Wordnik's json response
         * @param json JSON string containing the definitions
         * @return an array of definitions for the
         */
        private String[] recoverDefsFromJSON(String json) {
            try {
                JSONArray jsonArray = new JSONArray(json);
                String[] defs = new String[jsonArray.length()];
                for(int i = 0; i < jsonArray.length(); i++) {
                    JSONObject defObject = (JSONObject) jsonArray.get(i);
                    defs[i] = defObject.getString("text");
                }
                return defs;
            } catch (JSONException e) {
                e.printStackTrace();
                return new String[0];
            }
        }

        @Override
        protected void onPostExecute(String[] result) {
            ArrayAdapter<String> adapter = (ArrayAdapter<String>) onlineDefList.getAdapter();
            adapter.clear();
            adapter.addAll(result);
            adapter.notifyDataSetChanged();
        }

    }


    private boolean saveAcronym() {
        AcronymDatabase dbHelper = new AcronymDatabase(this);

        Word[] wordArr = new Word[words.size()];
        words.toArray(wordArr);
        String acronym = "";
        for(int i = 0; i < wordArr.length; i++){
            acronym = acronym + wordArr[i].firstLetter;
        }

        String desc = ((TextView) findViewById(R.id.create_acr_desc)).getText().toString();
        Acronym acr = new Acronym(acronym, wordArr, desc);
        return dbHelper.insertAcronym(acr) > -1;
    }

    //click listener for when user wants to add a word to the acronym
    public void addWordToAcr(View view) {
        EditText editText = (EditText) findViewById(R.id.create_new_word_edit_text);
        String word = editText.getText().toString();
        if(word != null) {
            words.add(new Word(word, (word.charAt(0) + "").toUpperCase(), ""));
            acrAdapter.notifyDataSetChanged();
        }
        editText.setText("");
    }

    @Override
    public void onSaveInstanceState(Bundle savedState) {
        super.onSaveInstanceState(savedState);

        LinkedList<Word> words = acrAdapter.getValues();
        Parcelable[] parsedWords = new Parcelable[words.size()];
        int size = words.size();
        for (int i = 0; i < size; i++) {
            parsedWords[i] = words.pop();
        }
        savedState.putParcelableArray("words", parsedWords);
    }
}

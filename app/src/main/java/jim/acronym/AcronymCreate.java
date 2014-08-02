package jim.acronym;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.LinkedList;

public class AcronymCreate extends Activity {

    private LinkedList<Word> words;
    private ListView wordList;
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

        setContentView(R.layout.activity_create_acronym);

        wordList = (ListView) findViewById(R.id.create_acr_list);
        acrAdapter = new AcronymCreateListViewAdapter(this, R.layout.acr_create_list_item, words, wordList);

        wordList.setAdapter(acrAdapter);

        acrAdapter.notifyDataSetChanged();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create_acronym, menu);
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

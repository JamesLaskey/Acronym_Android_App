package jim.acronym;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import jim.acronym.R;

public class CreateAcronym extends Activity {

    private ArrayList<String> words;
    private ListView wordList;
    private ArrayAdapter<String> acrAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_acronym);

        //setup wordlist and add rearrange listener
        words = new ArrayList<String>(4);
        wordList = (ListView) findViewById(R.id.create_acr_list);
        acrAdapter = new ArrayAdapter<String>(this, R.layout.acr_create_list_item, words);
        wordList.setAdapter(acrAdapter);


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
        }
        return super.onOptionsItemSelected(item);
    }

    //click listener for when user wants to add a word to the acronym
    public void addWordToAcr(View view) {
        EditText editText = (EditText) findViewById(R.id.create_new_word_edit_text);
        String word = editText.getText().toString();
        if(word != null) {
            words.add(word);
            acrAdapter.notifyDataSetChanged();
        }
    }

    public void removeWordFromAcr(View view) {

    }
}

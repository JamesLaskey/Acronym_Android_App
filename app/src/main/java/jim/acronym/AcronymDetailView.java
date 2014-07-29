package jim.acronym;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import jim.acronym.R;

public class AcronymDetailView extends Activity {

    private Acronym acr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acronym_detail_view);

        //get acronym to be displayed
        this.acr = getIntent().getParcelableExtra("acronym");

        //fill word part list
        ListView wordList = (ListView) findViewById(R.id.wordList);
        final String[] simpleWordArr = new String[acr.words.length];
        //until I make a separate class for this arrayAdapter simpleWordArr is hacky workaround
        wordList.setAdapter(new ArrayAdapter<String>(this, R.layout.word_list_item, simpleWordArr){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View rowView = inflater.inflate(R.layout.word_list_item, parent, false);

                TextView letter = (TextView) rowView.findViewById(R.id.wordListLetter);
                TextView word = (TextView) rowView.findViewById(R.id.wordListWord);

                letter.setText(acr.words[position].firstLetter);
                word.setText(acr.words[position].word);

                return rowView;
            }
        });

        //put in title
        TextView title = (TextView) findViewById(R.id.acronymTitle);
        title.setText(acr.acronym);

        //put in desc
        TextView desc = (TextView) findViewById(R.id.description);
        desc.setText(acr.desc);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.acronym_detail_view, menu);
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
}

package jim.acronym;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
        String[] simpleWordArr = new String[acr.words.length];
        //man I miss functional map :(
        for(int i = 0; i < acr.words.length; i++) {
            simpleWordArr[i] = acr.words[i].toString();
        }
        wordList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, simpleWordArr));

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

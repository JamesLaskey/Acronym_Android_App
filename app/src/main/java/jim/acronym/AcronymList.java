package jim.acronym;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;


public class AcronymList extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acronym_list);
        fillAcroynmList();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.acronym_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_create_acronym:
                startActivity(new Intent(this, AcronymCreate.class));
                break;

            case R.id.action_settings:
                break;
        }
        return true;
    }

    //will populate the list with acronyms
    public void fillAcroynmList() {

        AcronymDatabase db = new AcronymDatabase(this);
        final ArrayList<Acronym> acrArrayList = db.getAllAcronyms(-1, 10);

        //get the listview and set its adapter
        ListView list = (ListView) findViewById(R.id.acronymListView);
        AcronymListAdapter listAdapter = new AcronymListAdapter(this, acrArrayList);
        list.setAdapter(listAdapter);

        //bind a clicklistener for tapping on an item
        final Context clickContext = this;
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(clickContext, AcronymDetailView.class);
                intent.putExtra("acronym", acrArrayList.get(position));
                startActivity(intent);
            }
        });
    }
}

package jim.acronym;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

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
        acrAdapter = new ArrayAdapter<String>(this, R.layout.acr_create_list_item, words){

            @Override
            public View getView(final int i, View convertView, ViewGroup parent) {
                LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View rowView = inflater.inflate(R.layout.acr_create_list_item, parent, false);

                TextView letter = (TextView) rowView.findViewById(R.id.acrCreateListLetter);
                TextView word = (TextView) rowView.findViewById(R.id.acrCreateListWord);
                Button button = (Button) rowView.findViewById(R.id.acrCreateRemoveBtn);

                //remove button click listener to remove words from the acronym
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        words.remove(i);
                        acrAdapter.notifyDataSetChanged();
                    }
                });

                //set the first letter and word for the acronym
                letter.setText(words.get(i).charAt(0));
                word.setText(words.get(i));
                rowView.setTag(i);

                //enter motion event and create a shadow view for dragging
                rowView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                            view.startDrag(ClipData.newPlainText("",""), shadowBuilder, view, 0);
                            view.setVisibility(View.INVISIBLE);
                            return true;
                        }else {
                            return false;
                        }
                    }
                });

                rowView.setOnDragListener(new View.OnDragListener() {
                    //not implemented
                    @Override
                    public boolean onDrag(View view, DragEvent dragEvent) {
                        switch(dragEvent.getAction()) {
                            case DragEvent.ACTION_DRAG_ENDED:
                                float pos = dragEvent.getY();
                                //dragEvent.
                                break;
                        }
                        return false;
                    }
                });

                return rowView;
            }
        };

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
}

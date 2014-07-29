package jim.acronym;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Color;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by jim on 7/27/14.
 */
public class CreateListViewAdapter extends ArrayAdapter<Word> {

    private LinkedList<Word> words;
    private ListView listview;
    private Context context;

    public CreateListViewAdapter(Context context, int list_item_layout, LinkedList<Word> words, ListView listview) {
        super(context, list_item_layout, words);
        this.words = words;
        this.context = context;
        this.listview = listview;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.acr_create_list_item, parent, false);

        TextView letter = (TextView) rowView.findViewById(R.id.acrCreateListLetter);
        TextView word = (TextView) rowView.findViewById(R.id.acrCreateListWord);
        Button button = (Button) rowView.findViewById(R.id.acrCreateRemoveBtn);

        final ArrayAdapter<Word> notifyContext = this;
        //remove button click listener to remove words from the acronym
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                words.remove(i);
                notifyContext.notifyDataSetChanged();
            }
        });

        //set the first letter and word for the acronym
        letter.setText(words.get(i).firstLetter);
        word.setText(words.get(i).word);
        //insert the items position in case I want to pick it up later

        //enter motion event and create a shadow view for dragging
        rowView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                boolean success = view.startDrag(ClipData.newPlainText("", ""), shadowBuilder, view, 0);
                view.setVisibility(View.INVISIBLE);
                if(!success) {
                    view.setVisibility(View.VISIBLE);
                }

                for(int j = 0; j < words.size(); j++) {
                    listview.getChildAt(j).setOnDragListener(new View.OnDragListener() {
                        @Override
                        public boolean onDrag(View view, DragEvent dragEvent) {
                            switch(dragEvent.getAction()) {
                                case DragEvent.ACTION_DRAG_ENTERED:
                                    view.setBackgroundColor(Color.MAGENTA);
                                    break;
                                case DragEvent.ACTION_DROP:
                                    int pos = (Integer) view.getTag();
                                    Word word = words.remove(i);
                                    words.add(pos, word);

                                    view.setVisibility(View.VISIBLE);
                                    notifyContext.notifyDataSetChanged();
                                    break;
                                case DragEvent.ACTION_DRAG_EXITED:
                                    view.setBackgroundColor(Color.WHITE);
                                    break;
                            }
                            return true;
                        }
                    });
                }

                return true;
            }
        });

        rowView.setTag(i);

        return rowView;
    }

    public LinkedList<Word> getValues() {
        return words;
    }
}

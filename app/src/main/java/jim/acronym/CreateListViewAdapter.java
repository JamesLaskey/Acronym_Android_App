package jim.acronym;

import android.content.ClipData;
import android.content.Context;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by jim on 7/27/14.
 */
public class CreateListViewAdapter extends ArrayAdapter<Word> {

    private LinkedList<Word> words;
    private Context context;

    public CreateListViewAdapter(Context context, int list_item_layout, LinkedList<Word> words) {
        super(context, list_item_layout, words);
        this.words = words;
        this.context = context;
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
        rowView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                    boolean success = view.startDrag(ClipData.newPlainText("", ""), shadowBuilder, view, 0);
                    view.setVisibility(View.INVISIBLE);
                    if(!success) {
                        view.setVisibility(View.VISIBLE);
                    }
                    return true;
                }else {
                    view.setVisibility(View.VISIBLE);
                    return false;
                }
            }
        });

        rowView.setOnDragListener(new View.OnDragListener() {

            private DragEvent startEvent;

            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {
                switch(dragEvent.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        startEvent = dragEvent;
                        break;
                    case DragEvent.ACTION_DRAG_ENDED:
                        float end = dragEvent.getY();
                        float start = startEvent.getY();
                        float size = view.getHeight();
                        int index = i;
                        int totalElts = words.size();
                        float change = end - start;

                        System.out.println("change " + change);
                        System.out.println("start " + start);
                        System.out.println("pos " + end);

                        if(change > size/2) {
                            change = change - size /2;
                            int posToMove = Math.round(change/size);
                            if(posToMove > totalElts - i){
                                posToMove = totalElts - 1;
                            }else {
                                posToMove = i + posToMove;
                            }

                            System.out.println("positive " + posToMove);
                            Word word = words.remove(i);
                            words.add(posToMove, word);
                            notifyContext.notifyDataSetChanged();
                        }else if(change < -1 *size/2) {
                            change = change + size /2;
                            int posToMove = Math.round(change/size);
                            if(posToMove < 0){
                                posToMove = 0;
                            }else {
                                posToMove = i + posToMove;
                            }

                            System.out.println(posToMove);
                            Word word = words.remove(i);
                            words.add(posToMove, word);
                            notifyContext.notifyDataSetChanged();
                        }

                        view.setVisibility(View.VISIBLE);
                        break;
                    case DragEvent.ACTION_DRAG_EXITED:
                        view.setVisibility(View.VISIBLE);
                        break;
                }
                view.setVisibility(View.VISIBLE);
                return true;
            }
        });

        return rowView;
    }

    public LinkedList<Word> getValues() {
        return words;
    }
}

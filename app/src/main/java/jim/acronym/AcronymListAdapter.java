package jim.acronym;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jim on 7/26/14.
 */
public class AcronymListAdapter extends ArrayAdapter<Acronym> {

    ArrayList<Acronym> acrList;
    Context context;

    public AcronymListAdapter(Context context, ArrayList<Acronym> acrList) {
        super(context,R.layout.acr_list_item, acrList);
        this.acrList = acrList;
        this.context = context;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if(rowView == null) {
            //need to create new row layout since not reusing old one
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.acr_list_item, parent, false);

            //view holder for faster speeds over findbyid when we reuse acronyms
            ViewHolder holder = new ViewHolder();
            holder.text = (TextView) rowView.findViewById(R.id.acrListItemText);
            rowView.setTag(holder);
        }

        ViewHolder holder = (ViewHolder) rowView.getTag();
        TextView textView = holder.text;
        textView.setText(acrList.get(i).acronym);
        return rowView;
    }


    static private class ViewHolder {
        TextView text;
    }
}

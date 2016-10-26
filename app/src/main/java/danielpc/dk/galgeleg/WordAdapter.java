package danielpc.dk.galgeleg;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.Button;

/**
 * Created by Daniel_ on 23/10/2016.
 */

public class WordAdapter extends BaseAdapter {
    private String[] letters;
    private LayoutInflater letterInf;

    public WordAdapter(Context c) {
        //setup adapter
        letters=new String[29]; // A - Å
        for (int i = 0; i < letters.length; i++) {
            letters[i] = "" + (char)(i+'A');
        }
        letters[26] = "" + (char)('Æ');
        letters[27] = "" + (char)('Ø');
        letters[28] = "" + (char)('Å');
        letterInf = LayoutInflater.from(c);
    }

    @Override
    public int getCount() {
        return letters.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //create a button for the letter at this position in the alphabet
        Button letterBtn;
        if (convertView == null) {
            //inflate the button layout
            letterBtn = (Button)letterInf.inflate(R.layout.letter, parent, false);
        } else {
            letterBtn = (Button) convertView;
        }
        //set the text to this letter
        letterBtn.setText(letters[position]);
        return letterBtn;
    }
}

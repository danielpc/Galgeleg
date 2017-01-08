package danielpc.dk.galgeleg;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import android.content.res.Resources;
import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends Activity implements OnClickListener {

    private Button playBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Get the app's shared preferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int winCounter = prefs.getInt("winCounter", 0);

        // Update the TextView
        TextView text = (TextView) findViewById(R.id.won_textView);
        text.setText("Won " + winCounter);


        playBtn = (Button)findViewById(R.id.playBtn);
        playBtn.setOnClickListener(this);

        System.out.println("on create");
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.print("on resume");

        // Get the app's shared preferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int winCounter = prefs.getInt("winCounter", 0);

        // Update the TextView
        TextView text = (TextView) findViewById(R.id.won_textView);
        text.setText("Won " + winCounter);
    }

    @Override
    public void onClick(View view) {
        //handle clicks
        if(view == playBtn){
            Intent playIntent = new Intent(this, GameActivity.class);
            this.startActivity(playIntent);
        }
    }




}

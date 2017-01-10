package danielpc.dk.galgeleg;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ChooseWord extends Activity {

    private ListView mylist;
    private static GameLogic game;
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_word);

        // adapter
        game = new GameLogic();
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object... arg0) {
                System.out.println("Henter ord fra DRs server....");

                try {
                    game.hentOrdFraDr();
                    return "Ordene blev korrekt hentet fra DR's server";
                } catch (Exception e) {
                    e.printStackTrace();
                    return "Ordene blev ikke hentet korrekt: "+e;
                }
            }

            @Override
            protected void onPostExecute(Object o) {
                adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.activity_listview, game.getWords());
                mylist = (ListView)findViewById(R.id.words_listView);
                mylist.setAdapter(adapter);

                mylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // When clicked, show a toast with the TextView text or do whatever you need.
                        //Toast.makeText(getApplicationContext(), ((TextView) view).getText(), Toast.LENGTH_SHORT).show();
                        game.setWord((String) ((TextView) view).getText());
                        System.out.println("the word is " + game.getWord());

                        Intent playGame = new Intent(getBaseContext(), GameActivity.class);
                        getBaseContext().startActivity(playGame);

                    }
                });

            }
        }.execute();


    }

}

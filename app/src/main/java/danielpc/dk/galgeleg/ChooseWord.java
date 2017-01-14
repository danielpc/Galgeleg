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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class ChooseWord extends Activity {

    private ListView mylist;
    //private GameLogic game;
    private ArrayAdapter adapter;
    private List<String> words;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_word);

        words = new ArrayList<>();

        // adapter
        //game = new GameLogic();
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object... arg0) {
                System.out.println("Henter ord fra DRs server....");

                try {
                    hentOrdFraDr();
                    return "Ordene blev korrekt hentet fra DR's server";
                } catch (Exception e) {
                    e.printStackTrace();
                    return "Ordene blev ikke hentet korrekt: "+e;
                }
            }

            @Override
            protected void onPostExecute(Object o) {
                adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.activity_listview, words);
                mylist = (ListView)findViewById(R.id.words_listView);
                mylist.setAdapter(adapter);

                mylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent playGame = new Intent(getBaseContext(), GameActivity.class);
                        playGame.putExtra("myword", (String) ((TextView) view).getText());
                        startActivity(playGame);
                    }
                });
            }
        }.execute();


    }

    // network
    public static String hentUrl(String url) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
        StringBuilder sb = new StringBuilder();
        String linje = br.readLine();
        while (linje != null) {
            sb.append(linje + "\n");
            linje = br.readLine();
        }
        return sb.toString();
    }

    public void hentOrdFraDr() throws Exception {
        String data = hentUrl("http://dr.dk");
        data = data.substring(data.indexOf("<body")).
                replaceAll("<.+?>", " ").toLowerCase().replaceAll("[^a-zæøå]", " ").
                replaceAll(" [a-zæøå] "," "). // fjern 1-bogstavsord
                replaceAll(" [a-zæøå][a-zæøå] "," "); // fjern 2-bogstavsord
        words.addAll(new HashSet<String>(Arrays.asList(data.split(" "))));
    }

}

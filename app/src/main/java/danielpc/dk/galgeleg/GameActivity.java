package danielpc.dk.galgeleg;

import java.util.Random;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Daniel_ on 23/10/2016.
 */

public class GameActivity extends Activity {

    private static GameLogic game = new GameLogic();
    //the words
    private String[] words;
    //random for word selection
    private Random random;
    //the layout holding the answer
    private LinearLayout wordLayout;
    //text views for each letter in the answer
    private TextView[] charViews;
    //letter button grid
    private GridView letters;
    //letter button adapter
    private WordAdapter ltrAdapt;
    //body part images
    private ImageView[] bodyParts;
    //number of body parts or errors
    private int numParts=6;
    //current part - will increment when wrong answers are chosen
    private int currPart;
    //number of characters in current word
    private int numChars;
    //number correctly guessed
    private int numCorr;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //read answer words in
        Resources res = getResources();
        words = res.getStringArray(R.array.words);




        //get answer area
        wordLayout = (LinearLayout)findViewById(R.id.word);

        //get letter button grid
        letters = (GridView)findViewById(R.id.letters);

        //load body parts
        bodyParts = new ImageView[numParts];
        bodyParts[0] = (ImageView)findViewById(R.id.forkert1);
        bodyParts[1] = (ImageView)findViewById(R.id.forkert2);
        bodyParts[2] = (ImageView)findViewById(R.id.forkert3);
        bodyParts[3] = (ImageView)findViewById(R.id.forkert4);
        bodyParts[4] = (ImageView)findViewById(R.id.forkert5);
        bodyParts[5] = (ImageView)findViewById(R.id.forkert6);

        //hide all bodyparts
        for(int p = 0; p < numParts; p++) {
            bodyParts[p].setVisibility(View.INVISIBLE);
        }
        //start gameplay
        playGame();

    }

    //play a new game
    private void playGame(){


        //choose a random word from the array
        // String newWord = game.getShownWord();
        String newWord = "test";


        //create new array for character text views
        charViews = new TextView[game.getWord().length()];

        //remove any existing letters
        wordLayout.removeAllViews();


        //loop through characters
        for(int c=0; c<game.getWord().length(); c++){
            charViews[c] = new TextView(this);
            //set the current letter
            charViews[c].setText(""+game.getWord().charAt(c));
            //set layout
            charViews[c].setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT));
            charViews[c].setGravity(Gravity.CENTER);
            charViews[c].setTextColor(Color.WHITE);
            charViews[c].setBackgroundResource(R.drawable.letter_bg);
            //add to display
            wordLayout.addView(charViews[c]);
        }


        //reset adapter
        ltrAdapt=new WordAdapter(this);
        letters.setAdapter(ltrAdapt);

        //start part at zero
        currPart=0;


        //hide all parts
        for(int p=0; p<numParts; p++){
            bodyParts[p].setVisibility(View.INVISIBLE);
        }
    }

    public void wordPressedButton(View view){
        //find out which letter was pressed
        String letter=((TextView)view).getText().toString().toLowerCase();
        char guessChar = letter.charAt(0);
        //disable view
        view.setEnabled(false);
        view.setBackgroundResource(R.drawable.letter_down);

        //System.out.println("shown word " + game.getShownWord());

        if(game.isGameLost() || game.isWordGuessed()) {
            return;
        }


        // correct guess
        if(game.guess(guessChar)) {

            for(int k=0; k<game.getWord().length(); k++){
                if(game.getWord().charAt(k)==guessChar){
                    charViews[k].setTextColor(Color.BLACK);
                }

            }
            if(game.isWordGuessed()) {

                // Get the app's shared preferences
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                int winCounter = prefs.getInt("winCounter", 0);

                // Increment the counter
                prefs.edit().putInt("winCounter", ++winCounter).commit();


                //disable the buttons
                disableBtns();

                //let user know they have won, ask if they want to play again
                AlertDialog.Builder winBuild = new AlertDialog.Builder(this);
                winBuild.setTitle("YAY");
                winBuild.setMessage("You guessed the word in " + game.getGuesses().size() + " guesses");
                winBuild.setPositiveButton("Play Again",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                game.newGame();
                                GameActivity.this.playGame();
                            }});
                winBuild.setNegativeButton("Exit",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                GameActivity.this.finish();
                            }});
                winBuild.show();

            }
        } else {

            bodyParts[currPart].setVisibility(View.VISIBLE);
            currPart++;

            if(game.isGameLost()) {

                disableBtns();

                //let the user know they lost, ask if they want to play again
                AlertDialog.Builder loseBuild = new AlertDialog.Builder(this);
                loseBuild.setTitle("OOPS");
                loseBuild.setMessage("You lost. The word was: " + game.getWord());
                loseBuild.setPositiveButton("Play Again",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                game.newGame();
                                GameActivity.this.playGame();
                                //hide all bodyparts
                                for(int p = 0; p < numParts; p++) {
                                    bodyParts[p].setVisibility(View.INVISIBLE);
                                }
                            }});
                loseBuild.setNegativeButton("Exit",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                GameActivity.this.finish();
                            }});
                loseBuild.show();


            }
        }

    }

    //disable letter buttons
    public void disableBtns(){
        int numLetters = letters.getChildCount();
        for(int l=0; l<numLetters; l++){
            letters.getChildAt(l).setEnabled(false);
        }
    }

}

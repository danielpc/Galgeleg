package danielpc.dk.galgeleg;

import java.util.Random;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
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
    //the words
    private String[] words;
    //random for word selection
    private Random random;
    //store the current word
    private String currWord;
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

        //initialize random
        random = new Random();
        //initialize word
        currWord="";

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
        String newWord = words[random.nextInt(words.length)];
        //make sure the new word is new and not the same as the last time
        while(newWord.equals(currWord)) newWord = words[random.nextInt(words.length)];
        //update current word
        currWord = newWord;

        //create new array for character text views
        charViews = new TextView[currWord.length()];

        //remove any existing letters
        wordLayout.removeAllViews();

        //loop through characters
        for(int c=0; c<currWord.length(); c++){
            charViews[c] = new TextView(this);
            //set the current letter
            charViews[c].setText(""+currWord.charAt(c));
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
        //set word length and correct choices
        numChars=currWord.length();
        numCorr=0;

        //hide all parts
        for(int p=0; p<numParts; p++){
            bodyParts[p].setVisibility(View.INVISIBLE);
        }
    }

    public void wordPressedButton(View view){
        //find out which letter was pressed
        String letter=((TextView)view).getText().toString();
        char letterChar = letter.charAt(0);
        //disable view
        view.setEnabled(false);
        view.setBackgroundResource(R.drawable.letter_down);

        //check if correct
        boolean correct=false;
        for(int k=0; k<currWord.length(); k++){
            if(currWord.charAt(k)==letterChar){
                correct=true;
                numCorr++;
                charViews[k].setTextColor(Color.BLACK);
            }
        }
        System.out.println("correct " + correct + " numCorr " + numCorr + " numChars " + numChars + " wordpressed " + letter + " word " + currWord + " length "  + currWord.length());
        //check in case won
        if(correct){
            if(numCorr==numChars){
                //disable all buttons
                disableBtns();
                //let user know they have won, ask if they want to play again
                AlertDialog.Builder winBuild = new AlertDialog.Builder(this);
                winBuild.setTitle("YAY");
                winBuild.setMessage("You win!\n\nThe answer was:\n\n"+currWord);
                winBuild.setPositiveButton("Play Again",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                GameActivity.this.playGame();
                            }});
                winBuild.setNegativeButton("Exit",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                GameActivity.this.finish();
                            }});
                winBuild.show();
            }
        }
        //check if user still has guesses
        else if(currPart<numParts){
            //show next part
            bodyParts[currPart].setVisibility(View.VISIBLE);
            currPart++;
        }
        else{
            //user has lost
            disableBtns();
            //let the user know they lost, ask if they want to play again
            AlertDialog.Builder loseBuild = new AlertDialog.Builder(this);
            loseBuild.setTitle("OOPS");
            loseBuild.setMessage("You lose!\n\nThe answer was:\n\n"+currWord);
            loseBuild.setPositiveButton("Play Again",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
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

    //disable letter buttons
    public void disableBtns(){
        int numLetters = letters.getChildCount();
        for(int l=0; l<numLetters; l++){
            letters.getChildAt(l).setEnabled(false);
        }
    }

}

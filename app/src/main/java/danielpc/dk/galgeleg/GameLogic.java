package danielpc.dk.galgeleg;



import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class GameLogic {
    private static final int MAX_GUESSES = 6;

    private String word;
    private String shownWord;
    private List<Character> guesses;
    private int incorrectGuesses;
    public GameLogic() {

    }

    public void newGame(String newword) {
        word = newword;
        shownWord = "";

        for(int i = 0; i < word.length(); i++) {
            shownWord += '_';
        }

        guesses = new ArrayList<>();
        incorrectGuesses = 0;
    }

    public boolean guess(char guess) {
        guess = Character.toLowerCase(guess);

        char[] chars = shownWord.toCharArray();
        boolean ret = false;

        for(int i = 0; i < word.length(); i++) {
            if(word.charAt(i) == guess) {
                chars[i] = guess;
                ret = true;
            }
        }

        shownWord = String.valueOf(chars);

        guesses.add(guess);

        if(!ret) {
            incorrectGuesses++;
        }

        return ret;
    }

    public boolean alreadyGuessed(char guess) {
        return guesses.contains(guess);
    }

    public List<Character> getGuesses() {
        return guesses;
    }

    public int getIncorrectGuesses() {
        return incorrectGuesses;
    }

    public boolean isGameLost() {
        return incorrectGuesses == MAX_GUESSES;
    }

    public boolean isWordGuessed() {
        return shownWord.equals(word);
    }

    public String getWord() {
        return word;
    }

    public String getShownWord() {
        return shownWord;
    }

    public void setWord(String word) {
        this.word = word;
    }


}

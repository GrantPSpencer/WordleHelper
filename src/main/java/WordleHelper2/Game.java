package WordleHelper2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ProcessBuilder.Redirect;
import java.nio.BufferOverflowException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import wordlehelper.WordList2;

public class Game {
    String answer;
    private final int LENGTH = 5;
    public int remainingGuesses;
    // public LinkedList<String> possibleWords;
    public boolean gameWon;
    HashSet<String> usedWords;
    public boolean gameOver;

    private static final String GREEN_SQUARE = "\uD83D\uDFE9";
    private static final String YELLOW_SQUARE = "\uD83D\uDFE8";
    private static final String RED_SQUARE = "\uD83D\uDFE5";
    
    
    

    public Game(String answer) throws FileNotFoundException {
        this.answer = answer;
        this.gameOver = false;
        this.gameWon = false;
        this.remainingGuesses = 6;
        // possibleWords = WordList.guessList();
        usedWords = new HashSet<>();
    }    

    public int[] guess(String guess) {
        if (guess.length() != 5) {
            // System.out.println("Incorrect length guess");
            return new int[] {0};
        }
     
        
        if (this.gameWon) {
            // System.out.println("Game already won from guess: " + this.answer + " after " + (6-remainingGuesses) + " guesses");
            return new int[] {1};
        }
        if (this.gameOver) {
            // System.out.println("Game already over");
            return new int[] {-1};
        }

        usedWords.add(guess);
        this.remainingGuesses--;
        if (guess.equals(this.answer)) {
            this.gameWon = true;
            this.gameOver = true;
            for (int i = 0; i < 5; i++) {
                System.out.print(GREEN_SQUARE);
            }
            // System.out.print("\n");
            System.out.println(" " + guess);
            // System.out.println("Game won with guess: " + guess + ", after " + (6-remainingGuesses) + " guesses");
            return new int[] {1};
        }

        
        int[] response = judgeGuess(guess);

        
        if (remainingGuesses == 0) {
            // System.out.println("Game lost, word was: " + answer);
            this.gameOver = true;
            return new int[] {-1};
        }
        return response;



        
    }


    private int[] judgeGuess(String guess) {

        int[] response = new int[5];


        HashMap<Character, Integer> answerCharCount = new HashMap<>();

        for (int i = 0; i < this.LENGTH; i++) {
            char answerChar = this.answer.charAt(i);
            answerCharCount.put(answerChar, answerCharCount.getOrDefault(answerChar, 0)+1);

            char guessChar = guess.charAt(i);
            if (this.answer.charAt(i) == guessChar) {
                response[i] = 1;
                answerCharCount.put(guessChar, answerCharCount.get(guessChar)-1);
            }
        }

        for (int i = 0; i < this.LENGTH; i++) {
            if (response[i] == 1) {
                continue;
            }
            char guessChar = guess.charAt(i);
            if (answer.contains(String.valueOf(guessChar)) && answerCharCount.get(guessChar) > 0) {
                answerCharCount.put(guessChar, answerCharCount.get(guessChar)-1);
                response[i] = 0;
            } else {
                response[i] = -1;
            }
        }
        // System.out.println("Guess is: " + guess + " and answer is: " + answer);
        for (int i = 0; i < 5; i++) {
            switch (response[i]) {
                case 1: 
                    System.out.print(GREEN_SQUARE);
                    break;
                    case 0: 
                        System.out.print(YELLOW_SQUARE);
                        break;
                    case -1: 
                        System.out.print(RED_SQUARE);
                        break;
            }
        }
        System.out.print(" " + guess + "\n");
        return response;
    }


    public static void main(String[] args) throws IOException {
        Game game = new Game("stare");
        
        BufferedReader reader = new BufferedReader( new InputStreamReader(System.in));
        
        while (!game.gameWon && game.remainingGuesses > 0) {
            System.out.print("Guess a word: ");
            String guess = reader.readLine();
            game.guess(guess);
        }
    }
    
}



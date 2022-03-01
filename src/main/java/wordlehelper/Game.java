package wordlehelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Game {
    String answer;
    private final int LENGTH = 5;
    public int remainingGuesses;
    private String[] possibleWords;

    public Game(String answer) {
        this.answer = answer;
        remainingGuesses = 5;
        possibleWords = WordList.guessList;
    }

    public String[] filterByGuess(String[] list, String guess) {

        HashMap<Integer, Character> green = new HashMap<>();
        // HashMap<Integer, Character> yellow = new HashMap<>();
        HashMap<Character, Integer> yellowCount = new HashMap<>();
        HashSet<Character> reds = new HashSet<>();
        
        int[] responses = judgeGuess(guess);

        ArrayList<String> newList = new ArrayList<>();

        for (String possibleWord : list) {
            if (isPossibleWord(possibleWord, guess, responses)) {
                newList.add(possibleWord);
            }
        }


        return newList.toArray(new String[newList.size()]);


    }



    private boolean isPossibleWord(String possibleWord, String guess, int[] responses) {
        HashMap<Integer, Character> greens = new HashMap<>();
        HashMap<Character, Integer> yellowCounts = new HashMap<>();
        HashSet<Character> reds = new HashSet<>();

        // System.out.println(Arrays.toString(responses));
        // for (int i = 0; i < responses.length; i++) {

        // }

        for (int i = 0; i < this.LENGTH; i++) {
            if (responses[i] == 1) {
                greens.put(i, guess.charAt(i));
            } else if (responses[i] == 0) {
                // yellowCounts.put(guess.charAt(i), yellowCounts.getOrDefault(guess.charAt(i), 0)+1);
                yellowCounts.put(guess.charAt(i), this.LENGTH);
            } else {
                reds.add(guess.charAt(i));
                // if (reds.contains(guess.charAt(i))) {
                //     yellowCounts.put(guess.charAt(i), - Math.abs(yellowCounts.get(guess.charAt(i))));
                // }
            }

            // only set limit to yellows 
        }

        for (int i = 0; i < this.LENGTH; i++) {
            char c = possibleWord.charAt(i);
            // if (possibleWord.equals("cigar")) {
            //     System.out.println("NOWNOWNOWNOW");
            // }

            if (greens.containsKey(i)) {
                if (greens.get(i) != c) {
                    // System.out.println(possibleWord + " failed at green " + c);
                    return false;
                } else {
                    continue;
                }
            }
            // check yellow
            if (yellowCounts.containsKey(c)) {
                // System.out.println(yellowCounts.get(c));
                // if (yellowCounts.get(c) < 1) {
                //     System.out.println(possibleWord + " failed at yellow " + c + " at index " + i);
                //     return false;
                // } else {
                //     yellowCounts.put(c, yellowCounts.get(c)-1);
                //     continue;
                // }

                // if same index and letter, then false
                if (c == guess.charAt(i)) {
                    return false;
                }
                if (yellowCounts.get(c) == 0) {
                    return false;
                }
                yellowCounts.put(c, yellowCounts.get(c)+1);
            } else {
                if (reds.contains(c)) {
                    // System.out.println(possibleWord + " failed at red " + c);
                    return false;
                }
            }
            
        }
        for (int yellowCount : yellowCounts.values()) {
            if (yellowCount != 0 && yellowCount <= this.LENGTH) {
                return false;
            }
        }
        return true;
    }


    private int[] judgeGuess(String guess) {

        int[] response = new int[5];


        HashMap<Character, Integer> answerCharCount = new HashMap<>();
        // HashMap<Character, Integer> guessCharCount = new HashMap<>();
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
        return response;
    }

    public double getBits(String guess) {
        String[] newList = this.filterByGuess(WordList.guessList, guess);
        
        double probability = ((double) newList.length) / ((double) WordList.answerList.length);
        double bits = Math.log(1/probability) / Math.log(2);
        if (bits > 100) {
            System.out.println("size is " + newList.length);
            System.out.println(bits + " at " + guess + " " + this.answer);
        }
        return bits;
    }

    public boolean guess(String guess) {
        System.out.println("\nGuessed: " + guess);
        if (remainingGuesses <= 0) {
            System.out.println("Game already over");
            return false;
        }
        remainingGuesses--;
        if (guess.equals(this.answer)) {
            return true;
        }
        String[] newList = filterByGuess(this.possibleWords, guess);
        
        double probability = ((double) newList.length) / ((double) this.possibleWords.length);
        double bits = Math.log(1/probability) / Math.log(2);

        System.out.println("Bits gained: " + bits);

        this.possibleWords = newList;
        if (this.possibleWords.length > 5) {
            System.out.println(this.possibleWords.length + " possible words left");
        } else {
            System.out.print("Possible Words: ");
            for (String word : this.possibleWords) {
                System.out.print(word + ", ");
            }
            System.out.print("\n");
        }
        return false;
    }


    public static void main(String[] args) {
        Game game = new Game("awake");

        Scanner input = new Scanner(System.in);
        // while (game.remainingGuesses > 0) {
        //     System.out.println("Please guess a word");
        //     String inputString = input.nextLine();
        //     game.guess(inputString);
        // }
        game.guess("raise");
        boolean gameWon = false;
        HashMap<String, Double> avgScoreMap = new HashMap<>();
        while (gameWon == false && game.remainingGuesses > 0) {
            avgScoreMap.clear();
            for (int i = 0; i < game.possibleWords.length; i++) {
                double sum = 0;
                for (int j = 0; j < game.possibleWords.length; j++) {
                    if (i == j) {
                        continue;
                    }
                    SimpleGame simpleGame = new SimpleGame(game.possibleWords[j]);
                    sum += simpleGame.getBits(game.possibleWords[i], game.possibleWords);
                }
                avgScoreMap.put(game.possibleWords[i], sum/4);
            }

            // System.out.println(avgScoreMap.size());
            Double max = -Double.MAX_VALUE;
            String maxString = "";
            for (Map.Entry entry : avgScoreMap.entrySet()) {
                if (max < (Double) entry.getValue()) {
                    max = (Double) entry.getValue();
                    maxString = (String) entry.getKey();
                }
            }

            // System.out.println("Expected bits: " + max); 
            gameWon = game.guess(maxString);
        }
        if (gameWon) {
            System.out.println("Game won in " + (5 - game.remainingGuesses) + " guess(es)");
        } else {
            System.out.println("Game lost");
        }
        





    }
}

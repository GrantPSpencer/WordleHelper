package wordlehelper;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;

import WordleHelper2.WordList;

public class SimpleGame 
{
    
    String answer;
    private final int LENGTH = 5;

    public SimpleGame(String answer) {
        this.answer = answer;
    }

    public LinkedList<String> filterByGuess(LinkedList<String> list, String guess) {

        HashMap<Integer, Character> green = new HashMap<>();
        // HashMap<Integer, Character> yellow = new HashMap<>();
        HashMap<Character, Integer> yellowCount = new HashMap<>();
        HashSet<Character> reds = new HashSet<>();
        
        int[] responses = judgeGuess(guess);

        LinkedList<String> newList = new LinkedList<>();

        for (String possibleWord : list) {
            if (isPossibleWord(possibleWord, guess, responses)) {
                newList.add(possibleWord);
            }
        }


        return newList;


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

    public double getBits(String guess, LinkedList<String> possibleWordsList) {
       LinkedList<String> newList = this.filterByGuess(possibleWordsList, guess);
        
        double probability = ((double) newList.size()) / ((double) possibleWordsList.size());
        double bits = Math.log(1/probability) / Math.log(2);
        // if (bits > 100) {
        //     System.out.println("size is " + newList.size());
        //     System.out.println(bits + " at " + guess + " " + this.answer);
        // }
        return bits;
    }

    
    public static void main( String[] args ) throws FileNotFoundException
    {

        String guess = "awake";
        String answer = "cigar";
        SimpleGame game = new SimpleGame(answer);
        LinkedList<String> newList = game.filterByGuess(WordList.guessList(), guess);
        for (String str : newList) {
            System.out.println("list here");
            System.out.println(str);
        }

        
        double probability = ((double) newList.size()) / ((double) WordList.guessList().size());

        // System.out.println(probability);
        // double bits = Math.log(1/probability) / Math.log(2);
        // System.out.println(bits);
    }
}

package wordlehelper;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;

public class SimpleGame 
{
    
    String answer;
    private final int LENGTH = 5;

    public SimpleGame(String answer) {
        this.answer = answer;
    }

    public ArrayList<String> filterByGuess(String[] list, String guess) {

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


        return newList;


    }



    private boolean isPossibleWord(String possibleWord, String guess, int[] responses) {
        HashMap<Integer, Character> greens = new HashMap<>();
        HashMap<Character, Integer> yellowCounts = new HashMap<>();
        HashSet<Character> reds = new HashSet<>();

        for (int i = 0; i < this.LENGTH; i++) {
            if (responses[i] == 1) {
                greens.put(i, guess.charAt(i));
            } else if (responses[i] == 0) {
                yellowCounts.put(guess.charAt(i), yellowCounts.getOrDefault(guess.charAt(i), 0)+1);
            } else {
                reds.add(guess.charAt(i));
            }
        }

        for (int i = 0; i < this.LENGTH; i++) {
            char c = possibleWord.charAt(i);

            if (greens.containsKey(i)) {
                if (greens.get(i) != c) {
                    return false;
                } else {
                    continue;
                }
            }
            // check yellow
            if (yellowCounts.containsKey(c)) {
                if (yellowCounts.get(c) < 1) {
                    return false;
                } else {
                    yellowCounts.put(c, yellowCounts.get(c)-1);
                    continue;
                }
            }
            //check reds
            if (reds.contains(c)) {
                return false;
            }
        }
        for (int yellowCount : yellowCounts.values()) {
            if (yellowCount > 0) {
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
        ArrayList<String> newList = this.filterByGuess(WordList.list, guess);
        double probability = ((double) newList.size()) / ((double) WordList.list.length);
        double bits = Math.log(1/probability) / Math.log(2);
        return bits;
    }

    
    public static void main( String[] args )
    {

        String guess = "adobe";
        String answer = "abode";
        SimpleGame game = new SimpleGame(answer);
        ArrayList<String> newList = game.filterByGuess(WordList.list, guess);
        for (String str : newList) {
            System.out.println(str);
        }

        
        double probability = ((double) newList.size()) / ((double) WordList.list.length);
        // System.out.println(probability);
        double bits = Math.log(1/probability) / Math.log(2);
        System.out.println(bits);
    }
}

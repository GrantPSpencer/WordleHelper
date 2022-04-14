package wordlehelper;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import WordleHelper2.WordList;

public class Game2 {
    String answer;
    private final int LENGTH = 5;
    public int remainingGuesses;
    public LinkedList<String> possibleWords;
    public LinkedList<String> unusedWords;

    public Game2(String answer) throws FileNotFoundException {
        this.answer = answer;
        remainingGuesses = 5;
        try {
            WordList.guessList();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        possibleWords = WordList.guessList();
        unusedWords = WordList.guessList();
    }

    public LinkedList<String> filterByGuess(LinkedList<String> list, String guess) {

        // HashMap<Integer, Character> green = new HashMap<>();
        // HashMap<Character, Integer> yellowCount = new HashMap<>();
        // HashSet<Character> reds = new HashSet<>();
        
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



        for (int i = 0; i < this.LENGTH; i++) {
            if (responses[i] == 1) {
                greens.put(i, guess.charAt(i));
                yellowCounts.put(guess.charAt(i), yellowCounts.getOrDefault(guess.charAt(i), 0)+1);
            } else if (responses[i] == 0) {
                yellowCounts.put(guess.charAt(i), yellowCounts.getOrDefault(guess.charAt(i), 0)+1);
            } else {
                reds.add(guess.charAt(i));

            }

            // only set limit to yellows 
        }

        for (int i = 0; i < this.LENGTH; i++) {
            char c = possibleWord.charAt(i);
 

            if (greens.containsKey(i)) {
                if (greens.get(i) != c) {
                    return false;
                } else {
                    yellowCounts.put(c, yellowCounts.get(c)-1);
                    continue;
                }
            }
            // check yellow
            if (yellowCounts.containsKey(c)) {

                // if same index and letter, then false
                if (c == guess.charAt(i)) {
                    return false;
                }
                if (yellowCounts.get(c) < 1 && reds.contains(c)) {
                    return false;
                }
                yellowCounts.put(c, yellowCounts.get(c)-1);
            } else {
                if (reds.contains(c)) {
                    return false;
                }
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

    // public double getBits(String guess) {
    //     LinkedList<String> newList = this.filterByGuess(WordList.guessList, guess);
        
    //     double probability = ((double) newList.size()) / ((double) WordList.guessList.size());
    //     double bits = Math.log(1/probability) / Math.log(2);
    //     if (bits > 100) {
    //         System.out.println("size is " + newList.size());
    //         System.out.println(bits + " at " + guess + " " + this.answer);
    //     }
    //     return bits;
    // }

    public boolean guess(String guess) {
        System.out.println("\nGuessed: " + guess);
        // if (remainingGuesses <= 0) {
        //     // System.out.println("Game already over");
        //     return false;
        // }
        remainingGuesses--;
        if (guess.equals(this.answer)) {
            return true;
        }
        LinkedList<String> newList = filterByGuess(this.possibleWords, guess);
        
        double probability = ((double) newList.size()) / ((double) this.possibleWords.size());
        double bits = Math.log(1/probability) / Math.log(2);

        // System.out.println("Bits gained: " + bits);

        this.possibleWords = newList;
        this.unusedWords.remove(guess);
        if (this.possibleWords.size() > 5) {
            System.out.println(this.possibleWords.size() + " possible words left");
        } 
        else {
            System.out.print("Possible Words: ");
            for (String word : this.possibleWords) {
                System.out.print(word + ", ");
            }
            System.out.print("\n");
        }
        return false;
    }


    public static void main(String[] args) throws FileNotFoundException {
        Game2 game2 = new Game2("humph");

        // Scanner input = new Scanner(System.in);
        // while (game2.remainingGuesses > 0) {
        //     System.out.println("Please guess a word");
        //     String inputString = input.nextLine();
        //     game2.guess(inputString);
        // }
        boolean gameWon = game2.guess("stare");

        // Scanner scanner = new Scanner(System.in);
        // String line = scanner.nextLine();
        // while (line != null) {
        //     game2.guess(line);
        //     line = scanner.nextLine();
        // }
        HashMap<String, Double> avgScoreMap = new HashMap<>();
        while (gameWon == false) {
            avgScoreMap.clear();
            for (int i = 0; i < game2.unusedWords.size(); i++) {
                double sum = 0;
                for (int j = 0; j < game2.possibleWords.size(); j++) {
                    if (game2.unusedWords.get(i) == game2.possibleWords.get(j)) {
                         continue;
                    }
                    SimpleGame simpleGame = new SimpleGame(game2.possibleWords.get(j));
                    sum += simpleGame.getBits(game2.unusedWords.get(i), game2.possibleWords);
                }
                avgScoreMap.put(game2.unusedWords.get(i), sum/4);
                System.out.print('\r' +""+i + "/" + game2.unusedWords.size());
            }

            // System.out.println(avgScoreMap.size());
            Double max = -Double.MAX_VALUE;
            String maxString = "";
            for (Map.Entry entry : avgScoreMap.entrySet()) {
                Double newScore;
                if (game2.possibleWords.contains(entry.getKey())) {
                    newScore = (Double)entry.getValue() + (1/game2.possibleWords.size());
                } else {
                    newScore = (Double) entry.getValue();
                }
                if (max < newScore) {
                    max = newScore;
                    maxString = (String) entry.getKey();
                }
            }


            // System.out.println("Expected bits: " + max); 
            gameWon = game2.guess(maxString);
        }
        if (gameWon) {
            
            if (game2.remainingGuesses == 4) {
                System.out.println("Game won in " + (5 - game2.remainingGuesses) + " guess");
            } else {
                System.out.println("Game won in " + (5 - game2.remainingGuesses) + " guesses");
            }

                
        } else {
            System.out.println("Game lost");
        }
        

            



    }
}
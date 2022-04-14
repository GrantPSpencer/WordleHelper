package WordleHelper2;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;

// import java.util.LinkedList;

public class WordleBot implements Runnable {
    // LinkedList<String> guessList;
    // LinkedList<String> answerList;
    // int listLength;

    @Override
    public void run() {
        System.out.println("test run");
        try {
            guessList = WordList.guessList();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    private LinkedList<String> possibleWords;
    private LinkedList<String> guessList;
    private final int LENGTH = 5;

    public WordleBot() throws FileNotFoundException {
        
    }

   public int playGame(Game game) throws FileNotFoundException {
       this.possibleWords = WordList.guessList();
        String maxString = "raise";
       int[] responses = game.guess(maxString);
    //    this.possibleWords = filterByGuess("raise", responses);
        int guessCount = 1;

       HashMap<String, Double> avgScoreMap = new HashMap<>();
       while (!game.gameOver) {
            this.possibleWords = filterByGuess(maxString, responses);
            avgScoreMap.clear();
            for (int i = 0; i < this.possibleWords.size(); i++) {
                double sum = 0;
                String wordI = this.possibleWords.get(i);
                if (game.usedWords.contains(wordI)) {
                    continue;
                }
                for (int j = 0; j < this.possibleWords.size(); j++) {
                    String wordJ = this.possibleWords.get(j);
                    if (wordI.equals(wordJ)) {
                        continue;
                    }
                    sum += getBits(wordI, wordJ);
                }
                //had sum/4 before, not sure why
                avgScoreMap.put(wordI, sum/this.possibleWords.size()-1);
                System.out.print('\r' +""+i + "/" + (this.possibleWords.size() - game.usedWords.size()));
           }

            Double max = -Double.MAX_VALUE;
            
            for (Map.Entry entry : avgScoreMap.entrySet()) {
                if (max < (Double) entry.getValue()) {
                    max = (Double) entry.getValue();
                    maxString = (String) entry.getKey();
                }
            }
            guessCount++;
            // System.out.println("Guessed: " + maxString);
            responses = game.guess(maxString);
            
           
       }
       if (!game.gameWon) {
           guessCount++;
       }
       return guessCount;
   }


    private double getBits(String wordI, String wordJ) {

        int[] responses = judgeGuessToAnswer(wordJ, wordI);
        LinkedList<String> newList = this.filterByGuess(wordJ, responses);
        double probability = ((double) newList.size()) / ((double) this.possibleWords.size());

        double bits = Math.log(1/probability) / Math.log(2);
        return bits;
    }

    private LinkedList<String> filterByGuess(String guess, int[] responses) {

        HashMap<Integer, Character> green = new HashMap<>();
        HashMap<Character, Integer> yellowCount = new HashMap<>();
        HashSet<Character> reds = new HashSet<>();

        LinkedList<String> newList = new LinkedList<>();

        for (String possibleWord : this.possibleWords) {
            if (isPossibleWord(possibleWord, guess, responses)) {
                newList.add(possibleWord);
            }
        }
        return newList;
    }
    
    private int[] judgeGuessToAnswer(String guess, String answer) {

        int[] response = new int[this.LENGTH];


        HashMap<Character, Integer> answerCharCount = new HashMap<>();

        for (int i = 0; i < this.LENGTH; i++) {
            char answerChar = answer.charAt(i);
            answerCharCount.put(answerChar, answerCharCount.getOrDefault(answerChar, 0)+1);

            char guessChar = guess.charAt(i);
            if (answer.charAt(i) == guessChar) {
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

            if (greens.containsKey(i)) {
                if (greens.get(i) != c) {
                    return false;
                } else {
                    continue;
                }
            }
            if (yellowCounts.containsKey(c)) {
                if (c == guess.charAt(i)) {
                    return false;
                }
                if (yellowCounts.get(c) == 0) {
                    return false;
                }
                yellowCounts.put(c, yellowCounts.get(c)+1);
            } else {
                if (reds.contains(c)) {
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

    public static void main(String[] args) throws FileNotFoundException {
        WordleBot bot = new WordleBot();
        bot.playGame(new Game("paper"));
    }




}



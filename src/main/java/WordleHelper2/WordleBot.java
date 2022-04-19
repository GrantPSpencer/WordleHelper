package WordleHelper2;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;

// import java.util.LinkedList;

public class WordleBot {
    // LinkedList<String> guessList;
    // LinkedList<String> answerList;
    // int listLength;


    private LinkedList<String> possibleWords;
    private LinkedList<String> unusedWords;
    private LinkedList<String> guessList;
    private final int LENGTH = 5;

    public WordleBot() throws FileNotFoundException {
        // this.possibleWords = WordList.guessList();
        // this.unusedWords = WordList

    }

   public int playGame(Game game) throws FileNotFoundException {
        // System.out.println("normal mode called");
        this.possibleWords = WordList.guessList();
        this.unusedWords = WordList.guessList();
        String maxString = "raise";
        int[] responses = game.guess(maxString);
        // System.out.println("Guessed: " +  maxString);
        // this.possibleWords = filterByGuess("raise", responses);
        int guessCount = 1;

       HashMap<String, Double> avgScoreMap = new HashMap<>();

       while (!game.gameOver) {
            // this.possibleWords = filterByGuess(maxString, responses);
            this.possibleWords = filterByGuess(maxString, responses);
            // System.out.println(this.possibleWords.size());
            avgScoreMap.clear();
            for (int i = 0; i < this.unusedWords.size(); i++) {
                double sum = 0;
                String unusedWord = this.unusedWords.get(i);
   
                for (int j = 0; j < this.possibleWords.size(); j++) {
                    String possibleWord = this.possibleWords.get(j);
                    sum += getBits(unusedWord, possibleWord);
                }
                
                avgScoreMap.put(unusedWord, sum);
                // System.out.print('\r' +""+i + "/" + (this.possibleWords.size() - game.usedWords.size()));
           }

            Double max = -Double.MAX_VALUE;
            Double newScore;
            for (Map.Entry entry : avgScoreMap.entrySet()) {
                if (this.possibleWords.contains(entry.getKey())) {
                    newScore = (Double)entry.getValue() + (1/this.possibleWords.size())*4;
                } else {
                    newScore = (Double) entry.getValue();
                }
                
                if (max < newScore) {
                    max = newScore;
                    maxString = (String) entry.getKey();
                }
            }
            guessCount++;
            // System.out.println("Guessed: " + maxString);
            responses = game.guess(maxString);
            this.unusedWords.remove(maxString);
            
            
           
       }
       if (!game.gameWon) {
           guessCount++;
       }
       return guessCount;
   }

   public double getActualBits(int prevSize, int currentSize) {
    
    double probability = ((double) currentSize) / ((double) prevSize);

    double bits = Math.log(1/probability) / Math.log(2);

    return bits;
   }


    public int playHardmode(Game game) throws FileNotFoundException {
        // System.out.println("hardmode called");
        this.possibleWords = WordList.guessList();
        this.unusedWords = WordList.guessList();
        String maxString = "raise";
        int[] responses = game.guess(maxString);
        // System.out.println("Guessed: " +  maxString);
        // this.possibleWords = filterByGuess("raise", responses);
        int guessCount = 1;


        HashMap<String, Double> avgScoreMap = new HashMap<>();

        while (!game.gameOver) {
            // this.possibleWords = filterByGuess(maxString, responses);
            this.possibleWords = filterByGuess(maxString, responses);
            // System.out.println(this.possibleWords.size());
            avgScoreMap.clear();
            for (int i = 0; i < this.possibleWords.size(); i++) {
                double sum = 0;
                String guessWord = this.possibleWords.get(i);
                for (int j = 0; j < this.possibleWords.size(); j++) {
                    String answerWord = this.possibleWords.get(j);
                    sum += getBits(guessWord, answerWord);
                }
                
                avgScoreMap.put(guessWord, sum);
                // System.out.print('\r' +""+i + "/" + (this.possibleWords.size() - game.usedWords.size()));
            }

            Double max = -Double.MAX_VALUE;
            Double newScore;
            for (Map.Entry entry : avgScoreMap.entrySet()) {
                if (this.possibleWords.contains(entry.getKey())) {
                    newScore = (Double)entry.getValue() + (1/this.possibleWords.size())*4;
                } else {
                    newScore = (Double) entry.getValue();
                }
                
                if (max < newScore) {
                    max = newScore;
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

    public HashMap<String, String[]> analyzePerformance(Game game) throws FileNotFoundException {
        // System.out.println("analyzer called");

        HashMap<String, String[]> responseMap = new HashMap<>();
        HashMap<String, Double> avgScoreMap = new HashMap<>();

        this.possibleWords = WordList.guessList();
        this.unusedWords = WordList.guessList();
        String maxString = "raise";
        int[] responses = game.guess(maxString);
        int guessCount = 1;
        int previousListSize = this.possibleWords.size();
        // this.possibleWords = filterByGuess("raise", responses);

        double max = 5.876;
        double actualBits = this.getActualBits(previousListSize, this.possibleWords.size());
        double expectedBits = max;
        String guessNumber = "Guess" + guessCount;
        responseMap.put("Guess1", new String[] {maxString, String.valueOf(actualBits), String.valueOf(expectedBits)});


        



       while (!game.gameOver) {
            guessNumber = "Guess" + guessCount;
            if (guessCount > 1) {

                if(this.possibleWords.contains(maxString))  {
                    max = max - (4/this.possibleWords.size());
                    System.out.println("probablity of being answer*4 = " +(4/this.possibleWords.size()));
                }

                expectedBits = (max/(this.possibleWords.size()));
            } 

            previousListSize = this.possibleWords.size();
            this.possibleWords = filterByGuess(maxString, responses);
            actualBits = this.getActualBits(previousListSize, this.possibleWords.size());

            responseMap.put(guessNumber, new String[] {maxString, String.valueOf(actualBits), String.valueOf(expectedBits)});

            avgScoreMap.clear();
            for (int i = 0; i < this.unusedWords.size(); i++) {
                double sum = 0;
                String unusedWord = this.unusedWords.get(i);
   
                for (int j = 0; j < this.possibleWords.size(); j++) {
                    String possibleWord = this.possibleWords.get(j);
                    sum += getBits(unusedWord, possibleWord);
                }
                
                avgScoreMap.put(unusedWord, sum);
                // System.out.print('\r' +""+i + "/" + (this.possibleWords.size() - game.usedWords.size()));
           }

            max = -Double.MAX_VALUE;
            Double newScore;
            for (Map.Entry entry : avgScoreMap.entrySet()) {
                if (this.possibleWords.contains(entry.getKey())) {
                    newScore = (Double)entry.getValue() + (4/this.possibleWords.size());
                } else {
                    newScore = (Double) entry.getValue();
                }
                if (max < newScore) {
                    max = newScore;
                    maxString = (String) entry.getKey();
                }

            }
            guessCount++;
            responses = game.guess(maxString);
            this.unusedWords.remove(maxString);
            
            
           
       }
       if (!game.gameWon) {
           guessCount++;
       }
       
       if (game.gameWon) {
        //    System.out.println("Probablity of winning was: " + (1/this.possibleWords.size()));
        responseMap.put("answer", new String[] {maxString});
       }

       responseMap.put("guessCount",new String[] {String.valueOf(guessCount)});
       return responseMap;
        

    }



    private double getBits(String guessWord, String answerWord) {

        int[] responses = judgeGuessToAnswer(guessWord, answerWord);
        LinkedList<String> newList = this.filterByGuess(guessWord, responses);
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
        // bot.playGame(new Game("flair"));
        System.out.println("\033[H\033[2J");
        System.out.println("Today's word is: 'humph'");
        bot.playHardmode(new Game("humph"));
        System.out.println("Game won in 4 guesses");
    }




}



package wordlehelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import WordleHelper2.WordList;

public class PerformanceSimulator2 {

    public int playGame(Game2 game2) {
        boolean gameWon = game2.guess("raise");
        HashMap<String, Double> avgScoreMap = new HashMap<>();
        int guessCount = 1;
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
                    newScore = (Double)entry.getValue() + (1/game2.possibleWords.size())*4;
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
            ++guessCount;
        }
        return guessCount;
    }

    public static void main(String[] args) throws IOException {
        // iterate over all possible answers, create game of each, and then play it, and record score

        int sum = 0;
        HashMap<Integer, Integer> scoreMap = new HashMap<>();
        PerformanceSimulator2 sim = new PerformanceSimulator2();
        for (int i = 0; i < WordList.answerList.length; i++) {
            
            Game2 game2 = new Game2(WordList.answerList[i]);
            int toAdd = sim.playGame(game2);
            sum += toAdd;
            scoreMap.put(toAdd, scoreMap.getOrDefault(toAdd, 0)+1);
            System.out.println("Current Word: " + WordList.answerList[i] + " / Average Score: " + 
            (double)sum/(double)(i+1) + " / Progress: " + (i+1) + "/" + WordList.answerList.length);
        }
        System.out.println("Average performance is: " + ((double)sum / (double)WordList.answerList.length));
        File file = new File("src/main/java/performance_sim_results.txt");
        FileWriter writer = new FileWriter(file);
        writer.write("Sum is: " + sum + "\n");
        writer.write("Length is: " + WordList.answerList.length + "\n");
        writer.write("Average performance is: " + ((double)sum / (double)WordList.answerList.length)+"\n");
        writer.write("Score Distribution is: \n");
        for (Map.Entry entry : scoreMap.entrySet()) {
            writer.write(entry.getKey() + ": " + entry.getValue() + "\n");
        }
        writer.flush();
        writer.close();
        
    }
}

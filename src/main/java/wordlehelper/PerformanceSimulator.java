package wordlehelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PerformanceSimulator {

    public int playGame(Game game) {
        boolean gameWon = game.guess("raise");
        HashMap<String, Double> avgScoreMap = new HashMap<>();
        int guessCount = 0;
        while (gameWon == false) {
            // rank the possible guesses
            avgScoreMap.clear();
            for (int i = 0; i < game.possibleWords.size(); i++) {
                double sum = 0;
                for (int j = 0; j < game.possibleWords.size(); j++) {
                    if (i == j) {
                        continue;
                    }
                    SimpleGame simpleGame = new SimpleGame(game.possibleWords.get(j));
                    sum += simpleGame.getBits(game.possibleWords.get(i), game.possibleWords);
                }
                avgScoreMap.put(game.possibleWords.get(i), sum/4);
            }

            
            //choose best guess
            Double max = -Double.MAX_VALUE;
            String maxString = "";
            for (Map.Entry entry : avgScoreMap.entrySet()) {
                if (max < (Double) entry.getValue()) {
                    max = (Double) entry.getValue();
                    maxString = (String) entry.getKey();
                }
            }
            
            // guess
            gameWon = game.guess(maxString);
            ++guessCount;
        }
        return guessCount;
    }

    public static void main(String[] args) throws IOException {
        // iterate over all possible answers, create game of each, and then play it, and record score

        int count = 0;
        PerformanceSimulator sim = new PerformanceSimulator();
        for (int i = 0; i < WordList.answerList.length; i++) {
            
            Game game = new Game(WordList.answerList[i]);
            count += sim.playGame(game);
            System.out.println("Current Word: " + WordList.answerList[i+1] + " / Average Score: " + 
            (double)count/(double)(i+1) + " / Progress: " + (i+1) + "/" + WordList.answerList.length);
        }
        System.out.println("Average performance is: " + ((double)count / (double)WordList.answerList.length));
        File file = new File("src/main/java/performance_sim_results.txt");
        FileWriter writer = new FileWriter(file);
        writer.write("Count is: " + count + "\n");
        writer.write("Length is: " + WordList.answerList.length + "\n");
        writer.write("Average performance is: " + ((double)count / (double)WordList.answerList.length)+"\n");
        writer.flush();
        writer.close();
        
    }
}

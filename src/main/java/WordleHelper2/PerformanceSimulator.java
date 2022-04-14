package WordleHelper2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PerformanceSimulator {

    public static void main(String[] args) throws InterruptedException, IOException {
        int sum = 0;
        HashMap<Integer, Integer> scoreMap = new HashMap<>();

        WordleBot bot = new WordleBot();
        // LinkedList<String> guessList = WordList.guessList();
        String[] answerList = WordList.answerList;
        // List<String> answerList = Arrays.asList(WordList.answerList);

        int i = 0;
        for (String word : answerList) {
            System.out.println("Current Word: " + word + " / Progress: " + (i++) + "/" + answerList.length);
            int guessCount = bot.playGame(new Game(word));
            sum += guessCount;
            scoreMap.put(guessCount, scoreMap.getOrDefault(guessCount, 0)+1);
            System.out.println("Average is: " + (double)sum/(double)i);   
            // Thread.sleep(500);
            System.out.println("\033[H\033[2J");
        }

        File file = new File("src/main/java/WordleHelper2/performance_sim_results.txt");
        FileWriter writer = new FileWriter(file);
        writer.write("Sum is: " + sum + "\n");
        writer.write("Length is: " + WordList.answerList.length + "\n");
        writer.write("Average performance is: " + ((double)sum / (double)i)+"\n");
        writer.write("Score Distribution is: \n");
        for (Map.Entry entry : scoreMap.entrySet()) {
            writer.write(entry.getKey() + ": " + entry.getValue() + "\n");
        }
        writer.flush();
        writer.close();
        

    }

}

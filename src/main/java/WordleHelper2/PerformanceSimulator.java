package WordleHelper2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Time;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class PerformanceSimulator {

    public static void main(String[] args) throws InterruptedException, IOException {

        long startTime = System.currentTimeMillis();

        boolean hardmode = true;

        int sum = 0;
        HashMap<Integer, Integer> scoreMap = new HashMap<>();

        WordleBot bot = new WordleBot();
        // LinkedList<String> guessList = WordList.guessList();
        String[] answerList = WordList.answerList;
        // List<String> answerList = Arrays.asList(WordList.answerList);

        int i = 0;
        int guessCount;
        for (String word : answerList) {
            System.out.println("Current Word: " + word + " / Progress: " + (i++) + "/" + answerList.length);
            if (i > 1) {
                System.out.println("Average is: " + (double)sum/(i-1));
            }
            if (hardmode) {
                guessCount = bot.playHardmode(new Game(word));
            } else {
                guessCount = bot.playGame(new Game(word));
            }
            sum += guessCount;
            scoreMap.put(guessCount, scoreMap.getOrDefault(guessCount, 0)+1);
            System.out.println("Average is: " + (double)sum/(double)i);   
            // Thread.sleep(500);
            System.out.println("\033[H\033[2J");
        }
        
        

        File file;
        if (hardmode) {
            file = new File("src/main/java/WordleHelper2/hardmode_sim_results.txt");
        }  else {
            file = new File("src/main/java/WordleHelper2/normal_sim_results.txt");
        }
        Long endTime = System.currentTimeMillis();
        long timeInMilliseconds = endTime-startTime;
        long hours = TimeUnit.MILLISECONDS.toHours(timeInMilliseconds);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(timeInMilliseconds - TimeUnit.HOURS.toMillis(hours));
        long seconds = TimeUnit.MILLISECONDS.toSeconds(timeInMilliseconds - TimeUnit.HOURS.toMillis(hours) - TimeUnit.MINUTES.toMillis(minutes));
        long milliseconds = timeInMilliseconds - TimeUnit.HOURS.toMillis(hours) - TimeUnit.MINUTES.toMillis(minutes) - TimeUnit.SECONDS.toMillis(seconds);

        FileWriter writer = new FileWriter(file);
        writer.write("Sum is: " + sum + "\n");
        writer.write("Length is: " + WordList.answerList.length + "\n");
        writer.write("Average performance is: " + ((double)sum / (double)i)+"\n");
        writer.write("Score Distribution is: \n");
        writer.write("\n");
        for (Map.Entry entry : scoreMap.entrySet()) {
            writer.write(entry.getKey() + ": " + entry.getValue() + "\n");
        }

        writer.write("\n");
        
        writer.write("Started at: " + new Date(startTime) + "\n");
        writer.write("Finished at: " + new Date(endTime)+"\n");
        writer.write("Time to completion: " +String.format("%02d:%02d:%02d:%d", hours, minutes, seconds, milliseconds)+"\n");

        
        writer.flush();
        writer.close();

    }

}

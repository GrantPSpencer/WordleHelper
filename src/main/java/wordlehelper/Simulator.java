package wordlehelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Simulator {
    
    public static void main(String[] args) throws IOException {

        WordList.getWords();
    
        LinkedList<String> guessList = WordList.guessList;
        String[] answerList = WordList.answerList;
        int length = WordList.guessList.size();

        // HashMap<String, Double> averageBitsMap = new HashMap<>();
        FileWriter writer = new FileWriter("src/main/java/wordlehelper/averagescores.txt");
        for (int i = 0; i < guessList.size(); i++) {
            double sum = 0;
            for (int j = 0; j < answerList.length; j++) {
                if (guessList.get(i) == answerList[j]) {
                    continue;
                }
                SimpleGame simpleGame = new SimpleGame(answerList[j]);
                sum += simpleGame.getBits(guessList.get(i), guessList);
                
            }
            // System.out.println(WordList.list[i] + " has average bits of " + sum/4);
            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append(guessList.get(i)).append(",").append(sum/(length-1)).append("\n");
            writer.write(strBuilder.toString());
            System.out.print('\r'+"Progress: " + i + " / " + length);
            
        }

        writer.flush();
        writer.close();

           

        // read files, test if delimiting works  
        // try {
        //     FileReader reader = new FileReader("src/main/java/wordlehelper/data.txt");
        //     BufferedReader bufferedReader = new BufferedReader(reader);
        //     String line = bufferedReader.readLine();
        //     while (line != null) {
        //         System.out.println(line);
        //         line = bufferedReader.readLine();
        //     }
        // } catch (IOException e) {
        //     // TODO Auto-generated catch block
        //     e.printStackTrace();
        // }

        

        // try {
        //     System.out.println("attempting to write");
        //     writer2.write("hello world2\n");
        //     writer2.flush();
        //     writer2.close();
        // } catch (IOException e) {
        //     // TODO Auto-generated catch block
        //     e.printStackTrace();
        // }
        
    }
    
}



package wordlehelper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class SortAverageScores {
    public static void main(String[] args) throws IOException {
    
        FileWriter writer = new FileWriter("src/main/java/wordlehelper/data3.txt");
        ArrayList<String[]> pairs = new ArrayList<>(WordList.answerList.length);
        FileReader reader = new FileReader("src/main/java/wordlehelper/data2.txt");

        BufferedReader bufferedReader = new BufferedReader(reader);
        String line = bufferedReader.readLine();
        while (line != null) {
            String[] tokens = line.split(",");
            String a = tokens[0];
            String b = tokens[1];
            String[] pair = new String[] {a, b};
            pairs.add(pair);
            pairs.sort((x,y) -> (y[1].compareTo(x[1])));
            line = bufferedReader.readLine();
        }
       

        for (String[] pair : pairs) {
            writer.write("[" + pair[0] + "," + pair[1] + "], ");
        }

        writer.flush();
        writer.close();
        reader.close();

    }

}

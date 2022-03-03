package wordlehelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.Scanner;

public class WordFrequencies {

    public void getWordFrequencies() throws IOException {
        File file = new File("src/main/java/wordlehelper/word_frequencies.txt");
        Scanner scanner = new Scanner(file);
        scanner.useDelimiter("], ");
        File newFile = new File("src/main/java/wordlehelper/word_frequencies2.txt");
        FileWriter writer = new FileWriter(newFile);


        String line = scanner.next();
        


        while (line != null) {
            writer.write(line + "]\n");
            if (scanner.hasNext()) {
                line = scanner.next();
            } else {
                break;
            }
        }
        writer.flush();
        writer.close();
        scanner.close();
    }


    public void getAllowedWords() throws IOException {
        File fileOut = new File("src/main/java/wordlehelper/allowed_words.txt");
        File fileIn = new File("src/main/java/wordlehelper/word_frequencies2.txt");

        FileWriter writer = new FileWriter(fileOut);
        FileReader reader = new FileReader(fileIn);
        BufferedReader bufferedReader = new BufferedReader(reader);

        String line = bufferedReader.readLine();
        StringBuilder strBuilder;
        while (line != null) {
            strBuilder = new StringBuilder();
            String word = line.substring(1,6);
            strBuilder.append("\"" + word + "\",");
            writer.write(strBuilder.toString());
            line = bufferedReader.readLine();
        }
        writer.flush();
        writer.close();
        reader.close();

        
    }

   
    public static void main(String[] args) throws IOException {
        
        WordFrequencies wordFreq = new WordFrequencies();
        wordFreq.getAllowedWords();




    }

    
    

}
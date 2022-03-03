package wordlehelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class testList {
    public static ArrayList<String> guessList = new ArrayList<>();
    
    public static void getWords() throws FileNotFoundException {
        File file = new File("src/main/java/wordlehelper/allowed_words.txt");
        Scanner scanner = new Scanner(file);
        scanner.useDelimiter(",");
        while (scanner.hasNext()) {
            String word = scanner.next().substring(1,6);
            testList.guessList.add(word);
        }
        scanner.close();

    }

    public static void findWords() throws IOException {
        WordList.getWords();
        File file = new File("src/main/java/wordlehelper/allowed_words_temp");
        FileWriter writer = new FileWriter(file);
        
        for (String word : WordList.answerList) {
            if (!WordList.guessList.contains(word)) {
                writer.write('\"' + word + '\"'+ ", ");
            }
        }
        writer.flush();
        writer.close();
    }

    public static void main(String[] args) throws IOException {
        testList.findWords();

    }
}

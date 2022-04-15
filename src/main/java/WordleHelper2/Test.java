package WordleHelper2;

import java.io.FileNotFoundException;
import java.util.LinkedList;

public class Test {
    public static void main(String[] args) throws FileNotFoundException {
        LinkedList<String> wordlist = WordList.guessList();
        System.out.println(wordlist.size());
    }
}

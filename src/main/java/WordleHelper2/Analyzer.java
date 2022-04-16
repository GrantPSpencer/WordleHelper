package WordleHelper2;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class Analyzer {

    public Game game;
    Scanner scanner;
    
    
    public Analyzer() throws FileNotFoundException {
        scanner = new Scanner(System.in);
        System.out.print("Please input today's word: ");
        String answer = scanner.nextLine().toLowerCase();
        this.game = new Game(answer);
    }
    public static void main(String[] args) throws FileNotFoundException {
        // Analyzer analyzer = new Analyzer();
        WordleBot bot = new WordleBot();
        HashMap<String, String[]> responseMap = bot.analyzePerformance(new Game("props"));
        System.out.println("The computer took " + responseMap.get("guessCount")[0] + " guessses");
        String guessNumber;
        int i = 1;
        while ( i < 7 ) {
            guessNumber = "Guess" + i;
            if (!responseMap.containsKey(guessNumber)) {
                break;
            }
            String[] responses = responseMap.get(guessNumber);
            String guess = responses[0];
            String actualBits = responses[1];
            String expectedBits = responses[2];
            System.out.println("The computer guessed " + "'" + guess + "'" + " on turn " + i);
            System.out.println("The computer expected " + expectedBits + " bits, but actually received "  + actualBits);
            i++;
        }
        if (responseMap.containsKey("answer")) {
            System.out.println("The computer correclty guessed " + "'" + responseMap.get("answer")[0] + "'" + " on turn " + i);
        }
    }
}

# WordleHelper
 
Building a program that helps you analyze your wordle performance. Uses information theory to determine the best word to guess. 

Progress so far:
- Remade game in Java (normal mode and hardmode)
- Used this game to analyze performance of each each allowed guess
     - Determined expected bits of information to be gained from each word as a first guess (That computation is expensive due to the size of the list, so the first guess is hardcoded to avoid redundant recomputations
     - During a game, program dynamically determines the expected bits gained from each word when the list of possible words has been reduced
      - Compares expected bits gained vs. actual bits gained 
- Analyzed performance of my algorithm over all 2309 possible answers. 

Upcoming
- Analyzer Feature: Allows user to analyze their performance on an already completed game
- Helper Feature: Helps the user with their current game of wordle, suggests what the user should guess next

Current Algorithm's Performance (on hardmode):
![Sim Performance Chart](https://user-images.githubusercontent.com/80296166/156674090-5b1b8f73-d000-4368-a28d-6a097994fb7d.png)

Example Performance on the word "humph":
</br>
<img width="335" src = "https://user-images.githubusercontent.com/80296166/163917558-6683e9b9-776a-4a30-96b4-65a161c8970e.jpg">
<img width="290" alt="Screen Shot 2022-03-03 at 3 36 21 PM" src="https://user-images.githubusercontent.com/80296166/156674188-91dde57d-8c2c-4384-919c-34f22907a4ed.png">

</br>




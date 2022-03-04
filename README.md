# wordlehelper
 
Building a program that helps you analyze your wordle performance. Currently working

Progress so far:
- Remade game in Java (no gui)
- Used this game to analyze performance of each each allowed guess
     - Pre-determined expected bits of information to be gained from each word as a first guess (That computation is expensive due to the size of the list, so the first guess is hardcoded to avoid redundant recomputations
     - During a game, dynamically determine the expected bits gained from each word when the list of possible words has been reduced

- Analyzed performance of my algorithm over all 2309 possible answers. 

Upcoming
- Improve guessing algorithm
     - Currently it solely prioritizes information and does not take into account the probability of winning the game
- Compare user's guess to best expected guess to allow for analysis of a player's game

Current Algorithm's Performance:
![Sim Performance Chart](https://user-images.githubusercontent.com/80296166/156674090-5b1b8f73-d000-4368-a28d-6a097994fb7d.png)

Example Performance on the word "humph":
<img width="290" alt="Screen Shot 2022-03-03 at 3 36 21 PM" src="https://user-images.githubusercontent.com/80296166/156674188-91dde57d-8c2c-4384-919c-34f22907a4ed.png">



package trivia;

import java.util.ArrayList;
import java.util.LinkedList;

// REFACTOR ME
public class Game implements IGame {
   private static final int INITIAL_QUESTIONS_COUNT = 50;
   private static final int WINNING_COINS = 6;
   private static final int BOARD_SIZE = 12;
   ArrayList<Player> players = new ArrayList<>();
   int[] currentPlayerPosition = new int[6];
   int[] playerCoins = new int[6];
   boolean[] currentPlayerInPenaltyBox = new boolean[6];

   LinkedList popQuestions = new LinkedList();
   LinkedList scienceQuestions = new LinkedList();
   LinkedList sportsQuestions = new LinkedList();
   LinkedList rockQuestions = new LinkedList();

   int currentPlayer = 0;
   boolean isGettingOutOfPenaltyBox;

   public Game() {
      for (int i = 0; i < INITIAL_QUESTIONS_COUNT; i++) {
         popQuestions.addLast("Pop Question " + i);
         scienceQuestions.addLast(("Science Question " + i));
         sportsQuestions.addLast(("Sports Question " + i));
         rockQuestions.addLast(createRockQuestion(i));
      }
   }

   public String createRockQuestion(int index) {
      return "Rock Question " + index;
   }

   public boolean hasEnoughPlayers() {
      return (howManyPlayers() >= 2);
   }

   public boolean add(String playerName) {
      currentPlayerPosition[howManyPlayers()] = 1;
      playerCoins[howManyPlayers()] = 0;
      currentPlayerInPenaltyBox[howManyPlayers()] = false;
      
      Player newPlayer = new Player(playerName);
      newPlayer.setPosition(1); // <--- IMPORTANTE: Debe empezar en 1, no en 0
      players.add(newPlayer);

      System.out.println(playerName + " was added");
      System.out.println("They are player number " + players.size());
      return true;
   }

   public int howManyPlayers() {
      return players.size();
   }

   public void roll(int roll) {
      Player player = players.get(currentPlayer);
      System.out.println(player + " is the current player");
      System.out.println("They have rolled a " + roll);

      if (player.isInPenaltyBox()) { 
         if (roll % 2 != 0) {
               isGettingOutOfPenaltyBox = true;
               System.out.println(player + " is getting out of the penalty box");
               movePlayer(roll);

               System.out.println(player + "'s new location is " + player.getPosition());
               System.out.println("The category is " + currentCategory(player.getPosition()));
               askQuestion(currentCategory(player.getPosition()));
         } else {
               System.out.println(player + " is not getting out of the penalty box");
               isGettingOutOfPenaltyBox = false;
         }
      } else {
         movePlayer(roll);
         System.out.println(player + "'s new location is " + player.getPosition());
         System.out.println("The category is " + currentCategory(player.getPosition()));
         askQuestion(currentCategory(player.getPosition()));
      }
   }

   private void movePlayer(int roll) {
      Player player = players.get(currentPlayer);
      
      // Calculamos la nueva posición (1-based index)
      int newPosition = player.getPosition() + roll;
      
      if (newPosition > BOARD_SIZE) {
         newPosition -= BOARD_SIZE;
      }
      
      player.setPosition(newPosition);
      
      // Sincronizamos con el array viejo (para que los prints que aún lo usan no fallen)
      currentPlayerPosition[currentPlayer] = newPosition;
   }

   private void askQuestion(String category) {
      if (category.equals("Pop"))
         System.out.println(popQuestions.removeFirst());
      if (category.equals("Science"))
         System.out.println(scienceQuestions.removeFirst());
      if (category.equals("Sports"))
         System.out.println(sportsQuestions.removeFirst());
      if (category.equals("Rock"))
         System.out.println(rockQuestions.removeFirst());
   }


   private String currentCategory(int space) {
    // Usamos (space - 1) para mantener la lógica original del "oráculo"
      int categoryIndex = (space - 1) % 4; 
      if (categoryIndex == 0) return "Pop";
      if (categoryIndex == 1) return "Science";
      if (categoryIndex == 2) return "Sports";
      return "Rock";
   }

   public boolean handleCorrectAnswer() {
      // declaramos la variable obteniéndola de la lista
      Player player = players.get(currentPlayer); 

      if (player.isInPenaltyBox()) { 
         if (isGettingOutOfPenaltyBox) {
               System.out.println("Answer was correct!!!!");
               
               player.setCoins(player.getCoins() + 1); 
               
               System.out.println(player.getName()
                                 + " now has "
                                 + player.getCoins()
                                 + " Gold Coins.");

               boolean winner = !playerHasWon();
               currentPlayer++;
               if (currentPlayer == players.size()) currentPlayer = 0;

               return winner;
         } else {
               currentPlayer++;
               if (currentPlayer == players.size()) currentPlayer = 0;
               return true;
         }
      } else {
         System.out.println("Answer was correct!!!!"); 
         
         player.setCoins(player.getCoins() + 1);
         
         System.out.println(player.getName()
                              + " now has "
                              + player.getCoins()
                              + " Gold Coins.");

         boolean winner = !playerHasWon();
         currentPlayer++;
         if (currentPlayer == players.size()) currentPlayer = 0;

         return winner;
      }
   }

   public boolean wrongAnswer() {
      Player player = players.get(currentPlayer);
      System.out.println("Question was incorrectly answered");
      System.out.println(player + " was sent to the penalty box");
      
      player.setInPenaltyBox(true);

      currentPlayer++;
      if (currentPlayer == players.size()) currentPlayer = 0;
      return true;
   }


   private boolean playerHasWon() { 
      return players.get(currentPlayer).getCoins() == WINNING_COINS; 
   }
}

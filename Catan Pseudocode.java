//CATAN PSEUDOCODE

-------------------------------------------------------------------------------
//Longest Road
//guess this would be checked every time a road is placed

int longestRoad = 4;

if (player.roadLength > longestRoad) {

  player.hasLongestRoad = true;
  player.victoryPoints += 2;
  //check player win

  //find player who had longest road
  player2.hasLongestRoad = false;
  player2.victoryPoints -= 2;

  longestRoad = player.roadLength;
}

-------------------------------------------------------------------------------
//Largest Army
//guess this would be checked every time a knight is played

int largestArmy = 2;

if (player.armySize > largestArmy) {

  player.hasLargestArmy = true;
  player.victoryPoints += 2;
  //check player win

  //find player who had largest army;
  player2.hasLargestArmy = false;
  player2.victoryPoints -= 2;

  largestArmy = player.armySize;
}

-------------------------------------------------------------------------------
//End Of Game
//guess this would be checked every time the player does something on their turn
//like build something, or play a card. Also start of turn

if (player.victoryPoints == 10) {

  endGame();
}

void endGame () {

  System.out.println("Player wins!");
  //show victory point cards

  System.out.println("Play Again?");

  if (yes) {

    startGame();
  }
  else {

    exit();
  }
}

-------------------------------------------------------------------------------
//Robber
//this would be checked once the dice is rolled and also if a knight is played.

if (diceRoll == 7 || card == knight) {

  for (player) {
    noResource = player.lumber + player.brick + player.wool + player.ore
    + player.grain;

    if (noResource > 7) {

      int noToLose = noResource/2;
      //player chooses this many cards to lose
      }
    }
  }

  System.out.println("Choose where to move the robber");
  //robber is moved
  //the hex nodes are checked for settlements
  //player then steals a random resource card from one of those players
}

-------------------------------------------------------------------------------
//Roads
//this is checked if the player wants to build a road
//other than at the start of the game

//decide how to calculate the road length

if (player.brick >= 1 && player.lumber >= 1) {

  int roadsLeft = 15 - player.roads;
  if (roadsLeft > 0) {
    if (validRoads.contains(road)) {
      //check the road they want to place
      //is connected to a players road or settlement
      //in the game they would only be able to click on valid spots anyway
      //let the player place a road

      player.brick--;
      player.lumber--;
      player.roads++;
      validRoads.remove(road);

      //update road length if needed
      //check for longest road
    }
  }
  else {
    System.out.println("You do not have any roads left to place");
  }
}
else {
  System.out.println("You do not have enough resources to build a road");
}

-------------------------------------------------------------------------------
//Settlements
//this is checked if the player wants to build a settlement
//other than at the start of the game

if (player.brick >= 1 && player.lumber >= 1 && player.wool >= 1
  && player.grain >= 1) {

  int settlementsLeft = 5 - player.settlements;

  if (settlementsLeft > 0) {
    if (validSettlements.contains(settlement)) {

      //check the settlement is connected to a players road
      //in the game they would only be able to click on valid spots anyway
      //let the player place the settlement

      player.brick--;
      player.lumber--;
      player.wool--;
      player.grain--;
      player.settlements++;

      validSettlements.remove(settlement);
      //remove adjacent nodes from legal spots

      player.victoryPoints++;
      //check player win
    }
  }
  else {
    System.out.println("You do have any settlements left to place");
  }
}
else {
  System.out.println("You do not have enough resources to build a settlement");
}

-------------------------------------------------------------------------------
//Cities
//this is checked if the player wants to build a city

if (player.ore >= 3 && player.grain >= 2) {

  int citiesLeft = 4 - player.cities;

  if (citiesLeft > 0 && player.settlements > 0) {

    //let player place the city

    player.ore -= 3;
    player.grain -= 2;
    player.cities++;
    player.settlements--;
    player.victoryPoints++;
    //check player win
  }
  else {
    System.out.println("You do not have any cities left to place");
  }
}
else {
  System.out.println("You do not have enough resources to build a city");
}

-------------------------------------------------------------------------------
//Development Cards
//this is checked if the player wants to build a development card

if (player.ore >= 1 && player.wool >= 1 && player.grain >= 1) {
  if (noDevelopmentCards > 0) {

    developmentCard = developmentCardsDeck[Math.rand()];
    noDevelopmentCards--;
    developmentCardsDeck.remove(developmentCard);
    player.developmentCards++;
    player.developmentCardsList.add(developmentCard);

    player.ore--;
    player.wool--;
    player.grain--;

    //check the type of development card: if VP update victory points
    //if not VP put card in cannot play list
  }
  else {
    System.out.println("There are no more development cards in the deck");
  }
}
else {
  System.out.println("You do not have enough resources to buy a development
    card");
}

-------------------------------------------------------------------------------
//Knights
//this is checked if the player wants to play a development card

if (knight) {

  player.developmentCards--;
  player.armySize++;
  //do robber stuff
  //check for largest army
}

-------------------------------------------------------------------------------
//Progress Cards
//this is checked if the player wants to play a development card

if (progress) {

  //update number of development cards in hand
  player.developmentCards--;

  if (roadBuilding) {

    //let player build two free roads
    //so go to road building but don't take out resources
  }

  else if (yearOfPlenty) {

    for (int i = 0; i < 2; i++) {

      System.out.println("Choose your resource");

      player.resource++;
    }
  }

  else if (monopoly) {

    System.out.println("Choose your resource");

    for (player2) {
      int noResource = player.resource;

      if (player2.resource >= 1) {

        player2.resource = 0;
        player.resource += noResource;
      }
    }
  }
}

-------------------------------------------------------------------------------
//Victory Point Cards
//checked if the player buys a development card

if (victoryPoint) {

  player.victoryPoints++;
  //check player win
}

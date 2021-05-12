package adventure;

import java.util.ArrayList;
import java.util.Scanner;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

public class Game implements java.io.Serializable {

    /* this is the class that runs the game.
    You may need some member variables */
    private static final long serialVersionUID = 8691231374796487356L;
    private Adventure myAdventure;
    private Player myPlayer = new Player();
    private Parser myParser = new Parser();
    private static Scanner scanner = new Scanner(System.in); // create input buffer
    private boolean isSerialized = false; // use for overridden toString() method for Game class

    /** REQUIRED main method makes calls to primary methods and starts game
     * @param args String array of user input from terminal
     */
    public static void main(String[] args) {
        Game myGame = new Game(); // instantiate object of type Game to avoid using static methods
        JSONObject jsonObject = myGame.parseCommandLine(args); // send filename to loadAdventureJson(), exit if error

        myGame.welcomeDisplay(); // main screen - welcome message and instructions for game play
        // generating adventure and starting game
        myGame.setMyAdventure(myGame.generateAdventure(jsonObject));
        myGame.setPlayerName();
        myGame.gameLoop(myGame.startLocation()); // begin game if no error with file loading or file itself

        myGame.endScreen();
    }

    /* you must have these instance methods and may need more */

    /**
     * REQUIRED overloaded method; loads the custom adventure using String
     * @param filename relative file path for adventure file
     * @return JSONObject which file was parsed into
     */
    public JSONObject loadAdventureJson(String filename) {
        JSONParser parser = new JSONParser(); // object to parse file

        try (FileReader reader = new FileReader(filename)) {
            return (JSONObject) parser.parse(reader); // parse file into JSON object
        } catch (Exception e) {
            fileLoadingError();
            return null;
        }
    }

    /**
     * REQUIRED loads the default adventure
     * @param inputStream stream of data
     * @return JSONObject which file was parsed into
     */
    public JSONObject loadAdventureJson(InputStream inputStream) {
        JSONParser parser = new JSONParser(); // object to parse file

        try (InputStreamReader reader = new InputStreamReader(inputStream)) { // error-checking for file loading
            return (JSONObject) parser.parse(reader); // parse file into JSON object
        } catch (Exception e) {
            fileLoadingError();
            return null;
        }
    }

    /**
     * REQUIRED returns an Adventure object using information from parsed JSON file
     * @param obj JSONObject that has file parsed into it
     * @return an Adventure instance to use for the rest of the game
     */
    public Adventure generateAdventure(JSONObject obj) {
        Adventure generatedAdv = new Adventure();
        // parse the contents of the file and create the adventure
        JSONObject myObj = (JSONObject) obj.get("adventure");
        // retrieving arrays
        JSONArray itemList = (JSONArray) myObj.get("item");
        JSONArray roomList = (JSONArray) myObj.get("room");
        // parsing arrays by iterating through each array one object at a time
        itemList.forEach(item -> parseItemObject(generatedAdv, (JSONObject) item));
        roomList.forEach(room -> parseRoomObject(generatedAdv, (JSONObject) room));
        return generatedAdv;
    }

    /* MY ADDITIONAL METHODS */

    /**
     * display welcome message and tips to user once file loads and adventure begins
     */
    private void welcomeDisplay() {
        startMessage();
        help();
    }

    /**
     * parse command-line arguments and perform action based on arguments
     * @param args represents command-line arguments
     * @return a JSONObject representing the parsed file
     */
    private JSONObject parseCommandLine(String[] args) {
        JSONObject jsonObj = null;
        // need command-line flag followed by filename or game save name
        if (args.length == 2) {
            if (args[0].equals("-a")) {
                jsonObj = loadAdventureJson(args[1]);
            } else if (args[0].equals("-l")) {
                loadSavedGame(args[1]);
            }
        } else if (args.length == 0) {
            jsonObj = loadDefault();
        } else {
            commandLineError();
        }
        // error in file loading
        if (jsonObj == null) {
            System.exit(-1);
        }
        return jsonObj;
    }

    /**
     * load default adventure if no command-line arguments are given
     * @return a JSONObject representing the parsed file
     */
    private JSONObject loadDefault() {
        System.out.println("No command line arguments provided.\nGenerating default adventure...");
        InputStream inputStream = Game.class.getClassLoader().getResourceAsStream("default_adventure.json");
        JSONObject jsonObj = loadAdventureJson(inputStream);

        return jsonObj;
    }

    /**
     * prompt user for name
     */
    private void setPlayerName() {
        String name;
        do {
            System.out.println("\nEnter a name to begin:");
            name = promptInput();
        } while (name.isEmpty());

        myPlayer.setName(name);
    }

    /**
     * parses keys within each Item object from item array in JSON file
     * @param generatedAdv adventure generated using the file given to load game
     * @param item JSON object representing one item from items list in JSON file
     */
    private void parseItemObject(Adventure generatedAdv, JSONObject item) {
        Item nextItem = new Item();
        // get keys within item array element
        try { // error-checking for incorrectly formatted JSON file
            parseItemInfo(nextItem, item);
        } catch (Exception e) {
            fileParsingError();
        }
        generatedAdv.addItem(nextItem); // add item to adventure
        nextItem.setMyAdv(generatedAdv);
    }

    /**
     * parse keys in item
     * @param nextItem item being parsed from JSON file
     * @param item JSONObject representing one item in JSON file
     */
    private void parseItemInfo(Item nextItem, JSONObject item) {
        long id = (Long) item.get("id");
        String name = (String) item.get("name");
        String desc = (String) item.get("desc");
        setItemCheck(name, desc); // check for null values

        setItemInfo(nextItem, id, name, desc);
    }

    /**
     * store item id, name, and description in Item object
     * @param nextItem item being parsed from JSON file
     * @param id item id
     * @param name item name
     * @param desc item desc
     */
    private void setItemInfo(Item nextItem, long id, String name, String desc) {
        nextItem.setItemId(id);
        nextItem.setName(name);
        nextItem.setLongDescription(desc);
    }

    /**
     * parses keys within each Item object from item array in JSON file
     * @param generatedAdv adventure generated using the file given to load game
     * @param room JSON object representing one room from rooms list in JSON file
     */
    private void parseRoomObject(Adventure generatedAdv, JSONObject room) {
        Room nextRoom = new Room();
        parseStartFlag(nextRoom, room); // mark first room in adventure as start room
        try { // error-checking for incorrectly formatted JSON file
            parseRoomInfo(generatedAdv, nextRoom, room); // name, id, short & long description
        } catch (Exception e) {
            fileParsingError();
        }
        // add room to adventure
        generatedAdv.addRoom(nextRoom);
        nextRoom.setMyAdv(generatedAdv);
    }

    /**
     * parse keys in room
     * @param generatedAdv adventure generated using the file given to load game
     * @param nextRoom room being parsed from JSON file
     * @param room JSONObject representing one room in JSON file
     */
    private void parseRoomInfo(Adventure generatedAdv, Room nextRoom, JSONObject room) {
        // name, id, descriptions
        long roomID = (Long) room.get("id");
        String name = (String) room.get("name");
        String shortDesc = (String) room.get("short_description");
        String longDesc = (String) room.get("long_description");
        setRoomCheck(name, shortDesc, longDesc); // check for null values
        setRoomInfo(nextRoom, roomID, name, shortDesc, longDesc); // store information in Room object

        parseEntrance(nextRoom, room);
        parseLoot(generatedAdv, nextRoom, room);
    }

    /**
     * store room id, name, short & long description in Room object
     * @param nextRoom room being parsed from JSON file
     * @param id room ID
     * @param name room name
     * @param shortDesc short description of room
     * @param longDesc long description of room
     */
    private void setRoomInfo(Room nextRoom, long id, String name, String shortDesc, String longDesc) {
        nextRoom.setRoomId(id);
        nextRoom.setName(name);
        nextRoom.setShortDescription(shortDesc);
        nextRoom.setLongDescription(longDesc);
    }

    /**
     * set whether isStart to true or false depending on which room has start flag
     * @param nextRoom room being parsed from JSON file
     * @param room JSON object representing one room from rooms list in JSON file
     */
    private void parseStartFlag(Room nextRoom, JSONObject room) {
        boolean isStart = false;

        String start = (String) room.get("start");
        if (start != null) {
            isStart = true;
        }
        nextRoom.setIsStart(isStart); // store information in Room object
    }

    /**
     * parse and set room entrances
     * @param nextRoom Room object representing the room being parsed
     * @param room JSON object representing one room from rooms list in JSON file
     */
    private void parseEntrance(Room nextRoom, JSONObject room) {
        JSONArray entranceList = (JSONArray) room.get("entrance"); // get keys within entrance array
        nextRoom.setEntrances(); // allocate memory for entrance map

        for (int i = 0; i < entranceList.size(); i++) {
            JSONObject entrance = (JSONObject) entranceList.get(i);
            try { // error-checking for incorrectly formatted JSON file
                parseEntranceInfo(nextRoom, entrance); // id and direction
            } catch (Exception e) {
                fileParsingError();
            }
        }
    }

    /**
     * parse and set keys within room entrance array element
     * @param nextRoom room being parsed from JSON file
     * @param entrance JSONObject representing one entrance in JSON file
     */
    private void parseEntranceInfo(Room nextRoom, JSONObject entrance) {
        long entranceID = (Long) entrance.get("id");
        String dir = (String) entrance.get("dir");
        setEntranceCheck(dir); // check for null values

        nextRoom.addEntrance(entranceID, dir); // store information in Room object
    }

    /**
     * parse and set loot in each room
     * @param generatedAdv generated adventure using the file given to load game
     * @param nextRoom Room object representing the room being parsed
     * @param room JSON object representing one room from rooms list in JSON file
     */
    private void parseLoot(Adventure generatedAdv, Room nextRoom, JSONObject room) {
        ArrayList<Item> allItems = generatedAdv.listAllItems();
        JSONArray lootList = (JSONArray) room.get("loot"); // get keys within loot array
        if (lootList != null) { // checking if there is loot in room to be parsed
            nextRoom.setRoomItems(); // allocate memory for ArrayList of room items
            for (int i = 0; i < lootList.size(); i++) {
                JSONObject loot = (JSONObject) lootList.get(i);
                long lootID = (Long) loot.get("id");
                setLoot(allItems, nextRoom, lootID); // set loot id in Room object
            }
        }
    }

    /**
     * set room's loot in Room object
     * @param allItems ArrayList of all items in JSON file
     * @param nextRoom room being parsed from JSON file
     * @param lootID loot id of loot array element in room
     */
    private void setLoot(ArrayList<Item> allItems, Room nextRoom, long lootID) {
        for (Item item : allItems) {
            if (lootID == item.getItemId()) {
                nextRoom.addRoomItem(item); // store room's loot in roomItems ArrayList of Room class
            }
        }
    }

    /**
     * exit program if null Strings (incorrect formatting) present in Item object from file
     * @param name item name
     * @param description item description
     */
    private void setItemCheck(String name, String description) {
        if (name == null || name.length() == 0 || description == null || description.length() == 0) {
            System.out.println("Error: Items in file are incorrectly formatted.");
            System.out.println("Exiting...");
            System.exit(-1);
        }
    }

    /**
     * exit program if null Strings (incorrect formatting) present in Room object from file
     * @param name room name
     * @param shortDescription short description of room
     * @param longDescription long description of room
     */
    private void setRoomCheck(String name, String shortDescription, String longDescription) {
        // exit game if name or descriptions were not given
        if (name == null || name.length() == 0 || shortDescription == null ||  shortDescription.length() == 0
                || longDescription == null || longDescription.length() == 0) {
            System.out.println("Error: Rooms in file are incorrectly formatted...");
            System.out.println("Exiting...");
            System.exit(-1);
        }
    }

    /**
     * exit program if null or empty direction (incorrect formatting) for Room object from file
     * @param direction direction the entrance is in
     */
    private void setEntranceCheck(String direction) {
        // exit game if direction was not given
        if (direction == null || direction.length() == 0) {
            System.out.println("Error: Entrances in file are incorrectly formatted.");
            System.out.println("Exiting...");
            System.exit(-1);
        }
    }

    /**
     * find the start location from the JSON file
     * @return the starting Room
     */
    private Room startLocation() {
        Room startRoom = new Room();
        System.out.println("\n[BEGINNING ADVENTURE]----------------------------------------------------");
        // find starting location
        for (int i = 0; i < myAdventure.getTotalNumRooms(); i++) {
            if (myAdventure.listAllRooms().get(i).getIsStart()) {
                startRoom = setStartRoom(i);
            }
        }
        displayRoomInfo(startRoom);
        return startRoom;
    }

    /**
     * set starting room of the adventure
     * @param incrementor for loop incrementor
     * @return Room object representing the start room
     */
    private Room setStartRoom(int incrementor) {
        Room startRoom = myAdventure.listAllRooms().get(incrementor); // set current room to start room
        myPlayer.setCurrentRoom(startRoom);
        myAdventure.setMyPlayer(myPlayer);

        return startRoom;
    }

    /**
     * contains the game loop which prompts user input and helps user navigate through game
     * loop as long as input is invalid or "quit" has not been entered
     * @param currentRoom the room the adventure starts in
     */
    private void gameLoop(Room currentRoom) {
        Command nextCommand;
        boolean invalidInput;
        String userInput;
        String[] input;

        do {
            userInput = promptInput();
            try {
                nextCommand = null;
                nextCommand = parse(userInput); // parse user input into Command - throw Exception if invalid command
                input = userInput.split("\\s+", 2); // store everything after first word into second index
                invalidInput = checkInput(currentRoom, input); // invalid if error in "go", "look", or "take" inputs
                currentRoom = continueAdventure(currentRoom, input, invalidInput);
            } catch (InvalidCommandException e) {
                invalidInput = true;
                System.out.println(e.getMessage());
            }
        } while (invalidInput || !userInput.equals("quit"));
    }

    /**
     * if input is valid, update current room and advance in adventure
     * @param currentRoom a Room object representing the current room the user is in
     * @param input user input parsed into array of Strings
     * @param invalidInput a boolean variable representing whether user input was invalid
     * @return a Room object representing the current room user is in
     */
    private Room continueAdventure(Room currentRoom, String[] input, boolean invalidInput) {
        if (!invalidInput) {
            currentRoom = play(currentRoom, input);
            myPlayer.setCurrentRoom(currentRoom);
            myAdventure.setMyPlayer(myPlayer);
        }
        return currentRoom;
    }

    /**
     * wrapper method for parseUserCommand() method in Parser class
     * @param userInput user input
     * @return user input parsed into a Command
     * @throws InvalidCommandException if user does not input a command
     */
    private Command parse(String userInput) throws InvalidCommandException {
        return myParser.parseUserCommand(userInput);
    }

    /**
     * error-checking for valid commands (additional error-checking after parser and command class)
     * @param currentRoom Room object representing the current room the user is in
     * @param input user input parsed into array of Strings
     * @return boolean value representing whether the user input was invalid
     */
    private boolean checkInput(Room currentRoom, String[] input) {
        boolean invalidInput = false;
        if (input[0].equals("go")) {
            invalidInput = checkInputGo(currentRoom, input);
        } else if (input[0].equals("look")) {
            invalidInput = checkInputLook(currentRoom, input);
        } else if (input[0].equals("take")) {
            invalidInput = checkInputTake(currentRoom, input);
        }
        return invalidInput;
    }

    /**
     * display room name and items present in current room
     * @param currentRoom the room the user is in
     */
    private void displayRoomInfo(Room currentRoom) {
        if (currentRoom.getIsStart()) {
            System.out.println("\n" + myAdventure.getCurrentRoomDescription() + "."); // display start room description
        } else {
            System.out.println("\nYou are now in " + currentRoom.getName() + "."); // display room name
        }
        System.out.println("Items present:");
        printItems(currentRoom); // list items in room
    }

    /**
     * error-checking for user input when user enters "go"
     * invalid input if user-specified direction is not one of the four main directions or it does not lead to a room
     * @param currentRoom a Room object representing the current room the user is in
     * @param input user input parsed into array of Strings
     * @return boolean variable representing whether user input was invalid
     */
    private boolean checkInputGo(Room currentRoom, String[] input) {
        boolean invalidInput = false;
        // check whether connected room exists
        if (currentRoom.getConnectedRoom(input[1]) == null) {
            invalidInput = true;
            System.out.println("There is nothing in that direction.");
        }
        return invalidInput;
    }

    /**
     * error-checking for user input when user enters "look" followed by a second word
     * invalid input if user-specified item does not exist in the room
     * @param currentRoom a Room object representing the current room the user is in
     * @param input user input parsed into array of Strings
     * @return boolean variable representing whether user input was invalid
     */
    private boolean checkInputLook(Room currentRoom, String[] input) {
        boolean invalidInput = false;
        if (input.length == 2) {
            try { // error-checking if no items are in current room
                invalidInput = isInvalidItem(currentRoom, input);
            } catch (Exception e) {
                invalidInput = true;
                System.out.println("There are no items in this room/area.");
            }
        }
        return invalidInput;
    }

    /**
     * error-checking for user input when user enters "take"
     * invalid input if user-specified item does not exist in the room or user has already taken it
     * @param currentRoom a Room object representing the current room the user is in
     * @param input user input parsed into array of Strings
     * @return boolean variable representing whether user input was invalid
     */
    private boolean checkInputTake(Room currentRoom, String[] input) {
        boolean invalidInput;
        try { // error-checking for no items in current room
            invalidInput = isInvalidItem(currentRoom, input);
        } catch (Exception e) {
            invalidInput = true;
            System.out.println("Error: There are no items in this room/area.");
        }
        return invalidInput;
    }

    /**
     * used by checkInputTake() and checkInputLook() to check for invalid input for "look" and "take" commands
     * @param currentRoom Room object representing the current room the user is in
     * @param input user input parsed into array of Strings
     * @return a boolean variable representing whether user input is invalid
     */
    private boolean isInvalidItem(Room currentRoom, String[] input) {
        boolean invalidInput = itemInInventory(input); // check whether item is in inventory
        if (invalidInput) {
            return true;
        }
        invalidInput = itemInRoom(currentRoom, input); // check whether item is in room
        return invalidInput;
    }

    /**
     * check room items ArrayList to determine whether user can "take" or "look" at desired item
     * @param currentRoom Room object representing the current room the user is in
     * @param input user input parsed into array of Strings
     * @return a boolean variable representing whether user can "look" at or "take" desired item
     */
    private boolean itemInRoom(Room currentRoom, String[] input) {
        boolean invalidInput = true;
        for (Item roomItem : currentRoom.listItems()) { // check whether item is in list of items in room
            if (input[1].equals(roomItem.getName())) {
                invalidInput = false;
            }
        }
        if (invalidInput) {
            System.out.println("Error: There is no such item in this room/area.");
        }
        return invalidInput;
    }

    /**
     * check inventory ArrayList to determine whether user is already carrying item they want to "look" at / "take"
     * @param input user input parsed into array of Strings
     * @return a boolean variable representing whether item from user input was already "taken"
     */
    private boolean itemInInventory(String[] input) {
        boolean invalidInput = false;
        if (!myPlayer.getInventory().isEmpty()) { // if item is not in room, it may be in the inventory!
            invalidInput = myPlayer.isTakenItem(input[1]);
            if (invalidInput) {
                if (input[0].equals("take")) {
                    System.out.println("You are already carrying this item.");
                } else if (input[0].equals("look")) {
                    System.out.println("You may only 'look' at this room/area or the items within it.");
                }
            }
        }
        return invalidInput;
    }

    /**
     * perform action based on whether user entered help or quit - input does not need to be checked further
     * @param currentRoom a Room object representing the current room the user is in
     * @param input user input parsed into array of Strings
     */
    private void helpOrQuit(Room currentRoom, String input) {
        if (input.equals("help")) {
            help();
            displayRoomInfo(currentRoom); // print room name and items in room
        } else if (input.equals("quit")) {
            quit(); // if user confirms, method exits program
        }
    }

    /**
     * perform actions based on (valid) user input
     * @param theRoom temp variable to be used in this method to represent the current room
     * @param input user input parsed into array of Strings
     * @return an object of type Room to update the current room if the user input leads to a room from file
     * */
    private Room play(Room theRoom, String[] input) {
        if (input[0].equals("go")) {
            theRoom = playGo(theRoom, input);
        } else if (input[0].equals("look")) {
            playLook(theRoom, input);
        } else if (input[0].equals("take")) {
            playTake(theRoom, input);
        } else if (input[0].equals("inventory")) {
            printInventory();
        } else {
            helpOrQuit(theRoom, input[0]);
        }
        return theRoom;
    }

    /**
     * implement "go" command
     * @param currentRoom the room in which the user has entered
     * @param input user input parsed into array of Strings
     * @return an object of type Room representing the current room
     */
    private Room playGo(Room currentRoom, String[] input) {
        currentRoom = currentRoom.getConnectedRoom(input[1]); // set current room to connected room
        myPlayer.setCurrentRoom(currentRoom);
        myAdventure.setMyPlayer(myPlayer);
        displayRoomInfo(currentRoom); // print room name and items in room

        return currentRoom;
    }

    /**
     * implement "look" command
     * @param currentRoom the room the user is in
     * @param input user input parsed into array of Strings
     */
    private void playLook(Room currentRoom, String[] input) {
        if (input.length == 1) { // want longer description of room
            System.out.println(myPlayer.getCurrentRoom().getLongDescription() + ".");
        } else { // want description of an item
            String itemName = input[1];
            for (int i = 0; i < currentRoom.listItems().size(); i++) {
                if (itemName.equals(currentRoom.listItems().get(i).getName())) {
                    System.out.println("There is " + currentRoom.listItems().get(i).getLongDescription() + ".");
                }
            }
        }
    }

    /**
     * implement "take" command -- add item to inventory ArrayList from Player class
     * @param currentRoom the room the user is in
     * @param input user input parsed into array of Strings
     */
    private void playTake(Room currentRoom, String[] input) {
        Item carriedItem;
        // find index at which the item is in ArrayList of items in the room
        for (int i = 0; i < currentRoom.listItems().size(); i++) {
            if (input[1].equals(currentRoom.listItems().get(i).getName())) { // item found in current room
                carriedItem = currentRoom.listItems().get(i);
                myPlayer.setInventory();
                myPlayer.addToInventory(carriedItem);
                currentRoom.removeItem(currentRoom.listItems().indexOf(carriedItem));
                System.out.println("You are now carrying the " + carriedItem.getName() + ".");
            }
        }
        currentRoom.deleteListItems(); // set roomItems ArrayList to null if no remaining items in room
    }

    /**
     * print items in room
     * @param theRoom the room whose items will be printed out
     */
    private void printItems(Room theRoom) {
        try { // error-checking for uninitialized roomItems list (i.e. no items were originally in given room)
            for (Item roomItem : theRoom.listItems()) {
                System.out.printf("* %s\n", roomItem.getName());
            }
        } catch (Exception e) {
            System.out.println("There are no items in this room/area.");
        }
    }

    /**
     * print items in inventory
     */
    private void printInventory() {
        System.out.println(myPlayer.getName().toUpperCase() + "'s Inventory:");
        if (!myPlayer.getInventory().isEmpty()) {
            for (Item inventoryItem : myPlayer.getInventory()) {
                System.out.printf("* %s\n", inventoryItem.getName());
            }
        } else {
            System.out.println("There are no items in your inventory.");
        }
    }

    /**
     * prompt user for input
     * @return a String representing the user input
     */
    private String promptInput() {
        String userInput;
        System.out.print("> ");
        userInput = scanner.nextLine();
        userInput = userInput.trim(); // remove leading or trailing whitespace

        return userInput;
    }

    /**
     * REQUIRED (must have public setters for all member variables)
     * set myAdventure instance variable in Game class
     * @param adv Adventure object
     */
    public void setMyAdventure(Adventure adv) {
        myAdventure = adv;
    }

    /**
     * REQUIRED (must have public setters for all member variables)
     * set myParser instance variable in Game class
     */
    public void setMyParser() {
        myParser = new Parser();
    }

    /**
     * REQUIRED (must have public setters for all member variables)
     * set myPlayer instance variable in Game class
     */
    public void setMyPlayer() {
        myPlayer = new Player();
    }

    /**
     * REQUIRED (must have public setters for all member variables)
     * set isSerialized instance variable in Game class
     * @param isSavedGame Adventure object
     */
    public void setIsSerialized(boolean isSavedGame) {
        isSerialized = isSavedGame;
    }

    /**
     * prints start message once user selects file to read from
     */
    private void startMessage() {
        System.out.println("\n[WELCOME!]---------------------------------------------------------------");
        System.out.println("This is a prototype game modeled after the 1977 game Colossal Caves by Will Crowther.");
        System.out.println("This version of the game will load an adventure description from file and allow you to \n"
                + "interact with the rooms and items in that adventure.");
    }

    /**
     * prints tips for game play if user enters "help"
     */
    private void help() {
        System.out.println(myParser.allCommands());
        System.out.println("[SOME TIPS]--------------------------------------------------------------");
        System.out.println("As a player of this game, you may:\n"
                + " -- move rooms in the adventure using the keyword 'go' and the subjects: N, S, E, W, up, or down.\n"
                + " -- see a longer description of the room when you type 'look'.\n"
                + " -- see a longer description of an item in the room when you type 'look' followed by item name.\n"
                + " -- pick up an item and carry it when you type 'take' followed by an item name.\n"
                + " -- see a list of items in your inventory when you type 'inventory'.\n"
                + " -- quit the game when you enter 'quit'.\n"
                + " -- refer to these tips again when you enter 'help'.");
    }

    /**
     * prints our error message for incorrect usage of command line arguments
     */
    private void commandLineError() {
        System.out.println("Usage: [-a] <filename.json> or [-l] <game save name>");
        System.exit(-1);
    }

    /**
     * prints out error message for incorrectly formatted file
     */
    private void fileParsingError() {
        System.out.println("Error: JSON file is incorrectly formatted.");
        System.out.println("Exiting...");
        System.exit(-1);
    }

    /**
     * prints out error message if file could not be loaded
     */
    private void fileLoadingError() {
        System.out.println("Error: File could not be loaded. Please try verifying your filename.");
        System.out.println("Exiting...");
        System.exit(-1);
    }

    /**
     * exit program if user enters "quit"
     */
    private void quit() {
        boolean invalidInput;
        System.out.println("Would you like to save your progress? (Y/N)");
        do {
            String userInput = promptInput();
            invalidInput = false;
            if (userInput.equals("Y")) {
                saveGame();
            } else if (!userInput.equals("N")) { // if user input is neither "Y" nor "N"
                invalidInput = true;
                System.out.println("I don't understand...");
            }
        } while (invalidInput);
        endScreen();
    }

    /**
     * show thank you for playing message
     */
    private void endScreen() {
        System.out.println("\n[THANKS FOR PLAYING!]----------------------------------------------------\n");
        System.exit(0);
    }

    /**
     * serialize game before exiting if user desires
     */
    private void saveGame() {
        String filename = getAdventureName();
        try {
            serializeObject(filename); // saving object in a file
            System.out.println("Your game has been saved!");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * serialize object to save adventure state
     * @param filename name of game save file
     * @throws IOException if file cannot be opened
     */
    private void serializeObject(String filename) throws IOException {
        FileOutputStream outPutStream = new FileOutputStream(filename);
        ObjectOutputStream outPutDest = new ObjectOutputStream(outPutStream);
        // method for serialization of object
        outPutDest.writeObject(myAdventure);
        // close streams
        outPutDest.close();
        outPutStream.close();
    }

    /**
     * prompt user for the name of the adventure they want to save
     * @return filename of user's choice
     */
    private String getAdventureName() {
        String filename;
        boolean invalidFileName;
        System.out.println("Enter a name for your saved adventure:");
        do {
            filename = promptInput();
            invalidFileName = checkFileName(filename); // error-checking for no user input
        } while (invalidFileName);
        myPlayer.setSaveGameName(filename);
        return filename;
    }

    /**
     * check whether user input a filename
     * @param filename name the user would like to save the game save file as
     * @return boolean variable representing whether the filename is invalid
     */
    private boolean checkFileName(String filename) {
        boolean invalidFileName = false;
        if (filename.equals("null") || filename.isEmpty()) {
            System.out.println("Error: Invalid filename.");
            invalidFileName = true;
        }
        return invalidFileName;
    }

    /**
     * load serialized game and error-handling for unsuccessful game load
     * @param filename name of serialized file the user entered (may be valid or invalid)
     */
    private void loadSavedGame(String filename) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            setMyAdventure((Adventure) in.readObject());
            System.out.println("Your game has successfully loaded!");
            setIsSerialized(true); // use for overridden toString() method
        } catch (IOException e) {
            System.out.println("IOException caught: " + e.getMessage());
        } catch (Exception e) {
            fileLoadingError();
        }
        resumeSavedGame(); // begin adventure if no error in loading serialized game
    }

    /**
     * resume saved game once it has been loaded successfully
     */
    private void resumeSavedGame() {
        setMyPlayer(myAdventure.getPlayer()); // update myPlayer to get current room and continue game play
        displayRoomInfo(myPlayer.getCurrentRoom());
        gameLoop(myPlayer.getCurrentRoom());
        quit();
    }

    /**
     * REQUIRED (must have public setters for all member variables)
     * set myPlayer instance variable in Game class
     * @param player Player object to set instance variable to
     */
    public void setMyPlayer(Player player) {
        myPlayer = player;
    }

    /**
     * toString method prints String instead of mem location on accident
     * @return String a String representing the status of the game
     */
    @Override
    public String toString() {
        String gameInfo;
        // game description
        if (isSerialized) {
            gameInfo = "\nUser is playing a serialized game";
        } else {
            gameInfo = "\nUser is playing a new game";
        }
        // room info
        gameInfo = gameInfo
                + "\ncurrent room: " + myPlayer.getCurrentRoom().getName()
                + "\n" + myPlayer.getCurrentRoom().getLongDescription();

        return gameInfo;
    }

}

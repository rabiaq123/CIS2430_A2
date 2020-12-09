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
    private static Adventure myAdventure;
    private static Player myPlayer = new Player();
    private Parser myParser = new Parser();
    private static Scanner scanner = new Scanner(System.in); // create input buffer

    /** REQUIRED main method makes calls to primary methods and starts game
     * @param args String array of user input from terminal
     */
    public static void main(String[] args) {
        Game myGame = new Game(); // instantiate object of type Game to avoid using static methods
        JSONObject jsonObject; // represents parsed file

        // loading adventure
        jsonObject = myGame.parseCommandLine(myGame, args); // send specified file to loadAdventureJson()
        if (jsonObject == null) { // error in file loading
            System.exit(-1);
        }

        myGame.startMessage();
        myGame.help();

        // generating adventure and starting game
        myAdventure = myGame.generateAdventure(jsonObject);
        myGame.setPlayerName();
        myGame.gameLoop(myGame.startLocation()); // begin game if no error with file loading or file itself

        myGame.quit();
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

    /** parse command-line arguments and perform action based on arguments
     * NEED TO ADD FUNCTIONALITY FOR THE -L FLAG
     * @param theGame
     * @param args represents command-line arguments
     * @return a JSONObject representing the parsed file
     */
    public JSONObject parseCommandLine(Game theGame, String[] args) {
        JSONObject jsonObj = null;

        // need command-line flag followed by filename or game save name
        if (args.length == 2) {
            if (args[0].equals("-a")) {
                jsonObj = loadAdventureJson(args[1]);
            } else if (args[0].equals("-l")) {
                loadSavedGame(args[1]);
                displayRoomInfo(myPlayer.getCurrentRoom());
                gameLoop(myPlayer.getCurrentRoom());
                quit();
            }
        } else if (args.length == 0) {
            System.out.println("No command line arguments provided.\nGenerating default adventure...");
            InputStream inputStream = Game.class.getClassLoader().getResourceAsStream("default_adventure.json");
            jsonObj = loadAdventureJson(inputStream);
        } else {
            commandLineError();
        }

        return jsonObj;
    }

    /**
     * load serialized game and error-handling for unsuccessful game load
     * @param filename name of serialized file the user entered (may be valid or invalid)
     */
    public void loadSavedGame(String filename) {
        Adventure savedAdventure = null;
//        Game savedGame = null;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            savedAdventure = (Adventure)in.readObject();
//            savedGame = (Game)in.readObject();
            System.out.println("Your game has successfully loaded!");
        } catch (IOException e) {
            System.out.println("IOException is caught " + e);
            System.exit(-1);
        } catch (Exception e) {
            fileLoadingError();
            System.exit(-1);
        }
    }

    /**
     * prompt user for name
     */
    private void setPlayerName() {
        String name = null;
        do {
            System.out.println("\nEnter a name to begin:");
            System.out.print("> ");
            name = scanner.nextLine();
        } while (name.isEmpty());

        myPlayer.setName(name);
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
     * parses keys within each Item object from item array in JSON file
     * @param generatedAdv Adventure object representing the generated adventure using the file given to load game
     * @param item JSON object representing one item from items list in JSON file
     */
    public void parseItemObject(Adventure generatedAdv, JSONObject item){
        Item nextItem = new Item();
        // get keys within item array element
        try { // error-checking for incorrectly formatted JSON file
            long id = (Long) item.get("id");
            String name = (String) item.get("name");
            String desc = (String) item.get("desc");
            setItemCheck(name, desc); // check for null values
            nextItem.setItem(id, name, desc); // store information in item object
        } catch (Exception e) {
            fileParsingError();
        }

        generatedAdv.addItem(nextItem); // add item to adventure
    }

    /**
     * parses keys within each Item object from item array in JSON file
     * @param generatedAdv represents the generated adventure using the file given to load game
     * @param room JSON object representing one room from rooms list in JSON file
     */
    public void parseRoomObject(Adventure generatedAdv, JSONObject room) {
        Room nextRoom = new Room();

        // parse keys within room array element
        nextRoom = setStartRoom(room, nextRoom); // mark first room in adventure as start room
        try { // error-checking for incorrectly formatted JSON file
            long roomID = (Long) room.get("id");
            String name = (String) room.get("name");
            String shortDesc = (String) room.get("short_description");
            String longDesc = (String) room.get("long_description");
            setRoomCheck(name, shortDesc, longDesc); // check for null values
            nextRoom.setRoomInfo(roomID, name, shortDesc, longDesc); // store information in Room object
        } catch (Exception e) {
            fileParsingError();
        }
        // parse and set room entrances and loot
        nextRoom = parseEntrance(room, nextRoom);
        nextRoom = parseLoot(generatedAdv, room, nextRoom);

        generatedAdv.addRoom(nextRoom); // add room to adventure
    }

    /**
     * set whether isStart to true or false depending on which room has start flag
     * @param room JSON object representing one room from rooms list in JSON file
     * @param nextRoom Room object representing the room being parsed
     * @return Room object with its start flag set
     */
    private Room setStartRoom(JSONObject room, Room nextRoom) {
        boolean isStart = false;

        String start = (String) room.get("start");
        if (start != null) {
            isStart = true;
        }
        nextRoom.setStart(isStart); // store information in Room object

        return nextRoom;
    }

    /**
     * parse and set room entrances
     * @param room JSON object representing one room from rooms list in JSON file
     * @param nextRoom Room object representing the room being parsed
     * @return Room object with its entrances set
     */
    private Room parseEntrance(JSONObject room, Room nextRoom) {
        JSONArray entranceList = (JSONArray) room.get("entrance"); // get keys within entrance array
        nextRoom.createEntranceMap(); // allocate memory for entrance map
        for (int i = 0; i < entranceList.size(); i++) {
            JSONObject entrance = (JSONObject) entranceList.get(i);
            try { // get keys within entrance array element, error-checking for incorrectly formatted JSON file
                long entranceID = (Long) entrance.get("id");
                String dir = (String) entrance.get("dir");
                setEntranceCheck(dir); // check for null values
                nextRoom.setEntrance(entranceID, dir); // store information in Room object
            } catch (Exception e) {
                fileParsingError();
            }
        }

        return nextRoom;
    }

    /**
     * parse and set loot in each room
     * @param generatedAdv generated adventure using the file given to load game
     * @param room JSON object representing one room from rooms list in JSON file
     * @param nextRoom Room object representing the room being parsed
     * @return Room object with its loot set
     */
    public Room parseLoot(Adventure generatedAdv, JSONObject room, Room nextRoom) {
        ArrayList<Item> allItems = generatedAdv.listAllItems();

        JSONArray lootList = (JSONArray) room.get("loot"); // get keys within loot array
        if (lootList != null) { // error-checking for no loot
            for (int i = 0; i < lootList.size(); i++) {
                JSONObject loot = (JSONObject) lootList.get(i);
                long lootID = (Long) loot.get("id");
                for (Item item : allItems) {
                    if (lootID == item.getItemID()) {
                        nextRoom.addRoomItem(item); // store room's loot in roomItems ArrayList of Room class
                    }
                }
            }
        }

        return nextRoom;
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
    public Room startLocation() {
        Room startRoom = new Room();

        System.out.println("\n[BEGINNING ADVENTURE]----------------------------------------------------");
        // find starting location
        for (int i = 0; i < myAdventure.getTotalNumRooms(); i++) {
            if (myAdventure.listAllRooms().get(i).getStart()) {
                startRoom = myAdventure.listAllRooms().get(i); // set current room to start room
                myPlayer.setCurrentRoom(startRoom);
                myAdventure.setPlayer(myPlayer);
                System.out.println(myAdventure.getCurrentRoomDescription() + "."); // display description of start room
            }
        }
        System.out.println("Items present:");
        printItems(startRoom);

        return startRoom;
    }

    /**
     * contains the game loop which prompts user input and helps user navigate through game
     * loop as long as input is invalid or "quit" has not been entered
     * @param currentRoom the room the adventure starts in; will change as game loop iterates
     */
    public void gameLoop(Room currentRoom) {
        Command nextCommand;
        boolean invalidInput = false;
        String userInput;
        String[] input;

        do {
            System.out.print("> ");
            userInput = scanner.nextLine();
            userInput = userInput.trim(); // remove leading or trailing whitespace
            try {
                nextCommand = null;
                nextCommand = parse(userInput); // parse user input into Command - if invalid command, throws exception
                input = userInput.split("\\s+", 2); // store everything after first word into second index
                invalidInput = checkInput(currentRoom, input);
                if (!invalidInput) {
                    currentRoom = play(currentRoom, input);
                    myPlayer.setCurrentRoom(currentRoom);
                    myAdventure.setPlayer(myPlayer);
                }
            } catch (InvalidCommandException e) {
                invalidInput = true;
                System.out.println(e.getMessage());
            }
        } while (invalidInput || !userInput.equals("quit"));
    }

    /**
     * wrapper method for parseUserCommand() method in Parser class
     * @param userInput
     * @return user input parsed into a Command
     * @throws InvalidCommandException if user does not input a command
     */
    private Command parse(String userInput) throws InvalidCommandException {
        return myParser.parseUserInput(userInput);
    }

    /**
     * parsing user input to see what they want
     * @param currentRoom a Room object representing the current room the user is in
     * @param input user input parsed into array of Strings
     * @return a boolean value representing whether the user input was invalid
     */
    private boolean checkInput(Room currentRoom, String[] input) {
        boolean invalidInput = false;

        if (input[0].equals("go")) {
            invalidInput = checkInputGo(currentRoom, input);
        } else if (input[0].equals("look")) {
            invalidInput = checkInputLook(currentRoom, input);
        } else if (input[0].equals("take")) {
            invalidInput = checkInputTake(currentRoom, input);
        } else if (input[0].equals("help")) {
            invalidInput = true;
            help();
            displayRoomInfo(myPlayer.getCurrentRoom()); // print room name and items in room
        } else if (input[0].equals("quit")) {
            confirmQuit(); // if user confirms, method exits program
            invalidInput = true;
            displayRoomInfo(myPlayer.getCurrentRoom()); // print room name and items in room
        }
        return invalidInput;
    }

    /**
     * display room name and items present in current room
     * @param currentRoom the room the user is in
     */
    private void displayRoomInfo(Room currentRoom) {
        System.out.println("\nYou are now in " + currentRoom.getName() + "."); // display room name
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
                invalidInput = isInvalidItem(currentRoom, input, false);
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
        boolean inventoryCreated = false;

        try { // error-checking for no items in current room
            invalidInput = isInvalidItem(currentRoom, input, false);
        } catch (Exception e) {
            invalidInput = true;
            System.out.println("Error: There are no items in this room/area.");
        }

        return invalidInput;
    }

    /**
     * used by the checkInputTake() and checkInputLook() to check for invalid input for "look" and "go" commands
     * @param currentRoom Room object representing the current room the user is in
     * @param input user input parsed into array of Strings
     * @param invalidInput boolean variable representing whether user input was invalid
     * @return a boolean variable representing whether user input is invalid; return as soon as var is set to true
     */
    private boolean isInvalidItem(Room currentRoom, String[] input, boolean invalidInput) {
        invalidInput = inInventory(input); // check whether items are in inventory
        if (invalidInput) {
            return invalidInput;
        }

        for (Item roomItem : currentRoom.listItems()) { // check whether item is in list of items in room
            if (!input[1].equals(roomItem.getName())) {
                invalidInput = true;
            } else {
                invalidInput = false;
                break; // NOTE: refactor code to remove break statement!
            }
        }
        if (invalidInput) {
            System.out.println("Error: There is no such item in this room/area.");
        }

        return invalidInput;
    }

    /**
     * checks inventory ArrayList to determine whether user is already carrying the item they want to "take"
     * @param input user input parsed into array of Strings
     * @return a boolean variable representing whether item from user input was already "taken"
     */
    private boolean inInventory(String[] input) {
        boolean invalidInput = false;

        if (myPlayer.getInventory() != null) { // if item is not in room, it may be in the inventory!
            invalidInput = myPlayer.isTakenItem(input[1]);
            if (invalidInput) {
                if (input[0].equals("take")) {
                    System.out.println("Error: You are already carrying this item.");
                    return invalidInput;
                } else if (input[0].equals("look")) {
                    System.out.println("Error: You may only 'look' at items in this room/area.");
                    return invalidInput;
                }
            }
        }

        return invalidInput;
    }

    /**
     * perform actions based on user input
     * @param theRoom temp variable to be used in this method to represent the current room
     * @param input user input parsed into array of Strings
     * @return an object of type Room to update the current room if the user input leads to a room from file
     * */
    public Room play(Room theRoom, String[] input) {
        // checking whether user wants to "go" somewhere, "look", or "take" an item
        if (input[0].equals("go")) {
            theRoom = playGo(theRoom, input);
        } else if (input[0].equals("look")) {
            playLook(theRoom, input);
        } else if (input[0].equals("take")) {
            playTake(theRoom, input);
        } else if (input[0].equals("inventory")) {
            System.out.println(myPlayer.getName().toUpperCase() + "'s Inventory:");
            printInventory();
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
        myAdventure.setPlayer(myPlayer);
        displayRoomInfo(currentRoom); // print room name and items in room

        /* TOSTRING CHECK
        System.out.println(currentRoom);
        */
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
        Item carriedItem = null;
        int carriedItemIndex = 0;
        boolean itemFound = false;

        // find index at which the item is in ArrayList of items in the room
        for (int i = 0; i < currentRoom.listItems().size(); i++) {
            if (input[1].equals(currentRoom.listItems().get(i).getName())) {
                itemFound = true;
                carriedItemIndex = i;
                carriedItem = currentRoom.listItems().get(i);
            }
        }
        if (itemFound) {
            myPlayer.addToInventory(carriedItem);
            currentRoom.removeItem(carriedItemIndex);
            currentRoom.deleteList();
            System.out.println("You are now carrying the " + carriedItem.getName() + ".");
        }
    }

    /**
     * print items in room
     * @param theRoom the room whose items will be printed out
     */
    private void printItems(Room theRoom) {
        try { // error-checking for uninitialized roomItems list (i.e. no items were originally in given room)
            if (theRoom.listItems().size() != 0) {
                for (int i = 0; i < theRoom.listItems().size(); i++) {
                    System.out.printf("* %s\n", theRoom.listItems().get(i).getName());
                }
            } else {
                System.out.println("There are no items in this room/area.");
            }
        } catch (Exception e) {
            System.out.println("There are no items in this room/area.");
        }
    }

    /**
     * print items in inventory
     */
    private void printInventory() {
        try {
            for (int i = 0; i < myPlayer.getInventory().size(); i++) {
                System.out.printf("* %s\n", myPlayer.getInventory().get(i).getName());
            }
        } catch (Exception e) {
            System.out.println("There are no items in your inventory.");
        }
    }

    /**
     * prints start message once user selects file to read from
     */
    public void startMessage() {
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
                + " -- move from room to room in the adventure using the keyword 'go' and the subjects: N, S, E or W.\n"
                + " -- move up or down stairs in the adventure when you type 'go up' or 'go down'.\n"
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
    public void commandLineError() {
        System.out.println("Usage: [-a] <filename.json> or [-l] <game save name>");
        System.exit(-1);
    }

    /**
     * prints out error message for incorrectly formatted file
     */
    public void fileParsingError() {
        System.out.println("Error: JSON file is incorrectly formatted.");
        System.out.println("Exiting...");
        System.exit(-1);
    }

    /**
     * prints out error message if file could not be loaded
     */
    public void fileLoadingError() {
        System.out.println("Error: File could not be loaded. Please try verifying your filename.");
        System.out.println("Exiting...");
        System.exit(-1);
    }

    /**
     * confirm whether user wants to quit program
     */
    public void confirmQuit() {
        boolean invalidInput;
        String userInput;

        System.out.println("\nAre you sure you'd like to quit? (Y/N)");
        do {
            System.out.print("> ");
            userInput = scanner.nextLine();
            if (userInput.equals("Y")) {
                invalidInput = false;
                quit();
            } else if (userInput.equals("N")) {
                invalidInput = false;
            } else {
                invalidInput = true;
                System.out.println("I don't understand...");
            }
        } while (invalidInput);
    }

    /**
     * exit program if user enters "quit"
     */
    public void quit() {
        boolean invalidInput = false;
        System.out.println("Would you like to save your progress? (Y/N)");
        do {
            System.out.print("> ");
            String userInput = scanner.nextLine();
            if (userInput.equals("Y")) {
                invalidInput = false;
                saveGame();
            } else if (userInput.equals("N")) {
                invalidInput = false;
            } else {
                invalidInput = true;
                System.out.println("I don't understand...");
            }
        } while (invalidInput);

        System.out.println("\n[THANKS FOR PLAYING!]----------------------------------------------------\n");
        System.exit(0);
    }

    /**
     * serialize game before exiting if user desires
     */
    public void saveGame() {
        String filename = setAdventureName();
        Adventure savedAdventure = new Adventure();
//        Game savedGame = new Game();

        try {
            // saving object in a file
            FileOutputStream outPutStream = new FileOutputStream(filename);
            ObjectOutputStream outPutDest = new ObjectOutputStream(outPutStream);
            // method for serialization of object
            outPutDest.writeObject(savedAdventure);
//            outPutDest.writeObject(savedGame);
            // close streams
            outPutDest.close();
            outPutStream.close();
            System.out.println("Your game has been saved!");
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /**
     * prompt user for the name of the adventure they want to save
     * @return filename of user's choice
     */
    private String setAdventureName() {
        String filename = null;
        boolean invalidFileName = false;

        System.out.println("Enter a name for your saved adventure:");
        do {
            System.out.print("> ");
            filename = scanner.nextLine();
            filename = filename.trim();
            if (filename.equals("null") || filename.isEmpty()) {
                invalidFileName = true;
            }
        } while (filename.equals("null") || filename.isEmpty());
        myPlayer.setSaveGameName(filename);

        return filename;
    }
}

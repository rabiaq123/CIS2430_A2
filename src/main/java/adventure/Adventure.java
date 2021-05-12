package adventure;

import java.util.ArrayList;

public class Adventure implements java.io.Serializable {
    /* you will need to add some private member variables */
    // static -> stays in same mem loc, shared among all class instances
    private static final long serialVersionUID = 676358393088103531L;
    private ArrayList<Room> allRooms = new ArrayList<>();
    private ArrayList<Item> allItems = new ArrayList<>();
    private Player myPlayer; // keeping track of current game

    /* ======== Required public methods ========== */

    /**
     * REQUIRED lists all rooms in adventure
     * @return ArrayList of all rooms in adventure
     */
    public ArrayList<Room> listAllRooms() {
        if (allRooms == null) {
            setAllRooms();
        }
        return allRooms;
    }

    /**
     * REQUIRED lists all items in adventure
     * @return ArrayList of all items in adventure
     */
    public ArrayList<Item> listAllItems() {
        return allItems;
    }

    /**
     * REQUIRED returns short description of current room
     * @return String value representing short description of current room
     */
    public String getCurrentRoomDescription() {
        return getCurrentRoom().getShortDescription();
    }

    /**
     * REQUIRED FOR REGRESSION TESTING?? wrapper method for getCurrentRoom() method in Player class
     * @return the current room the user is in
     */
    public Room getCurrentRoom() {
        return myPlayer.getCurrentRoom();
    }

    /* you may wish to add additional methods */

    /**
     * get (updated) Player member variable
     * @return Player object representing the myPlayer instance variable being updated during game play
     */
    public Player getPlayer() {
        return myPlayer;
    }

    /**
     * returns total number of rooms
     * @return an int value representing the total number of rooms in the adventure
     */
    public int getTotalNumRooms() {
        return allRooms.size();
    }

    /**
     * add room to allRooms ArrayList
     * @param room room to be added to allRooms ArrayList
     */
    public void addRoom(Room room) {
        allRooms.add(room);
    }

    /**
     * add item to allItems ArrayList
     * @param item item to be added to allItems ArrayList
     */
    public void addItem(Item item) {
        allItems.add(item);
    }

    /**
     * REQUIRED FOR REGRESSION TESTING sets current room the user is in
     * wrapper method for setCurrentRoom() method in Player class
     * @param currentRoom the current room the user is in
     */
    public void setCurrentRoom(Room currentRoom) {
        myPlayer.setCurrentRoom(currentRoom);
    }

    /**
     * use Player instance for methods in this class to access currentRoom instance
     * @param player object to keep track of where the player is in the game
     */
    public void setMyPlayer(Player player) {
        myPlayer = player;
    }

    /**
     * REQUIRED (must have public setters for all member variables)
     * set allItems instance variable in Adventure class
     */
    public void setAllItems() {
        allItems = new ArrayList<>();
    }

    /**
     * REQUIRED (must have public setters for all member variables)
     * set allRooms instance variable in Adventure class
     */
    public void setAllRooms() {
        allRooms = new ArrayList<>();
    }

    /**
     * REQUIRED toString method prints String instead of mem location on accident
     * @return String a String representing the adventure
     */
    @Override
    public String toString() {
        String input;

        // store all items in adventure
        input = "\nall items in adventure: ";
        if (listAllItems() != null) {
            for (Item item : listAllItems()) {
                input = input.concat("/" + item.getName() + "/ ");
            }
        } else {
            input = input + "no items";
        }
        // store all rooms in adventure
        input = input.concat("\nall rooms in adventure: ");
        if (listAllRooms() != null) {
            for (Room room : listAllRooms()) {
                input = input.concat("/" + room.getName() + "/ ");
            }
        } else {
            input = input + "no rooms";
        }
        // store current room description
        input = input.concat("\ncurrent room: " + getCurrentRoom().getName() + " -> " + getCurrentRoomDescription());

        return input;
    }
}

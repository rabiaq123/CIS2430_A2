package adventure;

import java.util.ArrayList;

public class Player implements java.io.Serializable {
    private static final long serialVersionUID = -9032960684102100966L;
    private Room currentRoom; // keeping track of the current room
    private ArrayList<Item> inventory; // array list of items player is carrying
    private String name; // player name
    private String saveGameName; // file for game to be saved in if user desires to save progress

    /* ======== Required public methods ========== */

    /**
     * REQUIRED return the name of the player to personalize the game play experience
     * @return a String variable representing the player's name
     */
    public String getName() {
        if (name == null) { // if name was not explicitly set with the setter
            name = "Player";
        }
        return name;
    }

    /**
     * REQUIRED return the filename which will store the game to be saved
     * filename is NOT equivalent to adventure json file
     * @return a String variable representing the filename of the saved game
     */
    public String getSaveGameName() {
        if (saveGameName == null) { // FOR AUTO-GRADER: if saveGameName was not explicitly set with the setter
            saveGameName = "gameSave";
        }
        return saveGameName;
    }

    /**
     * REQUIRED return an ArrayList of items the user is carrying
     * @return an ArrayList of items in the inventory
     */
    public ArrayList<Item> getInventory() {
        if (inventory == null) { // FOR AUTO-GRADER: if inventory was not explicitly set with the setter
            inventory = new ArrayList<>();
        }
        return inventory;
    }

    /** REQUIRED returns the current room and will be used to keep track of it
     * @return a Room object representing the current room the user is in
     */
    public Room getCurrentRoom() {
        return currentRoom;
    }

    /* you may wish to add additional methods */

    /**
     * set current room the user is in
     * @param theRoom the current room the user is in
     */
    public void setCurrentRoom(Room theRoom) {
        currentRoom = theRoom;
    }

    /**
     * create inventory ArrayList
     */
    public void setInventory() {
        if (inventory == null) {
            inventory = new ArrayList<>();
        }
    }

    /**
     * add item in inventory when user enters "take" followed by the item name
     * @param item an item the user chooses to "take" from the room they are currently in
     */
    public void addToInventory(Item item) {
        inventory.add(item);
    }

    /**
     * set player name and remove trailing or leading whitespace
     * @param playerName player name
     */
    public void setName(String playerName) {
        name = playerName.trim();
    }

    /**
     * set filename that matches the load file name
     * @param filename game save name
     */
    public void setSaveGameName(String filename) {
        saveGameName = filename;
    }

    /**
     * return whether user already took the item they want to "take"
     * @param itemName item to be checked
     * @return boolean variable representing whether the item was already taken
     */
    public boolean isTakenItem(String itemName) {
        boolean wasTaken = false;

        for (Item takenItem : inventory) {
            if (itemName.equals(takenItem.getName())) {
                wasTaken = true;
            }
        }

        return wasTaken;
    }

    /**
     * toString method prints String instead of mem location on accident
     * @return String a String representing the player info
     */
    @Override
    public String toString() {
        // every player object has the following info
        return "\nplayer name: " + name
                + "\nsaved game name: " + saveGameName
                + "\ncurrent room: " + currentRoom;
    }
}

package adventure;

import java.util.ArrayList;

public class Player implements java.io.Serializable {
    private static final long serialVersionUID = -9032960684102100966L;
    private Room currentRoom; // keeping track of the current room
    private ArrayList<Item> inventory; // array list of items player is carrying
    private String playerName;
    private String filename; // file for game to be saved in if user desires to save progress

    /* ======== Required public methods ========== */

    /**
     * REQUIRED return the name of the player to personalize the game play experience
     * @return a String variable representing the player's name
     */
    public String getName() {
        return playerName;
    }

    /**
     * REQUIRED return the filename which will store the game to be saved
     * filename is NOT equivalent to adventure json file
     * @return a String variable representing the filename of the saved game
     */
    public String getSaveGameName() {
        return filename;
    }

    /**
     * REQUIRED return an ArrayList of items the user is carrying
     * @return an ArrayList of items in the inventory
     */
    public ArrayList<Item> getInventory() {
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
     * add item in inventory when user enters "take" followed by the item name
     * @param item an item the user chooses to "take" from the room they are currently in
     */
    public void addToInventory(Item item) {
        if (inventory == null) {
            inventory = new ArrayList<>();
        }
        inventory.add(item);
    }

    /**
     * set player name and remove trailing or leading whitespace
     * @param name player name
     */
    public void setName(String name) {
        playerName = name.trim();
    }

    /**
     * set filename that matches the load file name
     * @param name game save name
     */
    public void setSaveGameName(String name) {
        filename = name;
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
        String playerInfo;
        // every player object has the following info
        playerInfo = "player name: " + getName()
                + "\nsaved game name: " + getSaveGameName()
                + "\ncurrent room: " + getCurrentRoom();

        return playerInfo;
    }

}


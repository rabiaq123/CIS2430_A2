package adventure;

import java.util.ArrayList;

public class Adventure implements java.io.Serializable {
    /* you will need to add some private member variables */
    // static -> stays in same mem loc, shared among all class instances
    private static final long serialVersionUID = 676358393088103531L;
    private static ArrayList<Room> allRooms = new ArrayList<>();
    private static ArrayList<Item> allItems = new ArrayList<>();
    private Player myPlayer; // keeping track of current game

    /* ======== Required public methods ========== */

    /**
     * REQUIRED lists all rooms in adventure
     * @return ArrayList of all rooms in adventure
     */
    public ArrayList<Room> listAllRooms() {
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
        return myPlayer.getCurrentRoom().getShortDescription();
    }

    /* you may wish to add additional methods */

    /**
     * returns total number of rooms
     * @return an int value representing the total number of rooms in the adventure
     * */
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
     * use Player instance for methods in this class to access currentRoom instance
     * @param player object to keep track of where the player is in the game
     */
    public void setPlayer(Player player) {
        myPlayer = player;
    }

    /**
     * REQUIRED toString method prints String instead of mem location on accident
     * @return String a String representing the adventure
     */
    @Override
    public String toString() {
        String input = "all items in adventure: ";
        // store all items
        if (listAllItems() != null) {
            for (Item item : listAllItems()) {
                input = input.concat("/" + item.getName() + "/ ");
            }
        } else {
            input.concat("no items");
        }
        // store all rooms
        input.concat("\nall rooms in adventure: ");
        if (listAllRooms() != null) {
            for (Room room : listAllRooms()) {
                input = input.concat("/" + room.getName() + "/ ");
            }
        } else {
            input.concat("no rooms");
        }
        // store current room descriptoin
        input.concat("\ncurrent room description: ");
        input.concat(getCurrentRoomDescription());

        return input;
    }

}

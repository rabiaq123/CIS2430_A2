package adventure;

import java.util.ArrayList;
import java.util.HashMap;

public class Room implements java.io.Serializable {
    /* you will need to add some private member variables */
    private static final long serialVersionUID = 5218467369631712835L;
    private Room connectedRoom; // Room object connected to current Room instance
    private Adventure myAdv = new Adventure();
    private ArrayList<Item> roomItems; // array list of items in room
    private String roomName;
    private String shortDesc;
    private String longDesc;
    private long roomID;
    private boolean isStart;
    private HashMap<String, Long> entrances;

    /* required public methods */

    /**
     * REQUIRED lists all the items in the room (i.e. loot)
     * @return an ArrayList containing elements of type Item representing all items in the room
     */
    public ArrayList<Item> listItems(){
        return roomItems;
    }

    /**
     * REQUIRED returns name of room
     * @return a String value representing the room name
     */
    public String getName(){
        return roomName;
    }

    /**
     * REQUIRED returns long description of room
     * @return String value representing long description of room
     */
    public String getLongDescription(){
        return longDesc;
    }

    /**
     * REQUIRED using direction from user input to find connected room
     * @param givenDir the direction the user wants to go
     * @return an object of type Room which represents the connected room (if there is an entrance in that direction)
     */
    public Room getConnectedRoom(String givenDir) {
        connectedRoom = null; // using this removes the need for a break statement

        if (entrances.get(givenDir) != null) { // if parameter for .get() does not exist in hash map
            for (int j = 0; j < myAdv.getTotalNumRooms(); j++) {
                if (entrances.get(givenDir) == myAdv.listAllRooms().get(j).getRoomID()) {
                    connectedRoom = myAdv.listAllRooms().get(j); // set connected room
                }
            }
        }

        return connectedRoom;
    }

    /* you may wish to add some helper methods*/

    /**
     * get room short description
     * @return String value representing the short description of the room
     */
    public String getShortDescription() {
        return shortDesc;
    }

    /**
     * returns room id
     * @return a long value representing the room id
     */
    public long getRoomID() {
        return roomID;
    }

    /**
     * set room info (id, name, short and long description) from JSON file
     * @param id the room id
     * @param name the room name
     * @param shortDescription a short description of the room
     * @param longDescription a long description of the room
     */
    public void setRoomInfo(long id, String name, String shortDescription, String longDescription) {
        roomID = id;
        roomName = name;
        shortDesc = shortDescription;
        longDesc = longDescription;
    }

    /**
     * add entrance for room from JSON file to Map
     * @param id the id of the exit from the current room
     * @param direction the direction in which the exit is in the current room
     */
    public void setEntrance(long id, String direction) {
        entrances.put(direction, id);
    }

    /**
     * create hash map for entrances
     */
    public void createEntranceMap() {
        entrances = new HashMap<String, Long>();
    }

    /**
     * add room item in roomItems ArrayList
     * @param item item in room from JSON file
     */
    public void addRoomItem(Item item) {
        if (roomItems == null) {
            roomItems = new ArrayList<>();
        }
        roomItems.add(item);
    }

    /**
     * return room the user starts with in adventure
     * @return boolean variable representing whether room is starting room
     */
    public boolean getStart() {
        return isStart;
    }

    /**
     * set Room with "start" key as the start of the game
     * @param start used to represent whether room was marked as the starting room in the JSON file
     */
    public void setStart(boolean start) {
        isStart = start;
    }

    /**
     * remove current index from roomItems ArrayList once user "takes" item from room
     * @param index the ArrayList index of the Item to be removed
     */
    public void removeItem(int index) {
        listItems().remove(index);
    }

    /**
     * set roomItems ArrayList to null if no items are left in the ArrayList
     */
    public void deleteList() {
        if (roomItems != null && roomItems.size() == 0) {
            roomItems = null;
        }
    }

    /**
     * toString method prints String instead of mem location on accident
     * @return String a String representing the name of the room the user is in
     */
    @Override
    public String toString() {
        String roomInfo;

        // every room has the following info
        roomInfo = "room name: " + roomName
                + "\nroom ID: " + roomID
                + "\nstart room: " + isStart
                + "\nshort description: " + shortDesc
                + "\nlong description: " + longDesc;

        // if the room contains items
        String items = "\nitems: ";
        try {
            for (Item item : roomItems) {
                items = items.concat("/" + item.getName() + "/ ");
            }
        } catch (Exception e) {
            items = items.concat("no items in room");
        }
        roomInfo = roomInfo.concat(items);

        return roomInfo;
    }
}

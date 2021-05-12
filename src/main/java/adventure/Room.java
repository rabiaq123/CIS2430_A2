package adventure;

import java.util.ArrayList;
import java.util.HashMap;

public class Room implements java.io.Serializable {
    /* you will need to add some private member variables */
    private static final long serialVersionUID = 5218467369631712835L;
    private Adventure myAdv;
    private ArrayList<Item> roomItems; // array list of items in room
    private HashMap<String, Long> entrances;
    private String name;
    private String shortDescription;
    private String longDescription;
    private long roomId;
    private boolean isStart;

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
        return name;
    }

    /**
     * REQUIRED returns long description of room
     * @return String value representing long description of room
     */
    public String getLongDescription(){
        return longDescription;
    }

    /**
     * REQUIRED using direction from user input to find connected room
     * @param givenDir the direction the user wants to go
     * @return Room object connected to current Room instance (if there is an entrance in given direction)
     */
    public Room getConnectedRoom(String givenDir) {
        Room connectedRoom = null; // using this removes the need for a break statement
        if (entrances.get(givenDir) != null) { // if entrance in given direction exists
            for (int j = 0; j < myAdv.getTotalNumRooms(); j++) {
                if (entrances.get(givenDir) == myAdv.listAllRooms().get(j).getRoomId()) {
                    connectedRoom = myAdv.listAllRooms().get(j); // set connected room
                }
            }
        }

        return connectedRoom;
    }

    /**
     * REQUIRED set room name from JSONObject
     * @param roomName room name
     */
    public void setName(String roomName) {
        name = roomName;
    }

    /* you may wish to add some helper methods*/

    /**
     * get room short description
     * @return String value representing the short description of the room
     */
    public String getShortDescription() {
        return shortDescription;
    }

    /**
     * returns room id
     * @return a long value representing the room id
     */
    public long getRoomId() {
        return roomId;
    }

    /**
     * set room id from JSONObject
     * @param id room id
     */
    public void setRoomId(long id) {
        roomId = id;
    }

    /**
     * set short description of room from JSONObject
     * @param shortDesc short description of room
     */
    public void setShortDescription(String shortDesc) {
        shortDescription = shortDesc;
    }

    /**
     * set long description of room from JSONObject
     * @param longDesc long description of room
     */
    public void setLongDescription(String longDesc) {
        longDescription = longDesc;
    }

    /**
     * add room entrance from JSON file to entrances Map
     * @param id the id of the exit from the current room
     * @param direction the direction in which the exit is in the current room
     */
    public void addEntrance(long id, String direction) {
        entrances.put(direction, id);
    }

    /**
     * create hash map for entrances
     */
    public void setEntrances() {
        entrances = new HashMap<String, Long>();
    }

    /**
     * create ArrayList of room items
     */
    public void setRoomItems() {
        if (roomItems == null) {
            roomItems = new ArrayList<>();
        }
    }

    /**
     * add room item in roomItems ArrayList
     * @param item item in room from JSON file
     */
    public void addRoomItem(Item item) {
        roomItems.add(item);
    }

    /**
     * return start flag of Room object
     * @return boolean variable representing whether room is starting room
     */
    public boolean getIsStart() {
        return isStart;
    }

    /**
     * set Room with "start" key as the start of the game
     * @param start used to represent whether room was marked as the starting room in the JSON file
     */
    public void setIsStart(boolean start) {
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
    public void deleteListItems() {
        if (roomItems != null && roomItems.size() == 0) {
            roomItems = null;
        }
    }

    /**
     * update Adventure instance in Room class every time new room is added
     * and Game class's Adventure instance is updated
     * @param adv Adventure object representing the adventure
     */
    public void setMyAdv(Adventure adv) {
        if (adv == null) {
            myAdv = new Adventure();
        }
        myAdv = adv;
    }

    /**
     * toString method prints String instead of mem location on accident
     * @return String a String representing the name of the room the user is in
     */
    @Override
    public String toString() {
        String roomInfo;
        // every room has the following info
        roomInfo = "\nroom name: " + name
                + "\nroom ID: " + roomId
                + "\nstart room: " + isStart
                + "\nshort description: " + shortDescription
                + "\nlong description: " + longDescription;
        // if the room contains items
        String items = "\nitems: ";
        if (roomItems != null) {
            for (Item item : roomItems) {
                items = items.concat("/" + item.getName() + "/ ");
            }
        } else {
            items = items.concat("no items in room");
        }
        roomInfo = roomInfo + items;

        return roomInfo;
    }
}

package adventure;

public class Item implements java.io.Serializable {
    /* you will need to add some private member variables */
    private static final long serialVersionUID = -4546327969882507375L;
    private Room containingRoom = new Room();
    private Adventure myAdv;
    private Player myPlayer = new Player();
    private String name;
    private String longDescription;
    private long itemId;

    /* required public methods */

    /**
     * REQUIRED returns name of item
     * @return a String representing the name of the item
     */
    public String getName() {
        return name;
    }

    /**
     * REQUIRED returns long description of item
     * @return a String representing the description of the item
     */
    public String getLongDescription() {
        return longDescription;
    }

    /**
     * REQUIRED returns a reference to the room that contains the item
     * @return an object of type Room representing the room that contains the item
     */
    public Room getContainingRoom() {
        setContainingRoom();
        return containingRoom;
    }

    /**
     * REQUIRED (must have public setters for all member variables)
     * set containingRoom instance variable in Command class
     */
    public void setContainingRoom() {
        if (myPlayer.getInventory() == null || myPlayer.getInventory().size() == 0) { // if nothing in inventory
            for (Room room : myAdv.listAllRooms()) { // loop through all rooms
                for (int j = 0; j < room.listItems().size(); j++) { // loop through each room's items
                    if (itemId == room.listItems().get(j).getItemId()) {
                        containingRoom = room;
                    }
                }
            }
        } else {
            for (Item takenItem : myPlayer.getInventory()) {
                if (itemId == takenItem.getItemId()) {
                    containingRoom = null;
                }
            }
        }
    }


    /**
     * REQUIRED (must have public setters for all member variables)
     * set myPlayer instance variable in Command class
     */
    public void setMyPlayer() {
        myPlayer = new Player();
    }

    /* you may wish to add some helper methods */

    /**
     * return item id
     * @return a long value representing the id of the item
     */
    public long getItemId() {
        return itemId; // get item id for specified item in roomItems ArrayList
    }

    /**
     * set item name from JSONObject
     * @param itemName item name
     */
    public void setName(String itemName) {
        name = itemName;
    }

    /**
     * set item id from JSONObject
     * @param id item id
     */
    public void setItemId(long id) {
        itemId = id;
    }

    /**
     * set description of item from JSONObject
     * @param description description of item
     */
    public void setLongDescription(String description) {
        longDescription = description;
    }

    /**
     * update Adventure instance in Item class every time new item is added
     * and Game class's Adventure instance is updated
     * @param adv Adventure object representing the adventure
     */
    public void setMyAdv(Adventure adv) {
        myAdv = adv;
    }

    /**
     * toString method prints String instead of mem location on accident
     * @return String a String representing the item info
     */
    @Override
    public String toString() {
        // every item has the following info
        String itemInfo = "\nitem name: " + name
                + "\nitem description: " + longDescription
                + "\ncontaining room: " + getContainingRoom();

        return itemInfo;
    }
}

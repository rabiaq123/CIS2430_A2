package adventure;

public class Item implements java.io.Serializable {
    /* you will need to add some private member variables */
    private static final long serialVersionUID = -4546327969882507375L;
    private Room myRoom = new Room();
    private Adventure myAdv = new Adventure();
    private Player myPlayer = new Player();
    private String itemName;
    private String desc;
    private long itemID;

    /* required public methods */

    /**
     * returns name of item
     * @return a String representing the name of the item
     */
    public String getName() {
        return itemName;
    }

    /**
     * returns long description of item
     * @return a String representing the description of the item
     */
    public String getLongDescription() {
        return desc;
    }

    /**
     * returns a reference to the room that contains the item
     * @return an object of type Room representing the room that contains the item
     */
    public Room getContainingRoom() {
        if (myPlayer.getInventory() == null || myPlayer.getInventory().size() == 0) { // if nothing in inventory
            for (Room room : myAdv.listAllRooms()) { // loop through all rooms
                for (int j = 0; j < room.listItems().size(); j++) { // loop through each room's items
                    if (itemID == room.listItems().get(j).getItemID()) {
                        myRoom = room;
                    }
                }
            }
        } else {
            for (Item takenItem : myPlayer.getInventory()) {
                if (itemID == takenItem.getItemID()) {
                    myRoom = null;
                }
            }
        }

        return myRoom;
    }

    /* you may wish to add some helper methods */

    /**
     * set keys (id, name, and long description) of item in item array from JSON file
     * @param id the id of the item
     * @param name the name of the item
     * @param description the description of the item
     */
    public void setItem(long id, String name, String description) {
        itemID = id;
        itemName = name;
        desc = description;
    }

    /**
     * return item id
     * @return a long value representing the id of the item
     */
    public long getItemID() {
        return itemID; // get item id for specified item in roomItems ArrayList
    }

    /**
     * toString method prints String instead of mem location on accident
     * @return String a String representing the item info
     */
    @Override
    public String toString() {
        String itemInfo;
        // every item has the following info
        itemInfo = "item name: " + itemName
                + "\nitem description: " + desc
                + "\ncontaining room: " + getContainingRoom();

        return itemInfo;
    }

}

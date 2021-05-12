package adventure;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class RoomTest{
    private Room testRoom;

    @Before
    public void setup(){
        testRoom = new Room();
    }

    @Test
    public void testSetNameWithValidInput(){
        System.out.println("Testing setName with valid room name");
        String roomName = "one";
        testRoom.setName(roomName);
        assertEquals(testRoom.getName(), roomName);
    }

    @Test
    public void testSetIdWithValidInput(){
        System.out.println("Testing setRoomId with valid ID");
        long roomId = 3;
        testRoom.setRoomId(roomId);
        assertEquals(testRoom.getRoomId(), roomId);
    }

    @Test
    public void testSetShortDescWithValidInput(){
        System.out.println("Testing setShortDescription with valid short description");
        String roomShortDesc = "this is room one";
        testRoom.setShortDescription(roomShortDesc);
        assertEquals(testRoom.getShortDescription(), roomShortDesc);
    }

    @Test
    public void testSetLongDescWithValidInput(){
        System.out.println("Testing setLongDescription with valid long description");
        String roomLongDesc = "this is room one's longer description";
        testRoom.setLongDescription(roomLongDesc);
        assertEquals(testRoom.getLongDescription(), roomLongDesc);
    }

    @Test
    public void testGetRoomIdWithValidInput() {
        System.out.println("Testing getRoomId with valid id");
        testRoom.setRoomId(12);
        assertEquals(12, testRoom.getRoomId());
    }

    @Test
    public void testSetIsStartWithValidInput() {
        System.out.println("Testing getIsStart with valid start flag");
        testRoom.setIsStart(false);
        assertFalse(testRoom.getIsStart());
    }

    @Test
    public void testGetConnectedRoomWithValidInput() {
        System.out.println("Testing getConnectedRoom with valid direction");
        // create new adventure and add new room to it
        Adventure adv = new Adventure();
        Room currRoom = new Room();
        currRoom.setRoomId(12);
        currRoom.setEntrances();
        currRoom.addEntrance(11, "N"); // add connection to test room
        // set up test room
        testRoom.setMyAdv(adv);
        testRoom.setRoomId(11);
        testRoom.setEntrances();
        testRoom.addEntrance(12, "S"); // add connection to temp room
        // add both rooms to adventure
        adv.addRoom(currRoom);
        adv.addRoom(testRoom);
        assertEquals(currRoom, testRoom.getConnectedRoom("S"));
    }
}
package adventure;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.Before;


public class RoomTest{
    private Room testRoom;

    @Before
    public void setup(){
        testRoom = new Room();

    }

    @Test
    public void testSetNameWithValidInput(){
        System.out.println("Testing setRoomInfo with valid name");
        long roomId = 3;
        String roomName = "one";
        String roomShortDesc = "this is room one";
        String roomLongDesc = "this is room one's longer description";
        testRoom.setRoomInfo(roomId, roomName, roomShortDesc, roomLongDesc);
        assertTrue(testRoom.getName().equals(roomName));
    }

    @Test
    public void testSetIdWithValidInput(){
        System.out.println("Testing setRoomInfo with valid ID");
        long roomId = 3;
        String roomName = "one";
        String roomShortDesc = "this is room one";
        String roomLongDesc = "this is room one's longer description";
        testRoom.setRoomInfo(roomId, roomName, roomShortDesc, roomLongDesc);
        assertTrue(testRoom.getRoomID() == roomId);
    }

    @Test
    public void testSetShortDescWithValidInput(){
        System.out.println("Testing setRoomInfo with valid short description");
        long roomId = 3;
        String roomName = "one";
        String roomShortDesc = "this is room one";
        String roomLongDesc = "this is room one's longer description";
        testRoom.setRoomInfo(roomId, roomName, roomShortDesc, roomLongDesc);
        assertTrue(testRoom.getShortDescription().equals(roomShortDesc));
    }

    @Test
    public void testSetLongDescWithValidInput(){
        System.out.println("Testing setRoomInfo with valid long description");
        long roomId = 3;
        String roomName = "one";
        String roomShortDesc = "this is room one";
        String roomLongDesc = "this is room one's longer description";
        testRoom.setRoomInfo(roomId, roomName, roomShortDesc, roomLongDesc);
        assertTrue(testRoom.getLongDescription().equals(roomLongDesc));
    }

}
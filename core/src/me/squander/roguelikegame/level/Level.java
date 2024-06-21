package me.squander.roguelikegame.level;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import me.squander.roguelikegame.Room;
import me.squander.roguelikegame.RoomType;

import java.util.*;

public class Level {
    private final HashMap<Vector2, Room> generatedRooms = new HashMap<>();
    private final Random random = new Random();

    public Level(){
        this.generateRooms();
    }

    private void generateRooms(){
        Queue<Vector2> inProgress = new LinkedList<>();
        Vector2 startRoomPosition = new Vector2(0, 0);
        int maxRoomsCount = 24;
        int currentGeneratedRooms = 0;

        inProgress.add(startRoomPosition);
        generatedRooms.put(startRoomPosition, new Room(RoomType.START));
        currentGeneratedRooms++;

        while(currentGeneratedRooms < maxRoomsCount){
            if(inProgress.isEmpty()){
                inProgress.add(this.getRandomRoom());
            }

            Vector2 currentRoom = inProgress.poll();
            if(currentRoom == null) break;

            int roomsToGenerate = this.getRoomCountsForGenerate();
            int[] dirForRoom = this.getValidDirForRoom(currentRoom, roomsToGenerate);

            for (int i = 0; i < roomsToGenerate; i++) {
                Vector2 nextRoom = this.getRoomsAround(currentRoom)[dirForRoom[i]];
                if(!generatedRooms.containsKey(nextRoom)){
                    generatedRooms.put(nextRoom, new Room(RoomType.BASE));
                    inProgress.add(nextRoom);
                    currentGeneratedRooms++;
                    if (currentGeneratedRooms >= maxRoomsCount) break;
                }
            }

        }

        System.out.println("SET END ROOM");
        Vector2 endRoomPosition = this.findTheFarthestRoom(startRoomPosition);
        generatedRooms.put(endRoomPosition, new Room(RoomType.END));

        System.out.println("SET KEY ROOM");
        this.generateKeyRooms(startRoomPosition, endRoomPosition);

        System.out.println("MAX ROOMS TO GENERATE " + maxRoomsCount);
        System.out.println("ROOMS GENERATED " + currentGeneratedRooms);
    }

    private void generateKeyRooms(Vector2 startRoomPosition, Vector2 endRoomPosition){
        List<Vector2> roomsList = new ArrayList<>(generatedRooms.keySet());
        List<Vector2> keyRoomList = new ArrayList<>();
        int keyRooms = 0;
        while (keyRooms < 3){
            Vector2 keyRoomPosition = roomsList.get(this.random.nextInt(roomsList.size()));
            if(keyRoomPosition.equals(startRoomPosition) || keyRoomPosition.equals(endRoomPosition)) continue;
            if(keyRoomList.contains(keyRoomPosition)) continue;

            if(!this.checkRoomTypeAround(keyRoomPosition, RoomType.KEY) && !this.checkRoomTypeAround(keyRoomPosition, RoomType.START)) {
                generatedRooms.put(keyRoomPosition, new Room(RoomType.KEY));
                keyRoomList.add(keyRoomPosition);
                keyRooms++;
            }
        }

        System.out.println("KEY ROOMS GENERATED " + keyRooms);
    }

    public Vector2[] getRoomsAround(Vector2 v){
        Vector2[] result = new Vector2[4];
        result[0] = new Vector2(v.x, v.y + 1);
        result[1] = new Vector2(v.x + 1, v.y);
        result[2] = new Vector2(v.x, v.y - 1);
        result[3] = new Vector2(v.x - 1, v.y);
        return result;
    }

    public int getRoomCountsForGenerate() {
        float randomValue = this.random.nextFloat();
        return randomValue < 0.05 ? 3 : randomValue < 0.25f ? 2 : 1;
    }

    public int[] getValidDirForRoom(Vector2 currentRoom, int roomsCount) {
        List<Integer> availableDirections = Arrays.asList(0, 1, 2, 3);
        Collections.shuffle(availableDirections, random);

        List<Integer> validDirections = new ArrayList<>();
        for (int i = 0; i < availableDirections.size(); i++) {
            int dir = availableDirections.get(i);
            Vector2 nextRoom = this.getRoomsAround(currentRoom)[dir];
            if (!generatedRooms.containsKey(nextRoom) && this.getExistingRoomsCount(nextRoom) < 2) {
                validDirections.add(dir);
                if (validDirections.size() == roomsCount) {
                    break;
                }
            }
        }

        if (validDirections.size() < roomsCount) {
            for (int i = 0; i < availableDirections.size(); i++) {
                int dir = availableDirections.get(i);
                if (!validDirections.contains(dir)) {
                    validDirections.add(dir);
                    if (validDirections.size() == roomsCount) {
                        break;
                    }
                }
            }
        }

        return validDirections.stream().mapToInt(i -> i).toArray();
    }

    public int getExistingRoomsCount(Vector2 room) {
        Vector2[] neighbors = this.getRoomsAround(room);
        int adjacentRooms = 0;
        for (Vector2 neighbor : neighbors) {
            if (generatedRooms.containsKey(neighbor)) {
                adjacentRooms++;
            }
        }

        return adjacentRooms;
    }

    public Vector2 findTheFarthestRoom(Vector2 start){
        Vector2 result = null;
        float dist = -1;

        for(Map.Entry<Vector2, Room> e : this.generatedRooms.entrySet()) {
            Vector2 roomPosition = e.getKey();
            float d = roomPosition.dst(start);
            if(d > dist){
                dist = d;
                result = roomPosition;
            }
        }

        return result;
    }

    public boolean checkRoomTypeAround(Vector2 position, RoomType roomType){
        for (int i = 0; i < 4; i++) {
            Vector2 neighbor = this.getRoomsAround(position)[i];
            if(generatedRooms.containsKey(neighbor) && generatedRooms.get(neighbor).getRoomType() == roomType){
                return true;
            }
        }

        return false;
    }

    /*public Vector2 getRandomRoom() {
        List<Vector2> rooms = new ArrayList<>(this.generatedRooms.keySet());
        Vector2 r = null;

        while (r == null) {
            Vector2 randomRoom = rooms.get(this.random.nextInt(rooms.size()));
            Vector2[] neighbors = this.getRoomsAround(randomRoom);

            int emptyDirections = 0;
            Vector2 potentialRoom = null;

            for (int i = 0; i < neighbors.length; i++) {
                Vector2 neighbor = neighbors[i];
                if (!this.generatedRooms.containsKey(neighbor)) {
                    emptyDirections++;
                    potentialRoom = neighbor;
                }
            }

            if (emptyDirections == 3) {
                r = potentialRoom;
            }
        }

        return r;
    }*/

    public Vector2 getRandomRoom() {
        List<Vector2> rooms = new ArrayList<>(this.generatedRooms.keySet());
        Vector2 result = null;

        while (result == null) {
            Vector2 randomRoom = rooms.get(this.random.nextInt(rooms.size()));
            int emptyDirections = this.getExistingRoomsCount(randomRoom);

            if (emptyDirections == 1) {
                result = randomRoom;
            }
        }

        return result;
    }

    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer){
        for(Map.Entry<Vector2, Room> e : this.generatedRooms.entrySet()) {
            Vector2 roomPosition = e.getKey();
            Room room = e.getValue();

            room.renderPixel(batch, shapeRenderer, (int) roomPosition.x, (int) roomPosition.y);
        }
    }
}

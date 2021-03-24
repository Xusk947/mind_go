package mind_go.stage.lobby;

import arc.graphics.Color;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.gen.Call;

public class Room {
    public final int x, y, endX, endY;
    public final float drawx, drawy, endDrawx, endDrawy;
    public static final int ROOM_SIZE = 8;
    
    public Room(int x, int y) {
        this.x = x;
        this.y = y;
        this.endX = x + ROOM_SIZE;
        this.endY = y + ROOM_SIZE;
        this.drawx = x * Vars.tilesize;
        this.drawy = y * Vars.tilesize;
        this.endDrawx = drawx + ROOM_SIZE * Vars.tilesize;
        this.endDrawy = drawy + ROOM_SIZE * Vars.tilesize;
    }
    
    public void draw() {
        for (int xx = this.x; xx <= endX; xx++) {
            for (int yy = this.y; yy <= endY; yy++) {
                if (xx == x || xx == endX || yy  == y || yy == endY) {
                    Call.effect(Fx.smoke, xx * Vars.tilesize, yy * Vars.tilesize, 0, Color.white);
                }
            }
        }
    }
}

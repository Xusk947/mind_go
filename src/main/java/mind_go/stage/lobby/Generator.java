package mind_go.stage.lobby;

import arc.func.Cons;
import arc.struct.Seq;
import mindustry.content.Blocks;
import mindustry.game.Team;
import mindustry.world.Tile;
import mindustry.world.Tiles;
import mindustry.world.blocks.environment.Floor;

public class Generator implements Cons<Tiles>{
    
    Seq<Room> rooms;
    
    public Generator(Seq<Room> rooms) {
        this.rooms = rooms;
    }
    
    @Override
    public void get(Tiles tiles) {
        for (int x = 0; x < tiles.width; x++) {
            for (int y = 0; y < tiles.height; y++) {
                tiles.set(x, y, new Tile(x, y, Blocks.space, Blocks.air, Blocks.air));
            }
        }
        
        tiles.getn(tiles.width / 2, tiles.height / 2).setBlock(Blocks.coreShard, Team.sharded, 0);
        
        for (Room room : rooms) {
            for (int x = room.x; x <= room.endX; x++) {
                for (int y = room.y; y <= room.endY; y++) {
                    if (x == room.x || x == room.endX || y == room.y || y == room.endY) {
                        tiles.getc(x, y).setFloor((Floor) Blocks.metalFloor2);
                    } else  {
                        tiles.getc(x, y).setFloor((Floor) Blocks.stone);
                    }
                }
            }
        }
    }

}

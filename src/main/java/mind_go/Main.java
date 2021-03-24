package mind_go;

import arc.Events;
import arc.struct.IntMap;
import arc.util.Log;
import mind_go.stage.Game;
import mind_go.stage.Lobby;
import mind_go.stage.Stage;
import mind_go.stage.lobby.Room;
import mind_go.type.UnitData;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.gen.Groups;
import mindustry.gen.Unit;
import mindustry.mod.Plugin;

public class Main extends Plugin {
    public static boolean DEBUG = false;
    
    IntMap<UnitData> units;
    Lobby lobby;
    Game game;
    
    @Override
    public void init() {
        load();
        
        Events.run(EventType.Trigger.update, () -> {
            for (Unit unit : Groups.unit) {
                if (!(units.containsKey(unit.id))) {
                    units.put(unit.id, new UnitData(unit));
                } else {
                    UnitData unitData = units.get(unit.id);
                    unitData.update();
                }
            }
            Stage.updateStage(lobby, game);
        });
        
        Events.on(EventType.UnitDestroyEvent.class, event -> {
            if (units.containsKey(event.unit.id)) {
                units.remove(event.unit.id);
                debug("UnitData removed: " + event.unit.type + ":" + event.unit.id);
            }
        });
        
        Events.on(EventType.ServerLoadEvent.class, event -> {
            Vars.netServer.openServer();
        });
    }
    
    public static void debug(Object obj) {
        if (DEBUG) Log.info(obj);
    }
    
    public void load() {
        units = new IntMap<>();
        lobby = new Lobby();
        
        lobby.rooms.addAll(new Room[]{
            new Room(2, 2),
        });
        game = new Game();
    }
}

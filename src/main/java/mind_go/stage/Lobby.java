package mind_go.stage;

import arc.struct.Seq;
import arc.struct.StringMap;
import mind_go.stage.lobby.Generator;
import mind_go.stage.lobby.Room;
import mindustry.Vars;
import mindustry.gen.Call;
import mindustry.maps.Map;

public class Lobby extends Stage {

    public Seq<Room> rooms;

    public Lobby() {
        super(Type.Lobby);
        rooms = new Seq<>();
    }

    @Override
    public void update() {
        playTime++;

        if (interval.get(0, secTimer)) {
            StringBuilder hudText = new StringBuilder();

            hudText.append("time to end: ");
            if (endTime - playTime > 0) {
                hudText.append((int) ((endTime - playTime) / 60f));
            } else {
                hudText.append(0);
            }
            
            Call.setHudText(hudText.toString());
        }
    }

    @Override
    public void transfer() {
        Vars.world.loadGenerator(75, 75, new Generator(rooms));
        Vars.state.map = new Map(StringMap.of("[white]MS:[gray]GO", "[red]Lob[orange]by"));
    }

    @Override
    public void timeOut() {
        nextStage = Type.Game;
    }
}

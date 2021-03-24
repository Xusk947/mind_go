package mind_go.stage;

import arc.util.Timer;
import mindustry.Vars;
import mindustry.gen.Call;

public class Game extends Stage {
    
    public Game() {
        super(Type.Game);
    }

    @Override
    public void update() {
        playTime++;
        
        if (interval.get(1, secTimer)) {
            StringBuilder hudText = new StringBuilder();
            
            hudText.append("time to end: ");
            if (endTime - playTime > 0) {
                hudText.append((int)((endTime - playTime) / 60f));
            } else {
                hudText.append(0);
            }
            
            Call.setHudText(hudText.toString());
        }
    }

    @Override
    public void transfer() {
        Vars.world.loadGenerator(50, 50, tiles -> {tiles.fill();});
    }

    @Override
    public void timeOut() {
        Call.infoMessage("Game Over");
        Timer.schedule(() -> {
            nextStage = Type.Lobby;
        }, 3);
    }
    
}

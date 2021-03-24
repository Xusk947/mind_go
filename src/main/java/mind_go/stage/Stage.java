package mind_go.stage;

import arc.struct.Seq;
import arc.util.Interval;
import arc.util.Log;
import arc.util.Timer;
import mind_go.Main;
import mindustry.Vars;
import mindustry.game.Rules;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Player;

public abstract class Stage implements StageComp {

    public static Type currentStage;

    public boolean loaded = false;
    public float playTime = 0f,
            endTime = 60f * 10f,
            secTimer = 60f;

    public Interval interval;
    public Type type, nextStage;
    public Rules rules;

    public static enum Type {
        Game, Lobby
    }

    public Stage(Type type) {
        this.interval = new Interval(3);
        this.type = type;
        this.rules = new Rules();
        this.rules.canGameOver = false;
    }

    public void go() {
        loaded = false;
        playTime = 0f;
        Seq<Player> players = new Seq<>();
        Groups.player.copy(players);

        Vars.logic.reset();
        Call.worldDataBegin();

        transfer();
        goEnd(players);
    }

    public void goEnd(Seq<Player> players) {
        Vars.state.rules = rules.copy();
        Vars.logic.play();

        for (Player player : players) {
            Vars.netServer.sendWorldData(player);
        }
        Timer.schedule(() -> {
            timeOut();
        }, endTime / 60f);
        loaded = true;
        nextStage = null;
    }

    public static void updateStage(Stage... stages) {
        if (currentStage == null) {
            for (Stage stage : stages) {
                if (stage.type == Type.Lobby) {
                    stage.go();
                    currentStage = stage.type;
                }
            }
        }
        for (Stage stage : stages) {
            if (stage.type == currentStage && stage.loaded) {
                stage.update();
                if (stage.nextStage != null) {
                    for (Stage stage1 : stages) {
                        if (stage1.type == stage.nextStage) {
                            currentStage = stage1.type;
                            stage1.go();
                            Log.info(stage1.type);
                        }
                    }
                    stage.nextStage = null;
                }
            }
        }
    }
}

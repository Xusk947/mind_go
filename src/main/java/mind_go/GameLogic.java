/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mind_go;

import arc.util.Log;
import mindustry.game.Team;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Mechc;
import mindustry.gen.Nulls;
import mindustry.gen.Player;
import mindustry.gen.Unit;

import static mind_go.Main.bundle;

/**
 *
 * @author Xusk
 */
public class GameLogic {

    public static int gameOverTimer = 300,
            teamID = 1,
            timer = 0;
    public static Team winnerTeam = Team.derelict;
    public static boolean once = false,
            unitSpawned = false,
            gameOver = false;

    public static void update() {
        if (unitSpawned) {
            if (gameOver == true) /* Timer for non instant translate to the lobby */ {
                timer++;
                if (once) /* show message with winner team */ {
                    gameOver(winnerTeam);
                    once = false;
                }

                if (timer > gameOverTimer && !Lobby.inLobby) /* go to lobby when time out */ {
                    timer = 0;
                    gameOver = false;
                    unitSpawned = false;
                    if (Main.debug) {
                        Log.info(bundle.get("debug.logic"));
                    }
                    Lobby.go();
                }
            } else /* set when not gameOver */ {
                once = true;
            }
            if (Groups.unit.size() > 0) {
                boolean end = true;
                Team lastTeam = Groups.unit.index(0).team;
                for (Unit unit : Groups.unit) {
                    Team team = unit.team;
                    if (unit.isFlying()) {
                        unit.damagePierce(unit.maxHealth / 3333 / (unit instanceof Mechc ? 4f : 2f));
                    }
                    if (team != lastTeam) {
                        end = false;
                    }
                    lastTeam = unit.team;
                }

                if (end) {
                    winnerTeam = lastTeam;
                    gameOver = true;
                }
            } else if(Groups.player.size() > 0) /* Team Counter */ {
                winnerTeam = Team.derelict;
                gameOver = true;
            }
        }

    }

    public static void gameOver(Team team) {
        String text = bundle.get("game.win");
        if (team == Team.sharded) {
            text += bundle.get("game.team.sharded");
        } else if (team == Team.blue) {
            text += bundle.get("game.team.blue");
        } else /* no Winner Team*/ {
            text = bundle.get("game.nowin");
        }

        // DEBUG
        if (Main.debug) {
            System.out.println(text);
        }

        Call.infoMessage(text);
    }

    /**
     *
     * @param sx sharded core x
     * @param sy sharded core y
     * @param bx blue core x
     * @param by blue core y
     */
    public static void start(float sx, float sy, float bx, float by) {
        for (Player player : Groups.player) {
            player.unit(Nulls.unit);
            // Get Data From Hash Map
            PlayerData data = Main.data.get(player);
            // Team Changer 
            teamID = -teamID;
            // Pick Team
            Team team = teamID > 0 ? Team.sharded : Team.blue;
            // Create Unit
            Unit unit = Type.get(data.unit).create(team);
            // Set Unit to core position
            unit.set(unit.team() == Team.sharded ? sx : bx, unit.team() == Team.sharded ? sy : by);
            player.team(team);
            // Add Unit
            unit.add();
            // Set Unit To The Player
            data.unita = unit;
            player.unit(unit);
        }
        unitSpawned = true;
    }
}

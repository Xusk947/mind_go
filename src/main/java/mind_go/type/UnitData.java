package mind_go.type;

import arc.math.Angles;
import arc.struct.Seq;
import mindustry.entities.units.WeaponMount;
import mindustry.gen.Unit;
import mindustry.type.Weapon;

public class UnitData {
    public final Unit unit;
    public Seq<WeaponMount> mounts;
    
    public UnitData(Unit unit) {
        this.unit = unit;
    }
    
    public void update() {
        if (hasWeapons() && unit.isShooting()) {
            for (WeaponMount mount : mounts) {
                if (mount.reload < 0) {
                    mount.reload = mount.weapon.reload;
                    mount.weapon.bullet.createNet(unit.team, unit.x + Angles.trnsx(unit.rotation, mount.weapon.x, mount.weapon.y), unit.y + Angles.trnsy(unit.rotation, mount.weapon.x, mount.weapon.y), unit.rotation, mount.weapon.bullet.damage, mount.weapon.bullet.speed, mount.weapon.bullet.lifetime);
                } else {
                    mount.reload--;
                }
            }
        }
    }
    
    public void addMount(Weapon weapon) {
        mounts.add(new WeaponMount(weapon));
    }
    
    public boolean hasWeapons() {
        return mounts.size > 0;
    }
}

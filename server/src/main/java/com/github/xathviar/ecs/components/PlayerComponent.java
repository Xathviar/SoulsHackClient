package com.github.xathviar.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import lombok.Data;

@Data
public class PlayerComponent implements Component, Pool.Poolable {
    public int maxMagicka;
    public int magicka;
    public int hp;
    public int maxHp;
    public int stamina;
    public int maxStamina;
    public int wp;
    public int sp;
    public int hpReg;
    public int magReg;
    public int stamReg;
    public int xp;
    public int level;
    public float spellCrit;
    public float weaponCrit;
    public int damageResistance;
    public int spellResistance;
    public int critResistance;
    // Resistance wird zu Mitigation wenn (Resistance / 33000) * 0.5 rechnet.
    // Bsp. : 15000 damage Resistance; Angriff würd  12k Damage machen.
    // (15 / 33) * 0.5 = 0.227 => 22.7% vom Angriff werden mitigated
    // => 12k * (1-ans) = 9276 hp verliert man.

    @Override
    public void reset() {
        maxMagicka = magicka = hp = maxHp = stamina = maxStamina = wp = sp = hpReg = magReg = stamReg = xp =
                level = damageResistance = spellResistance = critResistance = 0;
        spellCrit = weaponCrit = 0;
    }
}

package com.github.xathviar.ecs;

import com.github.xathviar.ecs.components.PlayerComponent;

public class ComponentHelperClass {

    public static void createPlayer(PlayerComponent component) {
        component.maxMagicka = 20115;
        component.maxStamina = 20115;
        component.maxHp = 22041;
        component.hp = component.maxHp;
        component.stamina = component.maxStamina;
        component.magicka = component.maxMagicka;
        component.magReg = 1640;
        component.stamReg = 1640;
        component.hpReg = 543;
        component.sp = 1000;
        component.wp = 1000;
        component.spellCrit = 0.1f;
        component.weaponCrit = 0.1f;
        component.damageResistance = 0;
        component.spellResistance = 0;
        component.critResistance = 0;
    }
}

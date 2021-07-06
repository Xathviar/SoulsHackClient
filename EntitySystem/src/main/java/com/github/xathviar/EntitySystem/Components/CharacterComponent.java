package com.github.xathviar.EntitySystem.Components;

import lombok.Data;

@Data
public class CharacterComponent extends NamedComponent {
    private String className;
    private int hp;
    private int maxHp;
    private int hpRegen;
    private int mana;
    private int maxMana;
    private int stamina;
    private int magRegen;
    private int stamRegen;
    private int maxStamina;
    private int weaponDamage;
    private int spellDamage;
    private int physicalResistance;
    private int magicalResistance;
    private int spellCrit;
    private int weaponCrit;

    public CharacterComponent(String uuid) {
        super(uuid);
    }
    public CharacterComponent() {
        super();
    }
}

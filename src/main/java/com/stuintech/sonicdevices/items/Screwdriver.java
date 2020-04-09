package com.stuintech.sonicdevices.items;

import com.stuintech.sonicdevices.actions.*;
import com.stuintech.sonicdevices.extensions.Wrenchable;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/*
 * Created by Stuart Irwin on 4/4/2019.
 */

public class Screwdriver extends Device {
    public Screwdriver(boolean hidden) { this(hidden, 3); }

    //Actual constructor
    public Screwdriver(boolean hidden, int maxLevel) {
        super(hidden, maxLevel);
        actions[1].add(new ActivateAction(false));
        actions[2].add(new ActivateAction(true));
        actions[3].add(new RotateAction());
        actions[3].add(new Wrenchable());
    }
}

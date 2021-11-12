package com.stuintech.sonicdevices.item;

import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class SyncedItemData<T> {
    private static final Random random = new Random();
    private final Map<Integer, T> store = new HashMap<>();
    private final String key;
    private final T fallback;

    public SyncedItemData(String key, T fallback) {
        this.key = key;
        this.fallback = fallback;
    }

    public void setData(ItemStack stack, T data) {
        int id;
        if(stack.getNbt() == null || !stack.getNbt().contains(key)) {
            do {
                id = random.nextInt();
            } while(store.containsKey(id));

            stack.getOrCreateNbt().putInt(key, id);
        } else {
            id = stack.getNbt().getInt(key);
        }

        store.put(id, data);
    }

    public void clear(ItemStack stack) {
        setData(stack, fallback);
    }

    public T getData(ItemStack stack) {
        if(stack.getNbt() == null || !stack.getNbt().contains(key))
            return fallback;
        return store.getOrDefault(stack.getNbt().getInt(key), fallback);
    }
}

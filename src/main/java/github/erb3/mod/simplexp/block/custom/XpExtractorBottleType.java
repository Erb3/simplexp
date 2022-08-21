package github.erb3.mod.simplexp.block.custom;

import github.erb3.mod.simplexp.item.ModItems;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.StringIdentifiable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public enum XpExtractorBottleType implements StringIdentifiable {
    NONE("none", Items.AIR, Items.AIR, 0),
    SMALL("small", ModItems.SMALl_BOTTLE, ModItems.BASIC_XP_BOTTLE, 55),
    MEDIUM("medium", ModItems.MEDIUM_BOTTLE, ModItems.BETTER_XP_BOTTLE, 550),
    LARGE("large", ModItems.LARGE_BOTTLE, ModItems.BEST_XP_BOTTLE, 2920);

    private final String name;
    private final Item emptyItem;
    private final Item fullItem;
    private final int experience;

    public static final Set<Item> EMPTY_BOTTLES = new HashSet<>();
    public static final Map<Item, XpExtractorBottleType> TYPES = new HashMap<>();

    XpExtractorBottleType(String name, Item emptyItem, Item fullItem, int experience) {
        this.name = name;
        this.emptyItem = emptyItem;
        this.fullItem = fullItem;
        this.experience = experience;
    }

    static {
        for (XpExtractorBottleType value : values()) {
            EMPTY_BOTTLES.add(value.emptyItem);
            TYPES.put(value.emptyItem, value);
            TYPES.put(value.fullItem, value);
        }
    }

    public String toString() {
        return this.name;
    }

    @Override
    public String asString() {
        return this.name;
    }

    public Item getEmptyItem() {
        return emptyItem;
    }

    public Item getFullItem() {
        return fullItem;
    }

    public int getExperience() {
        return experience;
    }
}

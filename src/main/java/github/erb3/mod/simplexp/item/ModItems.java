package github.erb3.mod.simplexp.item;

import github.erb3.mod.simplexp.SimpleXP;
import github.erb3.mod.simplexp.item.custom.XpBottleItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;

public class ModItems {
    private static final HashMap<String, Item> ITEMS = new LinkedHashMap<>();
    public static final Item BASIC_XP_BOTTLE = add("basic_xp_bottle",
            new XpBottleItem(55, PotionUtil.getColor(Potions.WATER),
                    new FabricItemSettings().group(ModItemGroup.SIMPLE_XP)));
    public static final Item BETTER_XP_BOTTLE = add("better_xp_bottle",
            new XpBottleItem(550, PotionUtil.getColor(Potions.FIRE_RESISTANCE),
                    new FabricItemSettings().group(ModItemGroup.SIMPLE_XP)));
    public static final Item BEST_XP_BOTTLE = add("best_xp_bottle",
            new XpBottleItem(2920, PotionUtil.getColor(Potions.LEAPING),
                    new FabricItemSettings().group(ModItemGroup.SIMPLE_XP)));

    public static final Item SMALl_BOTTLE = add("small_bottle",
            new Item(new FabricItemSettings().group(ModItemGroup.SIMPLE_XP)));
    public static final Item MEDIUM_BOTTLE = add("medium_bottle",
            new Item(new FabricItemSettings().group(ModItemGroup.SIMPLE_XP)));
    public static final Item LARGE_BOTTLE = add("large_bottle",
            new Item(new FabricItemSettings().group(ModItemGroup.SIMPLE_XP)));

    public static final Set<Item> BOTTLES = Set.of(SMALl_BOTTLE, MEDIUM_BOTTLE, LARGE_BOTTLE);

    private static <T extends Item> T add(String name, T item) {
        ITEMS.put(name, item);
        return item;
    }

    public static void register() {
        ITEMS.forEach((name, item) -> Registry.register(Registry.ITEM, new Identifier(SimpleXP.MOD_ID, name), item));
    }
}

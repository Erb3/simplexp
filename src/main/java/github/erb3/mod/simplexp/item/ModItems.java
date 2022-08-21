package github.erb3.mod.simplexp.item;

import github.erb3.mod.simplexp.SimpleXP;
import github.erb3.mod.simplexp.item.custom.xpBottle;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModItems {
    public static final Item BASIC_XP_BOTTLE = registerItem("basic_xp_bottle",
            new xpBottle(55, PotionUtil.getColor(Potions.WATER),
                    new FabricItemSettings().group(ModItemGroup.SIMPLE_XP)));
    public static final Item BETTER_XP_BOTTLE = registerItem("better_xp_bottle",
            new xpBottle(550, PotionUtil.getColor(Potions.FIRE_RESISTANCE),
                    new FabricItemSettings().group(ModItemGroup.SIMPLE_XP)));
    public static final Item BEST_XP_BOTTLE = registerItem("best_xp_bottle",
            new xpBottle(2920, PotionUtil.getColor(Potions.LEAPING),
                    new FabricItemSettings().group(ModItemGroup.SIMPLE_XP)));

    public static final Item SMALl_BOTTLE = registerItem("small_bottle",
            new Item(new FabricItemSettings().group(ModItemGroup.SIMPLE_XP)));
    public static final Item MEDIUM_BOTTLE = registerItem("medium_bottle",
            new Item(new FabricItemSettings().group(ModItemGroup.SIMPLE_XP)));
    public static final Item LARGE_BOTTLE = registerItem("large_bottle",
            new Item(new FabricItemSettings().group(ModItemGroup.SIMPLE_XP)));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(SimpleXP.MOD_ID, name), item);
    }
    public static void registerModItems() {
        SimpleXP.LOGGER.debug("Registering Mod Items for " + SimpleXP.MOD_ID);
    }
}

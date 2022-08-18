package github.erb3.mod.simplexp.item;

import github.erb3.mod.simplexp.SimpleXP;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class ModItemGroup {
    public static final ItemGroup SIMPLE_XP = FabricItemGroupBuilder.build(
            new Identifier(SimpleXP.MOD_ID, "simple_xp"),
            () -> new ItemStack(ModItems.BEST_XP_BOTTLE));
}

package github.erb3.mod.simplexp.block;

import github.erb3.mod.simplexp.SimpleXP;
import github.erb3.mod.simplexp.block.custom.XpExtractorBlock;
import github.erb3.mod.simplexp.item.ModItemGroup;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class ModBlocks {
    public static final HashMap<String, Block> BLOCKS = new LinkedHashMap<>();
    public static final HashMap<String, Item> ITEMS = new LinkedHashMap<>();

    public static final Block XP_EXTRACTOR = add("xp_extractor",
            new XpExtractorBlock(FabricBlockSettings.of(Material.METAL)
                    .strength(3.5f)
                    .requiresTool()
                    .nonOpaque()), ModItemGroup.SIMPLE_XP);

    private static <T extends Block> T add(String name, T block, ItemGroup tab) {
        BLOCKS.put(name, block);
        ITEMS.put(name, new BlockItem(block, new FabricItemSettings().group(tab)));
        return block;
    }

    public static void register() {
        BLOCKS.forEach((name, block) -> Registry.register(Registry.BLOCK, new Identifier(SimpleXP.MOD_ID, name), block));
        ITEMS.forEach((name, item) -> Registry.register(Registry.ITEM, new Identifier(SimpleXP.MOD_ID, name), item));
    }
}

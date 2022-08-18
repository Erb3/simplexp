package github.erb3.mod.simplexp.block;

import github.erb3.mod.simplexp.SimpleXP;
import github.erb3.mod.simplexp.block.custom.XPExtractor;
import github.erb3.mod.simplexp.blockentity.XPExtractorBlockEntity;
import github.erb3.mod.simplexp.item.ModItemGroup;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModBlocks {

    public static final Block XP_EXTRACTOR = registerBlock("xp_extractor",
            new XPExtractor(FabricBlockSettings.of(Material.METAL).strength(3.5f).requiresTool().nonOpaque()), ModItemGroup.SIMPLE_XP);

    private static Block registerBlock(String name, Block block, ItemGroup tab) {
        registerBlockItem(name, block, tab);
        return Registry.register(Registry.BLOCK, new Identifier(SimpleXP.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, Block block, ItemGroup tab) {
        Registry.register(Registry.ITEM, new Identifier(SimpleXP.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings().group(tab)));
    }

    public static void registerModBlocks() {
        SimpleXP.LOGGER.debug("Registering ModBlocks for " + SimpleXP.MOD_ID);
    }
}

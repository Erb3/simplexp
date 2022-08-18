package github.erb3.mod.simplexp.blockentity;

import github.erb3.mod.simplexp.SimpleXP;
import github.erb3.mod.simplexp.block.ModBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class BlockEntites {

    public static BlockEntityType<XPExtractorBlockEntity> XP_EXTRACTOR_BE;

    public static void registerBlockEntities() {
        SimpleXP.LOGGER.debug("Registering Block Entities for " + SimpleXP.MOD_ID);

        XP_EXTRACTOR_BE = Registry.register(Registry.BLOCK_ENTITY_TYPE,
                new Identifier(SimpleXP.MOD_ID, "xp_extractor_blockentity"),
                FabricBlockEntityTypeBuilder.create(XPExtractorBlockEntity::new, ModBlocks.XP_EXTRACTOR).build());
    }
}

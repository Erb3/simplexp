package github.erb3.mod.simplexp;

import github.erb3.mod.simplexp.block.ModBlocks;
import github.erb3.mod.simplexp.blockentity.BlockEntites;
import github.erb3.mod.simplexp.item.ModItems;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleXP implements ModInitializer {
	public static final String MOD_ID = "simplexp";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Hello Minecraft world! This is SimpleXP here!");

		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
		BlockEntites.registerBlockEntities();
	}
}

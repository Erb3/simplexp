package github.erb3.mod.simplexp.block.custom;

import net.minecraft.util.StringIdentifiable;

public enum XPExtractorBottleType implements StringIdentifiable {
    SMALL("small"),
    SMALL_FILLED("small_f"),
    MEDIUM("medium"),
    NONE("none"),
    LARGE("large");

    private final String name;
    XPExtractorBottleType(String name) {
        this.name = name;
    }


    public String toString() {
        return this.name;
    }
    @Override
    public String asString() {
        return this.name;
    }
}

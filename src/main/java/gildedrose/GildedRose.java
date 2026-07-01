package gildedrose;

import java.util.Arrays;

public class GildedRose {
    private final Item[] items;

    public GildedRose(Item[] items) {
        this.items = Arrays.stream(items)
                .map(GildedRose::createSpecializedItem)
                .toArray(Item[]::new);
    }

    public Item[] getItems() {
        return items;
    }

    private static Item createSpecializedItem(Item item) {
        if (item.getClass() != Item.class) {
            return item;
        }
        return Item.create(item.getName(), item.getSellIn(), item.getQuality());
    }

    public void updateQuality() {
        for (Item item : items) {
            item.updateQuality();
        }
    }
}
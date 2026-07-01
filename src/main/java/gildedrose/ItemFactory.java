package gildedrose;

import gildedrose.itemtypes.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class ItemFactory {
    
    private static final String AGED_BRIE = "Aged Brie";
    private static final String BACKSTAGE_PASSES = "Backstage passes to a TAFKAL80ETC concert";
    private static final String SULFURAS = "Sulfuras, Hand of Ragnaros";
    private static final String CONJURED_PREFIX = "Conjured";

    private static final Map<String, BiFunction<Integer, Integer, Item>> ITEM_CREATORS = createItemCreators();

    private static Map<String, BiFunction<Integer, Integer, Item>> createItemCreators() {
        Map<String, BiFunction<Integer, Integer, Item>> creators = new HashMap<>();
        creators.put(AGED_BRIE, AgedBrie::new);
        creators.put(BACKSTAGE_PASSES, BackstagePass::new);
        creators.put(SULFURAS, Sulfuras::new);
        return creators;
    }

    public static Item createItem(String name, int sellIn, int quality) {
        if (name != null && name.startsWith(CONJURED_PREFIX)) {
            return new Conjured(name, sellIn, quality);
        }
        return ITEM_CREATORS
                .getOrDefault(name, (s, q) -> new Item(name, s, q))
                .apply(sellIn, quality);
    }
}

package gildedrose;

import gildedrose.itemtypes.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class ItemFactoryTest {

    @Test
    @DisplayName("Factory creates AgedBrie for 'Aged Brie' name")
    void factoryCreatesAgedBrieForAgedBrieName() {
        Item item = ItemFactory.createItem("Aged Brie", 10, 20);
        assertInstanceOf(AgedBrie.class, item);
        assertEquals("Aged Brie", item.getName());
        assertEquals(10, item.getSellIn());
        assertEquals(20, item.getQuality());
    }

    @Test
    @DisplayName("Factory creates BackstagePass for backstage passes name")
    void factoryCreatesBackstagePassForBackstagePassesName() {
        Item item = ItemFactory.createItem("Backstage passes to a TAFKAL80ETC concert", 15, 30);
        assertInstanceOf(BackstagePass.class, item);
        assertEquals("Backstage passes to a TAFKAL80ETC concert", item.getName());
        assertEquals(15, item.getSellIn());
        assertEquals(30, item.getQuality());
    }

    @Test
    @DisplayName("Factory creates Sulfuras for 'Sulfuras, Hand of Ragnaros' name")
    void factoryCreatesSulfurasForSulfurasName() {
        Item item = ItemFactory.createItem("Sulfuras, Hand of Ragnaros", 0, 80);
        assertInstanceOf(Sulfuras.class, item);
        assertEquals("Sulfuras, Hand of Ragnaros", item.getName());
        assertEquals(0, item.getSellIn());
        assertEquals(80, item.getQuality());
    }

    @Test
    @DisplayName("Factory creates Conjured for 'Conjured' name")
    void factoryCreatesConjuredForConjuredName() {
        Item item = ItemFactory.createItem("Conjured", 10, 20);
        assertInstanceOf(Conjured.class, item);
        assertEquals("Conjured", item.getName());
        assertEquals(10, item.getSellIn());
        assertEquals(20, item.getQuality());
    }

    @Test
    @DisplayName("Factory creates Conjured for 'Conjured Mana Cake' (prefix match) preserving name")
    void factoryCreatesConjuredForConjuredPrefixVariant() {
        Item item = ItemFactory.createItem("Conjured Mana Cake", 10, 20);
        assertInstanceOf(Conjured.class, item);
        assertEquals("Conjured Mana Cake", item.getName());
        assertEquals(10, item.getSellIn());
        assertEquals(20, item.getQuality());
    }

    @Test
    @DisplayName("Factory creates normal Item for unknown name")
    void factoryCreatesNormalItemForUnknownName() {
        Item item = ItemFactory.createItem("Normal Item", 10, 20);
        assertEquals(Item.class, item.getClass());
        assertFalse(item instanceof AgedBrie);
        assertFalse(item instanceof BackstagePass);
        assertFalse(item instanceof Sulfuras);
        assertFalse(item instanceof Conjured);
        assertEquals("Normal Item", item.getName());
        assertEquals(10, item.getSellIn());
        assertEquals(20, item.getQuality());
    }

    @Test
    @DisplayName("Factory creates normal Item for empty name")
    void factoryCreatesNormalItemForEmptyName() {
        Item item = ItemFactory.createItem("", 5, 10);
        assertEquals(Item.class, item.getClass());
        assertEquals("", item.getName());
        assertEquals(5, item.getSellIn());
        assertEquals(10, item.getQuality());
    }

    @Test
    @DisplayName("Factory creates normal Item for null-like name")
    void factoryCreatesNormalItemForRandomName() {
        Item item = ItemFactory.createItem("Random Item XYZ", 7, 15);
        assertEquals(Item.class, item.getClass());
        assertEquals("Random Item XYZ", item.getName());
        assertEquals(7, item.getSellIn());
        assertEquals(15, item.getQuality());
    }

    @Test
    @DisplayName("Factory handles negative sellIn values")
    void factoryHandlesNegativeSellInValues() {
        Item item = ItemFactory.createItem("Normal Item", -5, 10);
        assertEquals(-5, item.getSellIn());
        assertEquals(10, item.getQuality());
    }

    @Test
    @DisplayName("Factory handles zero quality")
    void factoryHandlesZeroQuality() {
        Item item = ItemFactory.createItem("Aged Brie", 10, 0);
        assertInstanceOf(AgedBrie.class, item);
        assertEquals(0, item.getQuality());
    }

    @Test
    @DisplayName("Factory handles quality above 50 for Sulfuras")
    void factoryHandlesQualityAbove50ForSulfuras() {
        Item item = ItemFactory.createItem("Sulfuras, Hand of Ragnaros", 10, 80);
        assertInstanceOf(Sulfuras.class, item);
        assertEquals(80, item.getQuality());
    }

    @Test
    @DisplayName("Factory case sensitive for item names")
    void factoryCaseSensitiveForItemNames() {
        Item item = ItemFactory.createItem("aged brie", 10, 20);
        assertEquals(Item.class, item.getClass());
        assertFalse(item instanceof AgedBrie);
    }

    @Test
    @DisplayName("Factory creates items with boundary values")
    void factoryCreatesItemsWithBoundaryValues() {
        Item item1 = ItemFactory.createItem("Backstage passes to a TAFKAL80ETC concert", 0, 50);
        assertInstanceOf(BackstagePass.class, item1);
        assertEquals(0, item1.getSellIn());
        assertEquals(50, item1.getQuality());
        
        Item item2 = ItemFactory.createItem("Normal Item", 100, 0);
        assertEquals(100, item2.getSellIn());
        assertEquals(0, item2.getQuality());
    }
}

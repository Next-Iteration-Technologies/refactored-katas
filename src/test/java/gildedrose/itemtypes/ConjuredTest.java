package gildedrose.itemtypes;

import gildedrose.Item;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class ConjuredTest {

    @Test
    @DisplayName("Conjured item quality degrades by 2 before sell date")
    void conjuredItemQualityDegradesByTwoBeforeSellDate() {
        Conjured item = new Conjured(10, 20);
        item.updateQuality();

        assertEquals(9, item.getSellIn());
        assertEquals(18, item.getQuality());
    }

    @Test
    @DisplayName("Conjured item quality degrades by 4 after sell date")
    void conjuredItemQualityDegradesByFourAfterSellDate() {
        Conjured item = new Conjured(0, 20);
        item.updateQuality();

        assertEquals(-1, item.getSellIn());
        assertEquals(16, item.getQuality());
    }

    @Test
    @DisplayName("Conjured item quality never goes negative")
    void conjuredItemQualityNeverNegative() {
        Conjured item = new Conjured(5, 0);
        item.updateQuality();

        assertEquals(4, item.getSellIn());
        assertEquals(0, item.getQuality());
    }

    @Test
    @DisplayName("Conjured item with quality 1 before sell date becomes 0")
    void conjuredItemWithQuality1BeforeSellDateBecomesZero() {
        Conjured item = new Conjured(5, 1);
        item.updateQuality();

        assertEquals(4, item.getSellIn());
        assertEquals(0, item.getQuality());
    }

    @Test
    @DisplayName("Conjured item with quality 3 after sell date becomes 0")
    void conjuredItemWithQuality3AfterSellDateBecomesZero() {
        Conjured item = new Conjured(-1, 3);
        item.updateQuality();

        assertEquals(-2, item.getSellIn());
        assertEquals(0, item.getQuality());
    }

    @Test
    @DisplayName("Conjured item degrades twice as fast as normal item")
    void conjuredItemDegradesTwiceAsFastAsNormalItem() {
        Conjured conjured = new Conjured(10, 20);
        Item normal = new Item("Normal Item", 10, 20);

        conjured.updateQuality();
        normal.updateQuality();

        assertEquals(9, conjured.getSellIn());
        assertEquals(18, conjured.getQuality()); // Conjured: -2
        assertEquals(9, normal.getSellIn());
        assertEquals(19, normal.getQuality()); // Normal: -1
    }

    @Test
    @DisplayName("Conjured with custom name preserves original name")
    void conjuredWithCustomNamePreservesOriginalName() {
        Conjured item = new Conjured("Conjured Mana Cake", 5, 10);
        assertEquals("Conjured Mana Cake", item.getName());
        assertEquals(5, item.getSellIn());
        assertEquals(10, item.getQuality());
    }

    @Test
    @DisplayName("Conjured with custom name degrades by 2 before sell date")
    void conjuredWithCustomNameDegradesByTwo() {
        Conjured item = new Conjured("Conjured Mana Cake", 10, 20);
        item.updateQuality();
        assertEquals("Conjured Mana Cake", item.getName());
        assertEquals(9, item.getSellIn());
        assertEquals(18, item.getQuality());
    }

    @Test
    @DisplayName("Conjured item with quality 1 after sell date becomes 0")
    void conjuredWithQuality1AfterSellDateBecomesZero() {
        Conjured item = new Conjured(-1, 1);
        item.updateQuality();
        assertEquals(-2, item.getSellIn());
        assertEquals(0, item.getQuality());
    }

    @Test
    @DisplayName("Conjured item with multiple updates degrades correctly")
    void conjuredItemMultipleUpdates() {
        Conjured item = new Conjured(5, 20);

        // Day 1: sellIn=5, quality=20 -> sellIn=4, quality=18
        item.updateQuality();
        assertEquals(4, item.getSellIn());
        assertEquals(18, item.getQuality());

        // Day 2: sellIn=4, quality=18 -> sellIn=3, quality=16
        item.updateQuality();
        assertEquals(3, item.getSellIn());
        assertEquals(16, item.getQuality());

        // Day 3: sellIn=3, quality=16 -> sellIn=2, quality=14
        item.updateQuality();
        assertEquals(2, item.getSellIn());
        assertEquals(14, item.getQuality());
    }
}

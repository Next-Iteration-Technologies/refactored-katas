package gildedrose;

import gildedrose.itemtypes.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class GildedRoseTest {

    @Test
    @DisplayName("Normal item quality degrades by 1 before sell date")
    void normalItemQualityDegradesByOneBeforeSellDate() {
        GildedRose app = new GildedRose(new Item[] { new Item("Normal Item", 10, 20) });
        app.updateQuality();

        assertEquals(9, app.getItems()[0].getSellIn());
        assertEquals(19, app.getItems()[0].getQuality());
    }

    @Test
    @DisplayName("Normal item quality degrades by 2 after sell date")
    void normalItemQualityDegradesByTwoAfterSellDate() {
        GildedRose app = new GildedRose(new Item[] { new Item("Normal Item", 0, 20) });
        app.updateQuality();

        assertEquals(-1, app.getItems()[0].getSellIn());
        assertEquals(18, app.getItems()[0].getQuality());
    }

    @Test
    @DisplayName("Normal item quality never goes negative")
    void normalItemQualityNeverNegative() {
        GildedRose app = new GildedRose(new Item[] { new Item("Normal Item", 5, 0) });
        app.updateQuality();

        assertEquals(4, app.getItems()[0].getSellIn());
        assertEquals(0, app.getItems()[0].getQuality());
    }

    @Test
    @DisplayName("Normal item with quality 1 after sell date becomes 0")
    void normalItemWithQuality1AfterSellDateBecomesZero() {
        GildedRose app = new GildedRose(new Item[] { new Item("Normal Item", -1, 1) });
        app.updateQuality();

        assertEquals(-2, app.getItems()[0].getSellIn());
        assertEquals(0, app.getItems()[0].getQuality());
    }

    @Test
    @DisplayName("Aged Brie increases in quality before sell date")
    void agedBrieIncreasesInQualityBeforeSellDate() {
        GildedRose app = new GildedRose(new Item[] { new Item("Aged Brie", 10, 20) });
        app.updateQuality();

        assertEquals(9, app.getItems()[0].getSellIn());
        assertEquals(21, app.getItems()[0].getQuality());
    }

    @Test
    @DisplayName("Aged Brie increases in quality by 2 after sell date")
    void agedBrieIncreasesInQualityByTwoAfterSellDate() {
        GildedRose app = new GildedRose(new Item[] { new Item("Aged Brie", 0, 20) });
        app.updateQuality();

        assertEquals(-1, app.getItems()[0].getSellIn());
        assertEquals(22, app.getItems()[0].getQuality());
    }

    @Test
    @DisplayName("Aged Brie quality never exceeds 50")
    void agedBrieQualityNeverExceeds50() {
        GildedRose app = new GildedRose(new Item[] { new Item("Aged Brie", 10, 50) });
        app.updateQuality();

        assertEquals(9, app.getItems()[0].getSellIn());
        assertEquals(50, app.getItems()[0].getQuality());
    }

    @Test
    @DisplayName("Aged Brie quality caps at 50 after sell date")
    void agedBrieQualityCapsAt50AfterSellDate() {
        GildedRose app = new GildedRose(new Item[] { new Item("Aged Brie", -1, 49) });
        app.updateQuality();

        assertEquals(-2, app.getItems()[0].getSellIn());
        assertEquals(50, app.getItems()[0].getQuality());
    }

    @Test
    @DisplayName("Sulfuras never decreases in quality or sellIn")
    void sulfurasNeverChanges() {
        GildedRose app = new GildedRose(new Item[] { new Item("Sulfuras, Hand of Ragnaros", 10, 80) });
        app.updateQuality();

        assertEquals(10, app.getItems()[0].getSellIn());
        assertEquals(80, app.getItems()[0].getQuality());
    }

    @Test
    @DisplayName("Sulfuras with negative sellIn never changes")
    void sulfurasWithNegativeSellInNeverChanges() {
        GildedRose app = new GildedRose(new Item[] { new Item("Sulfuras, Hand of Ragnaros", -1, 80) });
        app.updateQuality();

        assertEquals(-1, app.getItems()[0].getSellIn());
        assertEquals(80, app.getItems()[0].getQuality());
    }

    @Test
    @DisplayName("Backstage passes increase by 1 when sellIn > 10")
    void backstagePassesIncreaseBy1WhenSellInGreaterThan10() {
        GildedRose app = new GildedRose(new Item[] { new Item("Backstage passes to a TAFKAL80ETC concert", 15, 20) });
        app.updateQuality();

        assertEquals(14, app.getItems()[0].getSellIn());
        assertEquals(21, app.getItems()[0].getQuality());
    }

    @Test
    @DisplayName("Backstage passes increase by 2 when sellIn = 10")
    void backstagePassesIncreaseBy2WhenSellInEquals10() {
        GildedRose app = new GildedRose(new Item[] { new Item("Backstage passes to a TAFKAL80ETC concert", 10, 20) });
        app.updateQuality();

        assertEquals(9, app.getItems()[0].getSellIn());
        assertEquals(22, app.getItems()[0].getQuality());
    }

    @Test
    @DisplayName("Backstage passes increase by 2 when sellIn between 6 and 10")
    void backstagePassesIncreaseBy2WhenSellInBetween6And10() {
        GildedRose app = new GildedRose(new Item[] { new Item("Backstage passes to a TAFKAL80ETC concert", 8, 20) });
        app.updateQuality();

        assertEquals(7, app.getItems()[0].getSellIn());
        assertEquals(22, app.getItems()[0].getQuality());
    }

    @Test
    @DisplayName("Backstage passes increase by 3 when sellIn = 5")
    void backstagePassesIncreaseBy3WhenSellInEquals5() {
        GildedRose app = new GildedRose(new Item[] { new Item("Backstage passes to a TAFKAL80ETC concert", 5, 20) });
        app.updateQuality();

        assertEquals(4, app.getItems()[0].getSellIn());
        assertEquals(23, app.getItems()[0].getQuality());
    }

    @Test
    @DisplayName("Backstage passes increase by 3 when sellIn between 1 and 5")
    void backstagePassesIncreaseBy3WhenSellInBetween1And5() {
        GildedRose app = new GildedRose(new Item[] { new Item("Backstage passes to a TAFKAL80ETC concert", 3, 20) });
        app.updateQuality();

        assertEquals(2, app.getItems()[0].getSellIn());
        assertEquals(23, app.getItems()[0].getQuality());
    }

    @Test
    @DisplayName("Backstage passes quality drops to 0 after concert")
    void backstagePassesQualityDropsToZeroAfterConcert() {
        GildedRose app = new GildedRose(new Item[] { new Item("Backstage passes to a TAFKAL80ETC concert", 0, 20) });
        app.updateQuality();

        assertEquals(-1, app.getItems()[0].getSellIn());
        assertEquals(0, app.getItems()[0].getQuality());
    }

    @Test
    @DisplayName("Backstage passes quality never exceeds 50 with 15 days left")
    void backstagePassesQualityNeverExceeds50With15DaysLeft() {
        GildedRose app = new GildedRose(new Item[] { new Item("Backstage passes to a TAFKAL80ETC concert", 15, 50) });
        app.updateQuality();

        assertEquals(14, app.getItems()[0].getSellIn());
        assertEquals(50, app.getItems()[0].getQuality());
    }

    @Test
    @DisplayName("Backstage passes quality caps at 50 when increasing by 2")
    void backstagePassesQualityCapsAt50WhenIncreasingBy2() {
        GildedRose app = new GildedRose(new Item[] { new Item("Backstage passes to a TAFKAL80ETC concert", 10, 49) });
        app.updateQuality();

        assertEquals(9, app.getItems()[0].getSellIn());
        assertEquals(50, app.getItems()[0].getQuality());
    }

    @Test
    @DisplayName("Backstage passes quality caps at 50 when increasing by 3")
    void backstagePassesQualityCapsAt50WhenIncreasingBy3() {
        GildedRose app = new GildedRose(new Item[] { new Item("Backstage passes to a TAFKAL80ETC concert", 5, 48) });
        app.updateQuality();

        assertEquals(4, app.getItems()[0].getSellIn());
        assertEquals(50, app.getItems()[0].getQuality());
    }

    @Test
    @DisplayName("Multiple items update correctly in single update")
    void multipleItemsUpdateCorrectly() {
        GildedRose app = new GildedRose(new Item[] {
            new Item("Normal Item", 10, 20),
            new Item("Aged Brie", 5, 10),
            new Item("Sulfuras, Hand of Ragnaros", 0, 80),
            new Item("Backstage passes to a TAFKAL80ETC concert", 15, 30)
        });
        app.updateQuality();

        assertEquals(9, app.getItems()[0].getSellIn());
        assertEquals(19, app.getItems()[0].getQuality());

        assertEquals(4, app.getItems()[1].getSellIn());
        assertEquals(11, app.getItems()[1].getQuality());

        assertEquals(0, app.getItems()[2].getSellIn());
        assertEquals(80, app.getItems()[2].getQuality());

        assertEquals(14, app.getItems()[3].getSellIn());
        assertEquals(31, app.getItems()[3].getQuality());
    }

    @Test
    @DisplayName("Multiple updates simulate multiple days")
    void multipleUpdatesSimulateMultipleDays() {
        GildedRose app = new GildedRose(new Item[] { new Item("Normal Item", 5, 10) });

        for (int i = 0; i < 7; i++) {
            app.updateQuality();
        }

        assertEquals(-2, app.getItems()[0].getSellIn());
        assertEquals(1, app.getItems()[0].getQuality()); // 5 days at -1, 2 days at -2
    }

    @Test
    @DisplayName("Backstage passes full lifecycle from 11 days to after concert")
    void backstagePassesFullLifecycle() {
        GildedRose app = new GildedRose(new Item[] { new Item("Backstage passes to a TAFKAL80ETC concert", 11, 10) });

        // Day 1: 11 -> 10 days, quality +1
        app.updateQuality();
        assertEquals(10, app.getItems()[0].getSellIn());
        assertEquals(11, app.getItems()[0].getQuality());

        // Days 2-6: 10 -> 5 days, quality +2 each day
        for (int i = 0; i < 5; i++) {
            app.updateQuality();
        }
        assertEquals(5, app.getItems()[0].getSellIn());
        assertEquals(21, app.getItems()[0].getQuality());

        // Days 7-11: 5 -> 0 days, quality +3 each day
        for (int i = 0; i < 5; i++) {
            app.updateQuality();
        }
        assertEquals(0, app.getItems()[0].getSellIn());
        assertEquals(36, app.getItems()[0].getQuality());

        // Day 12: after concert, quality drops to 0
        app.updateQuality();
        assertEquals(-1, app.getItems()[0].getSellIn());
        assertEquals(0, app.getItems()[0].getQuality());
    }

    @Test
    @DisplayName("Aged Brie full lifecycle over 30 days")
    void agedBrieFullLifecycle() {
        GildedRose app = new GildedRose(new Item[] { new Item("Aged Brie", 10, 0) });

        // 10 days before sell date: +1 per day
        for (int i = 0; i < 10; i++) {
            app.updateQuality();
        }
        assertEquals(0, app.getItems()[0].getSellIn());
        assertEquals(10, app.getItems()[0].getQuality());

        // 20 days after sell date: +2 per day, but capped at 50
        for (int i = 0; i < 20; i++) {
            app.updateQuality();
        }
        assertEquals(-20, app.getItems()[0].getSellIn());
        assertEquals(50, app.getItems()[0].getQuality());
    }

    @Test
    @DisplayName("Normal item with 0 quality after sell date stays at 0")
    void normalItemWithZeroQualityAfterSellDateStaysAtZero() {
        GildedRose app = new GildedRose(new Item[] { new Item("Normal Item", -5, 0) });

        for (int i = 0; i < 5; i++) {
            app.updateQuality();
        }

        assertEquals(-10, app.getItems()[0].getSellIn());
        assertEquals(0, app.getItems()[0].getQuality());
    }

    @Test
    @DisplayName("Edge case: Normal item quality 1 before sell date")
    void normalItemQuality1BeforeSellDate() {
        GildedRose app = new GildedRose(new Item[] { new Item("Normal Item", 5, 1) });
        app.updateQuality();

        assertEquals(4, app.getItems()[0].getSellIn());
        assertEquals(0, app.getItems()[0].getQuality());
    }

    @Test
    @DisplayName("Item.create uses factory pattern")
    void itemCreateUsesFactoryPattern() {
        Item agedBrie = Item.create("Aged Brie", 10, 20);
        assertInstanceOf(AgedBrie.class, agedBrie);

        Item backstagePass = Item.create("Backstage passes to a TAFKAL80ETC concert", 15, 30);
        assertInstanceOf(BackstagePass.class, backstagePass);

        Item sulfuras = Item.create("Sulfuras, Hand of Ragnaros", 0, 80);
        assertInstanceOf(Sulfuras.class, sulfuras);

        Item normalItem = Item.create("Normal Item", 10, 20);
        assertEquals(Item.class, normalItem.getClass());
    }

    @Test
    @DisplayName("GildedRose constructor converts items to polymorphic types")
    void gildedRoseConstructorConvertsItemsToPolymorphicTypes() {
        GildedRose app = new GildedRose(new Item[] {
            new Item("Aged Brie", 10, 20),
            new Item("Backstage passes to a TAFKAL80ETC concert", 15, 30),
            new Item("Normal Item", 5, 10)
        });

        assertInstanceOf(AgedBrie.class, app.getItems()[0]);
        assertInstanceOf(BackstagePass.class, app.getItems()[1]);
        assertEquals(Item.class, app.getItems()[2].getClass());
    }

    @Test
    @DisplayName("Empty item array handled correctly")
    void emptyItemArrayHandledCorrectly() {
        GildedRose app = new GildedRose(new Item[] {});
        app.updateQuality();
        assertEquals(0, app.getItems().length);
    }

    @Test
    @DisplayName("Single item array works correctly")
    void singleItemArrayWorksCorrectly() {
        GildedRose app = new GildedRose(new Item[] { new Item("Normal Item", 5, 10) });
        app.updateQuality();
        assertEquals(4, app.getItems()[0].getSellIn());
        assertEquals(9, app.getItems()[0].getQuality());
    }

    @Test
    @DisplayName("Backstage passes at sellIn 1 increases by 3")
    void backstagePassesAtSellIn1IncreasesBy3() {
        GildedRose app = new GildedRose(new Item[] { new Item("Backstage passes to a TAFKAL80ETC concert", 1, 20) });
        app.updateQuality();
        assertEquals(0, app.getItems()[0].getSellIn());
        assertEquals(23, app.getItems()[0].getQuality());
    }

    @Test
    @DisplayName("Backstage passes at sellIn 6 increases by 2")
    void backstagePassesAtSellIn6IncreasesBy2() {
        GildedRose app = new GildedRose(new Item[] { new Item("Backstage passes to a TAFKAL80ETC concert", 6, 20) });
        app.updateQuality();
        assertEquals(5, app.getItems()[0].getSellIn());
        assertEquals(22, app.getItems()[0].getQuality());
    }

    @Test
    @DisplayName("Backstage passes at sellIn 11 increases by 1")
    void backstagePassesAtSellIn11IncreasesBy1() {
        GildedRose app = new GildedRose(new Item[] { new Item("Backstage passes to a TAFKAL80ETC concert", 11, 20) });
        app.updateQuality();
        assertEquals(10, app.getItems()[0].getSellIn());
        assertEquals(21, app.getItems()[0].getQuality());
    }

    @Test
    @DisplayName("Normal item at sellIn 1 degrades by 1")
    void normalItemAtSellIn1DegradesBy1() {
        GildedRose app = new GildedRose(new Item[] { new Item("Normal Item", 1, 10) });
        app.updateQuality();
        assertEquals(0, app.getItems()[0].getSellIn());
        assertEquals(9, app.getItems()[0].getQuality());
    }

    @Test
    @DisplayName("Aged Brie at sellIn 1 increases by 1")
    void agedBrieAtSellIn1IncreasesBy1() {
        GildedRose app = new GildedRose(new Item[] { new Item("Aged Brie", 1, 20) });
        app.updateQuality();
        assertEquals(0, app.getItems()[0].getSellIn());
        assertEquals(21, app.getItems()[0].getQuality());
    }

    @Test
    @DisplayName("Item toString returns correct format")
    void itemToStringReturnsCorrectFormat() {
        GildedRose app = new GildedRose(new Item[] { new Item("Normal Item", 10, 20) });
        assertEquals("Normal Item, 10, 20", app.getItems()[0].toString());
    }

    @Test
    @DisplayName("Backstage passes with quality 47 at sellIn 5 caps at 50")
    void backstagePassesWithQuality47AtSellIn5CapsAt50() {
        GildedRose app = new GildedRose(new Item[] { new Item("Backstage passes to a TAFKAL80ETC concert", 5, 47) });
        app.updateQuality();
        assertEquals(4, app.getItems()[0].getSellIn());
        assertEquals(50, app.getItems()[0].getQuality());
    }

    @Test
    @DisplayName("Multiple identical items update independently")
    void multipleIdenticalItemsUpdateIndependently() {
        GildedRose app = new GildedRose(new Item[] {
            new Item("Normal Item", 5, 10),
            new Item("Normal Item", 5, 10)
        });
        app.updateQuality();

        assertEquals(4, app.getItems()[0].getSellIn());
        assertEquals(9, app.getItems()[0].getQuality());
        assertEquals(4, app.getItems()[1].getSellIn());
        assertEquals(9, app.getItems()[1].getQuality());
    }

    @Test
    @DisplayName("Conjured item quality degrades by 2 before sell date")
    void conjuredItemQualityDegradesByTwoBeforeSellDate() {
        GildedRose app = new GildedRose(new Item[] { new Item("Conjured", 10, 20) });
        app.updateQuality();

        assertEquals(9, app.getItems()[0].getSellIn());
        assertEquals(18, app.getItems()[0].getQuality());
    }

    @Test
    @DisplayName("Conjured item quality degrades by 4 after sell date")
    void conjuredItemQualityDegradesByFourAfterSellDate() {
        GildedRose app = new GildedRose(new Item[] { new Item("Conjured", 0, 20) });
        app.updateQuality();

        assertEquals(-1, app.getItems()[0].getSellIn());
        assertEquals(16, app.getItems()[0].getQuality());
    }

    @Test
    @DisplayName("Conjured item quality never goes negative")
    void conjuredItemQualityNeverNegative() {
        GildedRose app = new GildedRose(new Item[] { new Item("Conjured", 5, 0) });
        app.updateQuality();

        assertEquals(4, app.getItems()[0].getSellIn());
        assertEquals(0, app.getItems()[0].getQuality());
    }

    @Test
    @DisplayName("Conjured item with quality 1 before sell date becomes 0")
    void conjuredItemWithQuality1BeforeSellDateBecomesZero() {
        GildedRose app = new GildedRose(new Item[] { new Item("Conjured", 5, 1) });
        app.updateQuality();

        assertEquals(4, app.getItems()[0].getSellIn());
        assertEquals(0, app.getItems()[0].getQuality());
    }

    @Test
    @DisplayName("Conjured item with quality 3 after sell date becomes 0")
    void conjuredItemWithQuality3AfterSellDateBecomesZero() {
        GildedRose app = new GildedRose(new Item[] { new Item("Conjured", -1, 3) });
        app.updateQuality();

        assertEquals(-2, app.getItems()[0].getSellIn());
        assertEquals(0, app.getItems()[0].getQuality());
    }

    @Test
    @DisplayName("Conjured item degrades twice as fast as normal item")
    void conjuredItemDegradesTwiceAsFastAsNormalItem() {
        GildedRose app = new GildedRose(new Item[] {
            new Item("Conjured", 10, 20),
            new Item("Normal Item", 10, 20)
        });
        app.updateQuality();

        assertEquals(9, app.getItems()[0].getSellIn());
        assertEquals(18, app.getItems()[0].getQuality()); // Conjured: -2
        assertEquals(9, app.getItems()[1].getSellIn());
        assertEquals(19, app.getItems()[1].getQuality()); // Normal: -1
    }

    @Test
    @DisplayName("Conjured Mana Cake degrades twice as fast as a normal item")
    void conjuredManaCakeDegradesTwiceAsFast() {
        GildedRose app = new GildedRose(new Item[] { new Item("Conjured Mana Cake", 10, 20) });
        app.updateQuality();

        assertInstanceOf(Conjured.class, app.getItems()[0]);
        assertEquals("Conjured Mana Cake", app.getItems()[0].getName());
        assertEquals(9, app.getItems()[0].getSellIn());
        assertEquals(18, app.getItems()[0].getQuality());
    }

    @Test
    @DisplayName("GildedRose does not mutate the caller's original array")
    void gildedRoseDoesNotMutateCallerArray() {
        Item original = new Item("Aged Brie", 10, 20);
        Item[] items = new Item[] { original };

        new GildedRose(items);

        assertSame(original, items[0]);
        assertFalse(items[0] instanceof AgedBrie);
    }

    @Test
    @DisplayName("GildedRose reuses pre-typed items without reconstruction")
    void gildedRoseReusesPreTypedItems() {
        AgedBrie original = new AgedBrie(10, 20);
        GildedRose app = new GildedRose(new Item[] { original });

        assertSame(original, app.getItems()[0]);
    }
}

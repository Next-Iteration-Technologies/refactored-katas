"""Integration test: GildedRose.update_quality() delegates to each item's own update rule.

Per-item business rules are unit-tested in their own test files
(test_item.py, test_aged_brie.py, test_sulfuras.py, test_backstage_passes.py,
test_conjured.py); this file only proves GildedRose iterates and delegates correctly.
"""

from gildedrose.itemtypes.aged_brie import AgedBrie
from gildedrose.itemtypes.backstage_passes import BackstagePasses
from gildedrose.itemtypes.conjured import Conjured
from gildedrose.gilded_rose import GildedRose
from gildedrose.item import Item
from gildedrose.itemtypes.sulfuras import Sulfuras

NORMAL_ITEM = "+5 Dexterity Vest"


class TestGildedRose:
    def test_update_quality_updates_every_item_in_the_list(self):
        items = [
            Item(NORMAL_ITEM, 10, 20),
            Item(AgedBrie.item_name, 2, 0),
            Item(Sulfuras.item_name, 0, 80),
            Item(BackstagePasses.item_name, 15, 20),
            Item(Conjured.item_name, 5, 10),
        ]
        gilded_rose = GildedRose(items)

        gilded_rose.update_quality()

        assert [(item.sell_in, item.quality) for item in items] == [
            (9, 19),
            (1, 1),
            (0, 80),
            (14, 21),
            (4, 8),
        ]

    def test_update_quality_with_empty_item_list_is_a_no_op(self):
        gilded_rose = GildedRose([])

        gilded_rose.update_quality()

        assert gilded_rose.items == []

    def test_update_quality_over_multiple_days_updates_each_item_independently(self):
        items = [
            Item(NORMAL_ITEM, 3, 6),
            Item(AgedBrie.item_name, 1, 48),
            Item(Sulfuras.item_name, 0, 80),
            Item(BackstagePasses.item_name, 12, 20),
            Item(Conjured.item_name, 2, 10),
        ]
        gilded_rose = GildedRose(items)

        for _ in range(3):
            gilded_rose.update_quality()

        assert [(item.sell_in, item.quality) for item in items] == [
            (0, 3),
            (-2, 50),
            (0, 80),
            (9, 24),
            (-1, 2),
        ]

    def test_update_quality_mutates_items_in_place_rather_than_replacing_them(self):
        item = Item(NORMAL_ITEM, 10, 20)
        items = [item]
        gilded_rose = GildedRose(items)

        gilded_rose.update_quality()

        assert gilded_rose.items[0] is item

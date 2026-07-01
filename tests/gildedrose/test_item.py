"""Unit tests for the Item base class: normal-item update rules and the
name-based subclass dispatch performed by Item.__new__.
"""

from gildedrose.aged_brie import AgedBrie
from gildedrose.backstage_passes import BackstagePasses
from gildedrose.conjured import Conjured
from gildedrose.item import Item
from gildedrose.sulfuras import Sulfuras

NORMAL_ITEM = "+5 Dexterity Vest"


class TestSubclassDispatch:
    def test_unregistered_name_creates_a_plain_item(self):
        item = Item(NORMAL_ITEM, 10, 20)

        assert type(item) is Item

    def test_aged_brie_name_creates_an_aged_brie_instance(self):
        item = Item(AgedBrie.item_name, 10, 20)

        assert type(item) is AgedBrie

    def test_sulfuras_name_creates_a_sulfuras_instance(self):
        item = Item(Sulfuras.item_name, 0, 80)

        assert type(item) is Sulfuras

    def test_backstage_passes_name_creates_a_backstage_passes_instance(self):
        item = Item(BackstagePasses.item_name, 10, 20)

        assert type(item) is BackstagePasses

    def test_conjured_name_creates_a_conjured_instance(self):
        item = Item(Conjured.item_name, 10, 20)

        assert type(item) is Conjured


class TestNormalItemUpdate:
    def test_sell_in_and_quality_decrease_by_one_before_sell_date(self):
        item = Item(NORMAL_ITEM, 10, 20)

        item.update()

        assert item.sell_in == 9
        assert item.quality == 19

    def test_quality_decreases_twice_as_fast_once_sell_by_date_has_passed(self):
        item = Item(NORMAL_ITEM, 0, 20)

        item.update()

        assert item.sell_in == -1
        assert item.quality == 18

    def test_quality_never_goes_negative_when_already_zero(self):
        item = Item(NORMAL_ITEM, 5, 0)

        item.update()

        assert item.quality == 0

    def test_quality_never_goes_negative_even_when_expiring_from_one(self):
        item = Item(NORMAL_ITEM, 0, 1)

        item.update()

        assert item.sell_in == -1
        assert item.quality == 0

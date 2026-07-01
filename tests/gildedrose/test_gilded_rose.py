"""Characterization tests for GildedRose.update_quality.

These tests pin down the *current* production behaviour of the legacy
update_quality implementation, including its quirks (e.g. items past their
sell date can gain/lose quality twice in the same update). They exist as a
safety net for refactoring, not as a spec of desired behaviour.
"""

from gildedrose.gilded_rose import GildedRose
from gildedrose.item import Item

AGED_BRIE = "Aged Brie"
SULFURAS = "Sulfuras, Hand of Ragnaros"
BACKSTAGE_PASSES = "Backstage passes to a TAFKAL80ETC concert"
NORMAL_ITEM = "+5 Dexterity Vest"


def update_once(name, sell_in, quality):
    item = Item(name, sell_in, quality)
    GildedRose([item]).update_quality()
    return item


def update_for_days(name, sell_in, quality, days):
    item = Item(name, sell_in, quality)
    gilded_rose = GildedRose([item])
    for _ in range(days):
        gilded_rose.update_quality()
    return item


class TestNormalItems:
    def test_sell_in_and_quality_decrease_by_one_before_sell_date(self):
        item = update_once(NORMAL_ITEM, 10, 20)

        assert item.sell_in == 9
        assert item.quality == 19

    def test_quality_decreases_twice_as_fast_once_sell_in_is_zero(self):
        item = update_once(NORMAL_ITEM, 0, 20)

        assert item.sell_in == -1
        assert item.quality == 18

    def test_quality_decreases_twice_as_fast_once_sell_by_date_has_passed(self):
        item = update_once(NORMAL_ITEM, -5, 5)

        assert item.sell_in == -6
        assert item.quality == 3

    def test_quality_never_goes_negative_when_already_zero(self):
        item = update_once(NORMAL_ITEM, 5, 0)

        assert item.quality == 0

    def test_quality_never_goes_negative_even_when_expiring_from_one(self):
        item = update_once(NORMAL_ITEM, 0, 1)

        assert item.sell_in == -1
        assert item.quality == 0

    def test_quality_stays_at_zero_over_multiple_days_after_expiry(self):
        item = update_for_days(NORMAL_ITEM, 1, 1, days=2)

        assert item.sell_in == -1
        assert item.quality == 0


class TestAgedBrie:
    def test_quality_increases_as_sell_in_approaches(self):
        item = update_once(AGED_BRIE, 10, 20)

        assert item.sell_in == 9
        assert item.quality == 21

    def test_quality_never_exceeds_fifty(self):
        item = update_once(AGED_BRIE, 5, 50)

        assert item.quality == 50

    def test_quality_can_increase_twice_in_one_day_once_past_sell_by_date(self):
        item = update_once(AGED_BRIE, 0, 48)

        assert item.sell_in == -1
        assert item.quality == 50

    def test_quality_still_capped_at_fifty_after_sell_by_date(self):
        item = update_once(AGED_BRIE, -5, 49)

        assert item.sell_in == -6
        assert item.quality == 50


class TestSulfuras:
    def test_quality_and_sell_in_never_change(self):
        item = update_once(SULFURAS, 0, 80)

        assert item.sell_in == 0
        assert item.quality == 80

    def test_quality_and_sell_in_never_change_past_sell_by_date(self):
        item = update_once(SULFURAS, -5, 80)

        assert item.sell_in == -5
        assert item.quality == 80


class TestBackstagePasses:
    def test_quality_increases_by_one_when_more_than_ten_days_remain(self):
        item = update_once(BACKSTAGE_PASSES, 15, 20)

        assert item.sell_in == 14
        assert item.quality == 21

    def test_quality_increases_by_two_when_ten_days_or_fewer_remain(self):
        item = update_once(BACKSTAGE_PASSES, 10, 20)

        assert item.sell_in == 9
        assert item.quality == 22

    def test_quality_increases_by_two_when_six_days_remain(self):
        item = update_once(BACKSTAGE_PASSES, 6, 20)

        assert item.sell_in == 5
        assert item.quality == 22

    def test_quality_increases_by_three_when_five_days_or_fewer_remain(self):
        item = update_once(BACKSTAGE_PASSES, 5, 20)

        assert item.sell_in == 4
        assert item.quality == 23

    def test_quality_increases_by_three_on_the_last_day(self):
        item = update_once(BACKSTAGE_PASSES, 1, 20)

        assert item.sell_in == 0
        assert item.quality == 23

    def test_quality_drops_to_zero_after_the_concert(self):
        item = update_once(BACKSTAGE_PASSES, 0, 20)

        assert item.sell_in == -1
        assert item.quality == 0

    def test_quality_capped_at_fifty_even_when_double_increment_applies(self):
        item = update_once(BACKSTAGE_PASSES, 5, 49)

        assert item.quality == 50

    def test_quality_capped_at_fifty_even_when_triple_increment_applies(self):
        item = update_once(BACKSTAGE_PASSES, 5, 48)

        assert item.quality == 50


class TestMultipleItems:
    def test_update_quality_updates_every_item_in_the_list(self):
        items = [
            Item(NORMAL_ITEM, 10, 20),
            Item(AGED_BRIE, 2, 0),
            Item(SULFURAS, 0, 80),
            Item(BACKSTAGE_PASSES, 15, 20),
        ]
        gilded_rose = GildedRose(items)

        gilded_rose.update_quality()

        assert [(item.sell_in, item.quality) for item in items] == [
            (9, 19),
            (1, 1),
            (0, 80),
            (14, 21),
        ]

    def test_update_quality_with_empty_item_list_is_a_no_op(self):
        gilded_rose = GildedRose([])

        gilded_rose.update_quality()

        assert gilded_rose.items == []

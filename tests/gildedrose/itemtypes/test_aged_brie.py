"""Unit tests for AgedBrie's quality-increase rules."""

from gildedrose.itemtypes.aged_brie import AgedBrie

NAME = AgedBrie.item_name


class TestAgedBrie:
    def test_quality_increases_as_sell_in_approaches(self):
        item = AgedBrie(NAME, 10, 20)

        item.update()

        assert item.sell_in == 9
        assert item.quality == 21

    def test_quality_never_exceeds_fifty(self):
        item = AgedBrie(NAME, 5, 50)

        item.update()

        assert item.quality == 50

    def test_quality_can_increase_twice_in_one_day_once_past_sell_by_date(self):
        item = AgedBrie(NAME, 0, 48)

        item.update()

        assert item.sell_in == -1
        assert item.quality == 50

    def test_quality_still_capped_at_fifty_after_sell_by_date(self):
        item = AgedBrie(NAME, -5, 49)

        item.update()

        assert item.sell_in == -6
        assert item.quality == 50

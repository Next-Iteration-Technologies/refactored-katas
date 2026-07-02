"""Unit tests confirming Sulfuras never changes."""

from gildedrose.itemtypes.sulfuras import Sulfuras

NAME = Sulfuras.item_name


class TestSulfuras:
    def test_quality_and_sell_in_never_change(self):
        item = Sulfuras(NAME, 0, 80)

        item.update()

        assert item.sell_in == 0
        assert item.quality == 80

    def test_quality_and_sell_in_never_change_past_sell_by_date(self):
        item = Sulfuras(NAME, -5, 80)

        item.update()

        assert item.sell_in == -5
        assert item.quality == 80

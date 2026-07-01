"""Unit tests for Conjured's double-speed quality degrade rule."""

from gildedrose.conjured import Conjured

NAME = Conjured.item_name


class TestConjured:
    def test_quality_degrades_twice_as_fast_before_sell_date(self):
        item = Conjured(NAME, 10, 20)

        item.update()

        assert item.sell_in == 9
        assert item.quality == 18

    def test_quality_degrades_four_times_as_fast_once_sell_by_date_has_passed(self):
        item = Conjured(NAME, 0, 20)

        item.update()

        assert item.sell_in == -1
        assert item.quality == 16

    def test_quality_never_goes_negative_when_already_zero(self):
        item = Conjured(NAME, 5, 0)

        item.update()

        assert item.quality == 0

    def test_quality_never_goes_negative_even_when_close_to_zero(self):
        item = Conjured(NAME, 5, 1)

        item.update()

        assert item.quality == 0

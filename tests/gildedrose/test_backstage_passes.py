"""Unit tests for BackstagePasses' tiered quality-increase rules."""

import pytest

from gildedrose.backstage_passes import BackstagePasses

NAME = BackstagePasses.item_name


class TestBackstagePasses:
    @pytest.mark.parametrize(
        "sell_in, expected_increase",
        [
            (15, 1),  # more than 10 days remain
            (10, 2),  # at the "10 days or fewer" threshold
            (6, 2),  # between the two thresholds
            (5, 3),  # at the "5 days or fewer" threshold
            (1, 3),  # last day before the concert
        ],
    )
    def test_quality_increase_scales_with_days_remaining(self, sell_in, expected_increase):
        item = BackstagePasses(NAME, sell_in, 20)

        item.update()

        assert item.quality == 20 + expected_increase

    def test_quality_drops_to_zero_after_the_concert(self):
        item = BackstagePasses(NAME, 0, 20)

        item.update()

        assert item.sell_in == -1
        assert item.quality == 0

    def test_quality_capped_at_fifty_even_when_double_increment_applies(self):
        item = BackstagePasses(NAME, 5, 49)

        item.update()

        assert item.quality == 50

    def test_quality_capped_at_fifty_even_when_triple_increment_applies(self):
        item = BackstagePasses(NAME, 5, 48)

        item.update()

        assert item.quality == 50

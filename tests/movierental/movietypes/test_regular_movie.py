import pytest

from movierental.movietypes.regular_movie import RegularMovie


@pytest.mark.parametrize("days,expected_charge", [
    (1, 2.0),
    (2, 2.0),
    (3, 3.5),
    (4, 5.0),
])
def test_regular_movie_charge(days, expected_charge):
    assert RegularMovie("Jaws").charge(days) == expected_charge


def test_regular_movie_does_not_earn_bonus_points():
    assert RegularMovie("Jaws").earns_bonus_points is False

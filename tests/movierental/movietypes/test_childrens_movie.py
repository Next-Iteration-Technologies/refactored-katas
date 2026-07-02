import pytest

from movierental.movietypes.childrens_movie import ChildrensMovie


@pytest.mark.parametrize("days,expected_charge", [
    (1, 1.5),
    (2, 1.5),
    (3, 1.5),
    (4, 3.0),
    (5, 4.5),
])
def test_childrens_movie_charge(days, expected_charge):
    assert ChildrensMovie("Bambi").charge(days) == expected_charge


def test_childrens_movie_does_not_earn_bonus_points():
    assert ChildrensMovie("Bambi").earns_bonus_points is False

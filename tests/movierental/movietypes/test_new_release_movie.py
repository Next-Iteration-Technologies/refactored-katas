import pytest

from movierental.movietypes.new_release_movie import NewReleaseMovie


@pytest.mark.parametrize("days,expected_charge", [
    (1, 3.0),
    (2, 6.0),
    (3, 9.0),
])
def test_new_release_charge(days, expected_charge):
    assert NewReleaseMovie("Top Gun").charge(days) == expected_charge


def test_new_release_movie_earns_bonus_points():
    assert NewReleaseMovie("Top Gun").earns_bonus_points is True

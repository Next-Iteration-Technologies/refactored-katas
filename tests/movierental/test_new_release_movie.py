import pytest

from movierental.new_release_movie import NewReleaseMovie


@pytest.mark.parametrize("days,expected_charge", [
    (1, 3.0),
    (2, 6.0),
    (3, 9.0),
])
def test_new_release_charge(days, expected_charge):
    assert NewReleaseMovie("Top Gun").charge(days) == expected_charge


def test_new_release_one_day_earns_one_point():
    assert NewReleaseMovie("Top Gun").points(1) == 1


def test_new_release_two_days_earns_two_points():
    assert NewReleaseMovie("Top Gun").points(2) == 2


def test_new_release_three_days_earns_two_points():
    assert NewReleaseMovie("Top Gun").points(3) == 2

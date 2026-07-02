from movierental.movietypes.childrens_movie import ChildrensMovie
from movierental.movietypes.new_release_movie import NewReleaseMovie
from movierental.movietypes.regular_movie import RegularMovie
from movierental.rental import Rental


def test_rental_exposes_movie():
    movie = RegularMovie("Jaws")
    rental = Rental(movie, 3)
    assert rental.movie is movie


def test_rental_exposes_days_rented():
    movie = RegularMovie("Jaws")
    rental = Rental(movie, 3)
    assert rental.days_rented == 3


def test_rental_title_delegates_to_movie():
    rental = Rental(RegularMovie("Jaws"), 1)
    assert rental.title == "Jaws"


def test_rental_charge_delegates_to_movie():
    rental = Rental(RegularMovie("Jaws"), 3)
    assert rental.charge() == 3.5


def test_rental_charge_with_childrens_movie():
    rental = Rental(ChildrensMovie("Bambi"), 4)
    assert rental.charge() == 3.0


# --- Frequent renter points ---

def test_rental_points_regular_movie_earns_standard_points():
    rental = Rental(RegularMovie("Jaws"), 5)
    assert rental.points() == 1


def test_rental_points_childrens_movie_earns_standard_points():
    rental = Rental(ChildrensMovie("Bambi"), 4)
    assert rental.points() == 1


def test_rental_points_new_release_one_day_earns_standard_points():
    rental = Rental(NewReleaseMovie("Top Gun"), 1)
    assert rental.points() == 1


def test_rental_points_new_release_multiple_days_earns_bonus_points():
    rental = Rental(NewReleaseMovie("Top Gun"), 2)
    assert rental.points() == 2

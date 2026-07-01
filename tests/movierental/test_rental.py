from movierental.childrens_movie import ChildrensMovie
from movierental.new_release_movie import NewReleaseMovie
from movierental.regular_movie import RegularMovie
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


def test_rental_points_delegates_to_movie():
    rental = Rental(NewReleaseMovie("Top Gun"), 2)
    assert rental.points() == 2


def test_rental_charge_with_childrens_movie():
    rental = Rental(ChildrensMovie("Bambi"), 4)
    assert rental.charge() == 3.0


def test_rental_points_with_childrens_movie():
    rental = Rental(ChildrensMovie("Bambi"), 4)
    assert rental.points() == 1

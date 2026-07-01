import pytest

from movierental.childrens_movie import ChildrensMovie
from movierental.movie import Movie
from movierental.new_release_movie import NewReleaseMovie
from movierental.regular_movie import RegularMovie


def test_movie_is_abstract():
    with pytest.raises(TypeError):
        Movie("Jaws")  # type: ignore[abstract]


def test_regular_movie_stores_title():
    assert RegularMovie("Jaws").title == "Jaws"


def test_new_release_movie_stores_title():
    assert NewReleaseMovie("Top Gun").title == "Top Gun"


def test_childrens_movie_stores_title():
    assert ChildrensMovie("Bambi").title == "Bambi"


def test_all_movie_types_are_subclasses_of_movie():
    assert isinstance(RegularMovie("Jaws"), Movie)
    assert isinstance(NewReleaseMovie("Top Gun"), Movie)
    assert isinstance(ChildrensMovie("Bambi"), Movie)

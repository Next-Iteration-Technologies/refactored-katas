import pytest

from movierental.customer import Customer
from movierental.formatters.text_statement_formatter import TextStatementFormatter
from movierental.movietypes.regular_movie import RegularMovie
from movierental.rental import Rental


# --- Fixtures / helpers ---

@pytest.fixture
def customer():
    return Customer("Alice")


def make_rental(title, movie_class, days):
    return Rental(movie_class(title), days)


# --- Text formatter ---

def test_text_formatter_header(customer):
    assert TextStatementFormatter().format(customer).startswith("Rental Record for Alice\n")


def test_text_formatter_rental_line(customer):
    customer.add_rental(make_rental("Jaws", RegularMovie, 2))
    assert "\tJaws\t2.0\n" in TextStatementFormatter().format(customer)


def test_text_formatter_footer(customer):
    customer.add_rental(make_rental("Jaws", RegularMovie, 2))
    statement = TextStatementFormatter().format(customer)
    assert "Amount owed is 2.0\n" in statement
    assert statement.endswith("You earned 1 frequent renter points")


def test_text_formatter_no_rentals(customer):
    statement = TextStatementFormatter().format(customer)
    assert "Amount owed is 0.0\n" in statement
    assert "You earned 0 frequent renter points" in statement

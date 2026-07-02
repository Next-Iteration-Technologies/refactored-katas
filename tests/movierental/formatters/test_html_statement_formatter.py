import pytest

from movierental.customer import Customer
from movierental.formatters.html_statement_formatter import HtmlStatementFormatter
from movierental.movietypes.childrens_movie import ChildrensMovie
from movierental.movietypes.new_release_movie import NewReleaseMovie
from movierental.movietypes.regular_movie import RegularMovie
from movierental.rental import Rental


# --- Fixtures / helpers ---

@pytest.fixture
def customer():
    return Customer("Alice")


def make_rental(title, movie_class, days):
    return Rental(movie_class(title), days)


# --- HTML formatter ---

def test_html_formatter_header(customer):
    statement = HtmlStatementFormatter().format(customer)
    assert statement.startswith("<h1>Rental Record for <em>Alice</em></h1>\n<table>\n")


def test_html_formatter_rental_line(customer):
    customer.add_rental(make_rental("Jaws", RegularMovie, 2))
    statement = HtmlStatementFormatter().format(customer)
    assert "  <tr><td>Jaws</td><td>2.0</td></tr>\n" in statement


def test_html_formatter_footer(customer):
    customer.add_rental(make_rental("Jaws", RegularMovie, 2))
    statement = HtmlStatementFormatter().format(customer)
    assert "<p>Amount owed is <em>2.0</em></p>\n" in statement
    assert statement.endswith("<p>You earned <em>1</em> frequent renter points</p>")


def test_html_formatter_no_rentals(customer):
    statement = HtmlStatementFormatter().format(customer)
    assert "<table>\n</table>\n" in statement
    assert "<p>Amount owed is <em>0.0</em></p>\n" in statement
    assert "<p>You earned <em>0</em> frequent renter points</p>" in statement


def test_html_formatter_rental_lines_appear_in_order(customer):
    customer.add_rental(make_rental("Jaws", RegularMovie, 1))
    customer.add_rental(make_rental("Bambi", ChildrensMovie, 1))
    statement = HtmlStatementFormatter().format(customer)
    assert statement.index("Jaws") < statement.index("Bambi")


def test_html_formatter_matches_requirements_example():
    customer = Customer("martin")
    customer.add_rental(make_rental("Ran", RegularMovie, 3))
    customer.add_rental(make_rental("Trois Couleurs: Bleu", RegularMovie, 2))

    expected = (
        "<h1>Rental Record for <em>martin</em></h1>\n"
        "<table>\n"
        "  <tr><td>Ran</td><td>3.5</td></tr>\n"
        "  <tr><td>Trois Couleurs: Bleu</td><td>2.0</td></tr>\n"
        "</table>\n"
        "<p>Amount owed is <em>5.5</em></p>\n"
        "<p>You earned <em>2</em> frequent renter points</p>"
    )
    assert HtmlStatementFormatter().format(customer) == expected


def test_html_formatter_all_movie_types():
    customer = Customer("Bob")
    customer.add_rental(make_rental("Jaws", RegularMovie, 2))
    customer.add_rental(make_rental("Short New", NewReleaseMovie, 1))
    customer.add_rental(make_rental("Bambi", ChildrensMovie, 3))

    expected = (
        "<h1>Rental Record for <em>Bob</em></h1>\n"
        "<table>\n"
        "  <tr><td>Jaws</td><td>2.0</td></tr>\n"
        "  <tr><td>Short New</td><td>3.0</td></tr>\n"
        "  <tr><td>Bambi</td><td>1.5</td></tr>\n"
        "</table>\n"
        "<p>Amount owed is <em>6.5</em></p>\n"
        "<p>You earned <em>3</em> frequent renter points</p>"
    )
    assert HtmlStatementFormatter().format(customer) == expected

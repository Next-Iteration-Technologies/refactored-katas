import pytest

from movierental.customer import Customer
from movierental.formatters.html_statement_formatter import HtmlStatementFormatter
from movierental.formatters.text_statement_formatter import TextStatementFormatter
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


# --- Customer properties ---

def test_customer_exposes_name():
    assert Customer("Alice").name == "Alice"


def test_add_rental_accumulates_rentals(customer):
    customer.add_rental(make_rental("Jaws", RegularMovie, 1))
    customer.add_rental(make_rental("Bambi", ChildrensMovie, 1))
    statement = customer.statement(TextStatementFormatter())
    assert "\tJaws\t" in statement
    assert "\tBambi\t" in statement


# --- Regular movie charges ---

@pytest.mark.parametrize("days,expected_charge", [
    (1, 2.0),
    (2, 2.0),
    (3, 3.5),
    (4, 5.0),
])
def test_regular_movie_charge(customer, days, expected_charge):
    customer.add_rental(make_rental("Jaws", RegularMovie, days))
    assert f"\tJaws\t{expected_charge}\n" in customer.statement(TextStatementFormatter())


# --- New release movie charges ---

@pytest.mark.parametrize("days,expected_charge", [
    (1, 3.0),
    (2, 6.0),
    (3, 9.0),
])
def test_new_release_charge(customer, days, expected_charge):
    customer.add_rental(make_rental("Top Gun", NewReleaseMovie, days))
    assert f"\tTop Gun\t{expected_charge}\n" in customer.statement(TextStatementFormatter())


# --- Children's movie charges ---

@pytest.mark.parametrize("days,expected_charge", [
    (1, 1.5),
    (2, 1.5),
    (3, 1.5),
    (4, 3.0),
    (5, 4.5),
])
def test_childrens_movie_charge(customer, days, expected_charge):
    customer.add_rental(make_rental("Bambi", ChildrensMovie, days))
    assert f"\tBambi\t{expected_charge}\n" in customer.statement(TextStatementFormatter())


# --- Frequent renter points ---

def test_regular_rental_earns_one_point(customer):
    customer.add_rental(make_rental("Jaws", RegularMovie, 5))
    assert "You earned 1 frequent renter points" in customer.statement(TextStatementFormatter())


def test_childrens_rental_earns_one_point(customer):
    customer.add_rental(make_rental("Bambi", ChildrensMovie, 5))
    assert "You earned 1 frequent renter points" in customer.statement(TextStatementFormatter())


def test_new_release_one_day_earns_one_point(customer):
    customer.add_rental(make_rental("Top Gun", NewReleaseMovie, 1))
    assert "You earned 1 frequent renter points" in customer.statement(TextStatementFormatter())


def test_new_release_two_days_earns_two_points(customer):
    customer.add_rental(make_rental("Top Gun", NewReleaseMovie, 2))
    assert "You earned 2 frequent renter points" in customer.statement(TextStatementFormatter())


def test_new_release_three_days_earns_two_points(customer):
    customer.add_rental(make_rental("Top Gun", NewReleaseMovie, 3))
    assert "You earned 2 frequent renter points" in customer.statement(TextStatementFormatter())


def test_multiple_rentals_accumulate_points(customer):
    customer.add_rental(make_rental("Jaws", RegularMovie, 1))
    customer.add_rental(make_rental("Top Gun", NewReleaseMovie, 2))
    customer.add_rental(make_rental("Bambi", ChildrensMovie, 1))
    assert "You earned 4 frequent renter points" in customer.statement(TextStatementFormatter())


# --- Statement format ---

def test_statement_starts_with_customer_name(customer):
    assert customer.statement(TextStatementFormatter()).startswith("Rental Record for Alice\n")


def test_statement_rental_line_format(customer):
    customer.add_rental(make_rental("Jaws", RegularMovie, 2))
    assert "\tJaws\t2.0\n" in customer.statement(TextStatementFormatter())


def test_statement_title_with_spaces(customer):
    customer.add_rental(make_rental("The Dark Knight", RegularMovie, 1))
    assert "\tThe Dark Knight\t2.0\n" in customer.statement(TextStatementFormatter())


def test_statement_total_is_sum_of_charges(customer):
    customer.add_rental(make_rental("Jaws", RegularMovie, 2))
    customer.add_rental(make_rental("Top Gun", NewReleaseMovie, 1))
    assert "Amount owed is 5.0\n" in customer.statement(TextStatementFormatter())


def test_statement_rental_lines_appear_in_order(customer):
    customer.add_rental(make_rental("Jaws", RegularMovie, 1))
    customer.add_rental(make_rental("Bambi", ChildrensMovie, 1))
    statement = customer.statement(TextStatementFormatter())
    assert statement.index("\tJaws\t") < statement.index("\tBambi\t")


def test_statement_ends_without_trailing_newline(customer):
    customer.add_rental(make_rental("Jaws", RegularMovie, 1))
    assert customer.statement(TextStatementFormatter()).endswith("frequent renter points")


def test_statement_no_rentals(customer):
    statement = customer.statement(TextStatementFormatter())
    assert "Amount owed is 0.0\n" in statement
    assert "You earned 0 frequent renter points" in statement


def test_statement_all_movie_types():
    customer = Customer("Bob")
    customer.add_rental(make_rental("Jaws", RegularMovie, 2))
    customer.add_rental(make_rental("Golden Eye", RegularMovie, 3))
    customer.add_rental(make_rental("Short New", NewReleaseMovie, 1))
    customer.add_rental(make_rental("Long New", NewReleaseMovie, 2))
    customer.add_rental(make_rental("Bambi", ChildrensMovie, 3))
    customer.add_rental(make_rental("Toy Story", ChildrensMovie, 4))

    expected = (
        "Rental Record for Bob\n"
        "\tJaws\t2.0\n"
        "\tGolden Eye\t3.5\n"
        "\tShort New\t3.0\n"
        "\tLong New\t6.0\n"
        "\tBambi\t1.5\n"
        "\tToy Story\t3.0\n"
        "Amount owed is 19.0\n"
        "You earned 7 frequent renter points"
    )
    assert customer.statement(TextStatementFormatter()) == expected


# --- HTML statement format ---

def test_html_statement_starts_with_header(customer):
    assert customer.statement(HtmlStatementFormatter()).startswith("<h1>Rental Record for <em>Alice</em></h1>\n<table>\n")


def test_html_statement_rental_line_format(customer):
    customer.add_rental(make_rental("Jaws", RegularMovie, 2))
    assert "<tr><td>Jaws</td><td>2.0</td></tr>" in customer.statement(HtmlStatementFormatter())


def test_html_statement_total_is_sum_of_charges(customer):
    customer.add_rental(make_rental("Jaws", RegularMovie, 2))
    customer.add_rental(make_rental("Top Gun", NewReleaseMovie, 1))
    assert "<p>Amount owed is <em>5.0</em></p>" in customer.statement(HtmlStatementFormatter())


def test_html_statement_ends_without_trailing_newline(customer):
    customer.add_rental(make_rental("Jaws", RegularMovie, 1))
    assert customer.statement(HtmlStatementFormatter()).endswith("frequent renter points</p>")


def test_html_statement_no_rentals(customer):
    statement = customer.statement(HtmlStatementFormatter())
    assert "<p>Amount owed is <em>0.0</em></p>" in statement
    assert "<p>You earned <em>0</em> frequent renter points</p>" in statement


def test_html_statement_matches_requirements_example():
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
    assert customer.statement(HtmlStatementFormatter()) == expected

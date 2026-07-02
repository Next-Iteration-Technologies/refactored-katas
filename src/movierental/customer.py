from movierental.html_statement_formatter import HtmlStatementFormatter
from movierental.rental import Rental
from movierental.text_statement_formatter import TextStatementFormatter


class Customer:

    def __init__(self, name: str):
        self._name = name
        self._rentals: list[Rental] = []

    def add_rental(self, rental: Rental) -> None:
        self._rentals.append(rental)

    @property
    def name(self) -> str:
        return self._name

    @property
    def rentals(self) -> tuple[Rental, ...]:
        return tuple(self._rentals)

    @property
    def total_amount(self) -> float:
        return sum((rental.charge() for rental in self._rentals), 0.0)

    @property
    def total_points(self) -> int:
        return sum(rental.points() for rental in self._rentals)

    def statement(self) -> str:
        return TextStatementFormatter().format(self)

    def html_statement(self) -> str:
        return HtmlStatementFormatter().format(self)

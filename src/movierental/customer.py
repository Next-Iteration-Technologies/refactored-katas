from movierental.rental import Rental
from movierental.statement_formatter import StatementFormatter


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

    def statement(self, formatter: StatementFormatter) -> str:
        return formatter.format(self)

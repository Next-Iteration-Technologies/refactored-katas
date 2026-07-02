from __future__ import annotations

from typing import TYPE_CHECKING

from movierental.statement_formatter import StatementFormatter

if TYPE_CHECKING:
    from movierental.customer import Customer
    from movierental.rental import Rental


class TextStatementFormatter(StatementFormatter):
    """Renders a plain-text rental statement."""

    def _header(self, customer: Customer) -> str:
        return f"Rental Record for {customer.name}\n"

    def _rental_line(self, rental: Rental) -> str:
        return f"\t{rental.title}\t{rental.charge()}\n"

    def _footer(self, customer: Customer) -> str:
        return (
            f"Amount owed is {customer.total_amount}\n"
            f"You earned {customer.total_points} frequent renter points"
        )

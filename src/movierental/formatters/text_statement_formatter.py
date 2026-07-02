from __future__ import annotations

from typing import TYPE_CHECKING

from movierental.statement_formatter import StatementFormatter

if TYPE_CHECKING:
    from movierental.customer import Customer
    from movierental.rental import Rental


class TextStatementFormatter(StatementFormatter):
    """Renders a plain-text rental statement."""

    def _header(self, customer: Customer) -> str:
        return f"{self.HEADER_LABEL} {customer.name}\n"

    def _rental_line(self, rental: Rental) -> str:
        return f"\t{rental.title}\t{rental.charge()}\n"

    def _footer(self, customer: Customer) -> str:
        return (
            f"{self.AMOUNT_OWED_LABEL} {customer.total_amount}\n"
            f"{self.POINTS_EARNED_LABEL} {customer.total_points} {self.POINTS_EARNED_SUFFIX}"
        )

from __future__ import annotations

from typing import TYPE_CHECKING

from movierental.statement_formatter import StatementFormatter

if TYPE_CHECKING:
    from movierental.customer import Customer
    from movierental.rental import Rental


class HtmlStatementFormatter(StatementFormatter):
    """Renders an HTML rental statement."""

    def _header(self, customer: Customer) -> str:
        return f"<h1>{self.HEADER_LABEL} <em>{customer.name}</em></h1>\n<table>\n"

    def _rental_line(self, rental: Rental) -> str:
        return f"  <tr><td>{rental.title}</td><td>{rental.charge()}</td></tr>\n"

    def _footer(self, customer: Customer) -> str:
        return (
            "</table>\n"
            f"<p>{self.AMOUNT_OWED_LABEL} <em>{customer.total_amount}</em></p>\n"
            f"<p>{self.POINTS_EARNED_LABEL} <em>{customer.total_points}</em> {self.POINTS_EARNED_SUFFIX}</p>"
        )

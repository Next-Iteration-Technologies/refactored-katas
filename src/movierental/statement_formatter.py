from __future__ import annotations

from abc import ABC, abstractmethod
from typing import TYPE_CHECKING

if TYPE_CHECKING:
    from movierental.customer import Customer
    from movierental.rental import Rental


class StatementFormatter(ABC):
    """Renders a customer's rental statement as header, body, and footer sections."""

    def format(self, customer: Customer) -> str:
        return self._header(customer) + self._body(customer) + self._footer(customer)

    def _body(self, customer: Customer) -> str:
        return "".join(self._rental_line(rental) for rental in customer.rentals)

    @abstractmethod
    def _header(self, customer: Customer) -> str: ...

    @abstractmethod
    def _rental_line(self, rental: Rental) -> str: ...

    @abstractmethod
    def _footer(self, customer: Customer) -> str: ...

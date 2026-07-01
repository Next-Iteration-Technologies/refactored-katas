from movierental.rental import Rental


class Customer:

    def __init__(self, name: str):
        self._name = name
        self._rentals: list[Rental] = []

    def add_rental(self, rental: Rental) -> None:
        self._rentals.append(rental)

    @property
    def name(self) -> str:
        return self._name

    def _format_rental_line(self, rental: Rental) -> str:
        return f"\t{rental.title}\t{rental.charge()}\n"

    def statement(self) -> str:
        total_amount = 0.0
        total_points = 0
        rental_lines = []

        for rental in self._rentals:
            total_amount += rental.charge()
            total_points += rental.points()
            rental_lines.append(self._format_rental_line(rental))

        header = f"Rental Record for {self.name}\n"
        footer = f"Amount owed is {total_amount}\nYou earned {total_points} frequent renter points"
        return header + "".join(rental_lines) + footer

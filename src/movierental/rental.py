from movierental.movie import Movie


class Rental:

    def __init__(self, movie: Movie, days_rented: int):
        self._movie = movie
        self._days_rented = days_rented

    @property
    def movie(self) -> Movie:
        return self._movie

    @property
    def days_rented(self) -> int:
        return self._days_rented

    @property
    def title(self) -> str:
        return self._movie.title

    def charge(self) -> float:
        return self._movie.charge(self._days_rented)

    def points(self) -> int:
        return self._movie.points(self._days_rented)

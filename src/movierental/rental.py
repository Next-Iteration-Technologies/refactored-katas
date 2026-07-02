from movierental.movie import Movie


class Rental:

    STANDARD_POINTS = 1
    BONUS_POINTS = 2
    BONUS_POINTS_THRESHOLD_DAYS = 2

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
        if self._qualifies_for_bonus_points():
            return self.BONUS_POINTS
        return self.STANDARD_POINTS

    def _qualifies_for_bonus_points(self) -> bool:
        return self._movie.earns_bonus_points and self._days_rented >= self.BONUS_POINTS_THRESHOLD_DAYS

from movierental.movietypes.free_days_movie import FreeDaysMovie


class ChildrensMovie(FreeDaysMovie):

    BASE_CHARGE = 1.5
    FREE_DAYS = 3
    EXTRA_CHARGE = 1.5

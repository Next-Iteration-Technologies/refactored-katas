from movierental.free_days_movie import FreeDaysMovie


class RegularMovie(FreeDaysMovie):

    BASE_CHARGE = 2.0
    FREE_DAYS = 2
    EXTRA_CHARGE = 1.5

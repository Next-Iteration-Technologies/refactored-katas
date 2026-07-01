# Movie Rental Requirements

In this MovieRental codebase, the `statement` method on `Customer` prints out a simple text rental statement:

```
Rental Record for martin
	Ran	3.5
	Trois Couleurs: Bleu	2.0
Amount owed is 5.5
You earned 2 frequent renter points
```

## Goal

We want to write an HTML version of the statement method:

```html
<h1>Rental Record for <em>martin</em></h1>
<table>
  <tr><td>Ran</td><td>3.5</td></tr>
  <tr><td>Trois Couleurs: Bleu</td><td>2.0</td></tr>
</table>
<p>Amount owed is <em>5.5</em></p>
<p>You earned <em>2</em> frequent renter points</p>
```

## Build

All you need is [Python](https://www.python.org/downloads/) 3.10 or later.

**macOS / Linux**

```bash
python -m venv .venv
source .venv/bin/activate
pip install -e ".[test]"
```

**Windows**

```bat
python -m venv .venv
.venv\Scripts\activate
pip install -e ".[test]"
```

## Testing

Unit tests can be run using pytest:

```bash
pytest
```

Tests are located in `tests/` and use [pytest](https://docs.pytest.org/).

# Learnings from Review & Refactor Sessions

Captures practices observed during live refactoring that are **not already covered** by
`core-standards.md` or `python.md`. Each entry notes where it applies and why it matters.

---

## 1. Write Characterization Tests Before Any Refactoring

**What:** Before touching any production code, write a comprehensive test suite that locks in the
current observable behavior — pricing boundaries, output format, edge cases (e.g. zero rentals),
ordering, and the golden-path integration test.

**Why it matters:** Refactoring without a safety net only confirms the code still compiles, not
that it still behaves correctly. Characterization tests make every subsequent refactoring step
verifiable in seconds.

**How to apply:**
- Cover every public method's output, not just the happy path.
- For threshold-based logic, always test: below-threshold, at-threshold-boundary,
  one-above-threshold, and well-above-threshold.
- Include one full integration test that asserts the complete output string end-to-end.
- Write these tests *before* the first refactor commit; they are the contract, not a bonus.

---

## 2. Follow a Safe Refactoring Sequence

**What:** When refactoring a class with multiple intertwined responsibilities, apply changes
in this order:

1. Characterization tests (lock in behaviour)
2. SRP — extract focused private methods
3. OCP — replace switch/if-elif chains with polymorphism
4. Magic numbers — extract named constants
5. Tell, Don't Ask — add delegation methods to eliminate message chains
6. Type hints — annotate once the design has settled

**Why it matters:** Each step is safe only because the previous step's tests are in place.
Doing type hints or constants before SRP means annotating code that is about to be restructured.

---

## 3. Use `abc.ABC` + `@abstractmethod` When Replacing Switch Statements with Polymorphism

**What:** In Python, when eliminating an `if/elif` chain that switches on a type code, make the
base class abstract using `abc.ABC` and mark the varying methods with `@abstractmethod`.

```python
from abc import ABC, abstractmethod

class Movie(ABC):
    @abstractmethod
    def charge(self, days_rented: int) -> float: pass

    @abstractmethod
    def points(self, days_rented: int) -> int: pass
```

**Why it matters:** `python.md` says to use polymorphism but does not prescribe the mechanism.
Without `ABC`, a new subclass that omits `charge()` silently returns `None` at runtime instead
of failing loudly at definition time.

---

## 4. Keep Constant Names Consistent Across Sibling Subclasses

**What:** When multiple subclasses share conceptually equivalent constants, use the same name in
all of them — even if the values differ.

```python
# Good — reader can compare subclasses side-by-side
class RegularMovie(Movie):
    BASE_CHARGE = 2.0
    EXTRA_CHARGE = 1.5

class ChildrensMovie(Movie):
    BASE_CHARGE = 1.5
    EXTRA_CHARGE = 1.5
```

**Why it matters:** Inconsistent names (e.g. `BASE_CHARGE` in one class, `BASE_AMOUNT` in
another) force readers to mentally map equivalent concepts. Sibling classes should read in
parallel.

**How to apply:** After extracting constants, scan all sibling subclasses and align names for
any constant that plays the same business role.

---

## 5. Establish a One-Way Import Chain Before Adding Type Hints

**What:** When adding type annotations across multiple related classes, map the dependency
direction first and ensure it is strictly one-way before adding any imports.

```
Movie  ←  Rental  ←  Customer
```

`rental.py` imports `Movie`; `customer.py` imports `Rental`; `movie.py` imports nothing.
No circular dependency is introduced.

**Why it matters:** Adding `from x import Y` for a type hint can silently introduce a circular
import that only surfaces at runtime. Mapping the chain first prevents this.

**How to apply:** Draw the dependency arrow for every new import before writing it. If an arrow
would create a cycle, use `from __future__ import annotations` (deferred evaluation) or a
`TYPE_CHECKING` guard instead.

---

## 6. Check for New Middle Men After Every Refactor Step

**What:** After moving logic to a more appropriate class, audit the original class for methods
that have become single-line delegators with no added value and remove them.

```python
# Before moving pricing to Movie — these made sense:
def _calculate_charge(self, rental): ...
def _calculate_points(self, rental): ...

# After — they are Middle Men; delete them and inline the call in statement()
```

**Why it matters:** `core-standards.md` lists Middle Man as a code smell, but it is easy to
miss newly-created Middle Men that did not exist before the refactor. Each refactor step can
introduce them in the class that *lost* responsibility.

**How to apply:** After every refactoring step, re-read the changed class top-to-bottom and
flag any method whose entire body is a single delegation call.

---

## 7. Parametrize Boundary Tests for Threshold-Based Pricing

**What:** For any pricing rule with a free-days threshold, always parametrize with these four
cases:

| Case | Example (Regular, threshold = 2 days) |
|---|---|
| Below threshold | 1 day → $2.00 |
| At threshold | 2 days → $2.00 |
| One above threshold | 3 days → $3.50 |
| Well above threshold | 4 days → $5.00 |

**Why it matters:** Off-by-one errors in `>` vs `>=` only show up at the boundary. Testing
only "1 day" and "5 days" misses the most likely defect location.

**How to apply:** Use `@pytest.mark.parametrize` with all four cases. Treat the "at threshold"
case as a required row, not an optional one.

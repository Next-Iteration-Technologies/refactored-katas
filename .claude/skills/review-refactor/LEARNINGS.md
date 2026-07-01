# Learnings from Review & Refactor Sessions

Captures practices observed during live refactoring that are **not already covered** by
`core-standards.md` or `python.md`. Each entry is written to generalize beyond the code it was
first observed in — apply it wherever the same shape of problem shows up.

---

## 1. Write Characterization Tests Before Any Refactoring

**What:** Before touching any production code, write a comprehensive test suite that locks in the
current observable behavior — value boundaries, output format, edge cases (e.g. empty input),
ordering, and one golden-path integration test.

**Why it matters:** Refactoring without a safety net only confirms the code still compiles, not
that it still behaves correctly. Characterization tests make every subsequent refactoring step
verifiable in seconds.

**How to apply:**
- Cover every public method's output, not just the happy path.
- For threshold-based logic, always test: below-threshold, at-threshold-boundary,
  one-above-threshold, and well-above-threshold.
- Include one full integration test that asserts the complete output end-to-end.
- Write these tests *before* the first refactor commit; they are the contract, not a bonus.

---

## 2. Follow a Safe, Small-Step Refactoring Sequence

**What:** When refactoring a class with multiple intertwined responsibilities, apply changes
in this order, and within each step take one smell → one move → re-run tests, rather than
batching several moves together:

1. Characterization tests (lock in behaviour)
2. SRP — extract focused private methods
3. OCP — replace switch/if-elif chains with polymorphism
4. Magic numbers — extract named constants
5. Tell, Don't Ask — add delegation methods to eliminate message chains
6. Type hints — annotate once the design has settled

**Why it matters:** Each step is safe only because the previous step's tests are in place.
Doing type hints or constants before SRP means annotating code that is about to be restructured.
Verifying after every individual move — instead of batching several — is what makes it safe to
trust the result without re-reading the whole diff by hand.

---

## 3. Extract Superclass Only for Duplicated Behavior, Not Duplicated Shape

**What:** If two sibling types have identical logic and differ only in a handful of
configuration values, pull the logic into a shared base and let the subtypes supply just the
values.

**Why it matters:** Extracting a base class works only when the bodies are actually identical.
Applying it because two types merely *look* similar forces unrelated formulas into one base
class and trades one smell (duplication) for another (a base class that doesn't represent a
real shared concept).

**How to apply:** Before extracting, diff the two method bodies line by line. If every line
matches except for a value that could be a constant, extract. If any line differs in shape
(different operations, different branching), leave them separate.

---

## 4. Use `abc.ABC` + `@abstractmethod` When Replacing Switch Statements with Polymorphism

**What:** In Python, when eliminating an `if/elif` chain that switches on a type code, make the
base class abstract using `abc.ABC` and mark the varying methods with `@abstractmethod`.

```python
from abc import ABC, abstractmethod

class Base(ABC):
    @abstractmethod
    def compute(self, *args) -> float: ...

    @abstractmethod
    def classify(self, *args) -> int: ...
```

**Why it matters:** A style guide may say to use polymorphism but not prescribe the mechanism.
Without `ABC`, a new subclass that omits a required method silently returns `None` at runtime
instead of failing loudly at definition time.

---

## 5. Name Every Literal When Replacing Magic Numbers

**What:** When extracting named constants, scan the *whole* expression rather than stopping at
the first literal you notice.

**Why it matters:** A well-named constant sitting next to unnamed literals is a signal, not a
finished job — if a threshold has a name, the values it produces or compares against usually
deserve one too (e.g. don't name `MIN_DAYS` while leaving the `1` and `2` it selects between
unnamed).

**How to apply:** After adding one named constant to an expression, re-read the same line for
any remaining bare numbers or strings before moving on.

---

## 6. Extract Variable When an Expression Does More Than One Thing

**What:** If a line mixes two operations (e.g. a subtraction feeding a multiplication, or a
comparison feeding a boolean op), pull the inner sub-expression into a named variable.

**Why it matters:** A named intermediate value lets the outer expression read as a single idea
instead of requiring the reader to parse operator precedence.

**How to apply:** Identify the innermost sub-expression, assign it to a variable named for what
it represents (not how it's computed), then use that variable in the outer expression.

---

## 7. Prefer Extract Method Over Extract Variable for Business-Rule Conditions

**What:** When a boolean or derived expression represents a business rule (a threshold check,
an eligibility condition), promote it to a private method rather than a local variable.

**Why it matters:** Methods outlive the function they're declared in, are independently
testable, and can be reused — a local variable's meaning is scoped to one function and
disappears the moment that function grows or gets split.

**How to apply:** If you're about to write `is_valid = <expression>` purely to name the
expression (not to reuse the value more than once locally), write `_is_valid(...)` as a method
instead.

---

## 8. Keep Constant Names Consistent Across Sibling Subclasses

**What:** When multiple subclasses share conceptually equivalent constants, use the same name in
all of them — even if the values differ.

```python
# Good — reader can compare subclasses side-by-side
class VariantA(Base):
    THRESHOLD = 2
    RATE = 1.5

class VariantB(Base):
    THRESHOLD = 3
    RATE = 1.5
```

**Why it matters:** Inconsistent names (e.g. `THRESHOLD` in one class, `LIMIT` in another) force
readers to mentally map equivalent concepts. Sibling classes should read in parallel.

**How to apply:** After extracting constants, scan all sibling subclasses and align names for
any constant that plays the same business role.

---

## 9. Establish a One-Way Import Chain Before Adding Type Hints

**What:** When adding type annotations across multiple related classes, map the dependency
direction first and ensure it is strictly one-way before adding any imports.

```
Base  ←  Middle  ←  Root
```

Each module imports only from the layer below it; the base layer imports nothing above it.
No circular dependency is introduced.

**Why it matters:** Adding `from x import Y` for a type hint can silently introduce a circular
import that only surfaces at runtime. Mapping the chain first prevents this.

**How to apply:** Draw the dependency arrow for every new import before writing it. If an arrow
would create a cycle, use `from __future__ import annotations` (deferred evaluation) or a
`TYPE_CHECKING` guard instead.

---

## 10. Check for New Middle Men After Every Refactor Step

**What:** After moving logic to a more appropriate class, audit the original class for methods
that have become single-line delegators with no added value and remove them.

```python
# Before moving logic to the owning class — these made sense:
def _calculate_x(self, item): ...
def _calculate_y(self, item): ...

# After — they are Middle Men; delete them and inline the call at the caller
```

**Why it matters:** A code-smell checklist may list Middle Man as a smell, but it is easy to
miss newly-created Middle Men that did not exist before the refactor. Each refactor step can
introduce them in the class that *lost* responsibility.

**How to apply:** After every refactoring step, re-read the changed class top-to-bottom and
flag any method whose entire body is a single delegation call.

---

## 11. Parametrize Boundary Tests for Threshold-Based Rules

**What:** For any rule with a free/threshold value that changes behavior above and below it,
always parametrize with these four cases:

| Case | Description |
|---|---|
| Below threshold | Behavior matches the "before" branch |
| At threshold | Boundary value — confirms `>` vs `>=` is correct |
| One above threshold | First value that triggers the "after" branch |
| Well above threshold | Confirms the "after" branch scales correctly |

**Why it matters:** Off-by-one errors in `>` vs `>=` only show up at the boundary. Testing
only a low value and a high value misses the most likely defect location.

**How to apply:** Use `@pytest.mark.parametrize` (or the equivalent in your test framework)
with all four cases. Treat the "at threshold" case as a required row, not an optional one.

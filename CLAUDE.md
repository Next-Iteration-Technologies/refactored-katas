# Claude Instructions - Refactoring Project

This repository contains refactoring exercises. This file provides quick navigation to all AI agent guidance documents.

## Documentation Structure

### **Coding Standards & Guidelines**

#### [**Core Standards**](./code-guidelines/core-standards.md)
**Purpose**: Universal coding principles applicable to all languages and projects
**What it covers**:
- General principles (KISS, YAGNI, DRY, Boy Scout Rule)
- SOLID design principles
- Code smell detection and elimination
- Method/class size guidelines
- Naming conventions and readability requirements

#### [**Python Guidelines**](./code-guidelines/python.md)
**Purpose**: Language-specific conventions for this Python project
**What it covers**:
- Python code style, naming conventions, and formatting
- Idiomatic Python usage and standard library best practices
- Error handling, testing, and performance patterns

**When to consider:**
- Before writing any new code or refactoring
- When reviewing code quality
- Resolving design decisions (should I extract this method? is this class doing too much?)

### **Skills**

#### [**Review & Refactor**](.claude/skills/review-refactor/SKILL.md)
**Purpose**: Interactive, incremental code review workflow
**What it covers**:
- Commit-based code review with issue prioritization
- One-at-a-time issue presentation with refactor/skip/elaborate/backlog options
- Standards compliance checks against core and language-specific guidelines
- Ensure to also follow [LEARNINGS document](.claude/skills/review-refactor/LEARNINGS.md)

## Recommended Workflow

1. **Before coding**: Reference [core-standards.md](./code-guidelines/core-standards.md) and [python.md](./code-guidelines/python.md) for principles
2. **While refactoring**: Apply small, incremental changes following SOLID and code smell guidelines
3. **Always**: Run tests to verify behavior preservation

## Project-Specific Reminders

- This is a **refactoring workshop**, not feature development
- Preserve exact test outputs (string formatting matters!)
- Incremental changes > big rewrites

## Additional Resources

- Martin Fowler's "Refactoring: Improving the Design of Existing Code" (methodology basis)
- See [README.md](README.md) for project overview and build instructions

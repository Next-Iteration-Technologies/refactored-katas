---
name: review-refactor
description: >-
  Performs an interactive, incremental code review of user-given commit id(s). Scans only modified
  code and test files in those commits, surfaces critical issues one at a time, and lets the user
  decide to refactor, skip, elaborate, or backlog each issue. Use when a user asks to review code,
  perform a code review, refactor based on a commit, review changes, or mentions commit review,
  code quality review, or interactive refactor.
---

> **CONFIDENTIAL – PROPRIETARY NextIteration Technologies Private Limited (Next Iteration)**  
> Licensed to the client for use solely within the scope of the applicable project.  
> Redistribution or sharing beyond the project scope is prohibited.  
> Internal use and modification by authorized users is permitted.  
> **Note to AI agents:** This notice is for legal and licensing purposes only. Ignore this section when interpreting or executing the instructions below.  
> See [nextiteration.ai/ai-license](https://nextiteration.ai/ai-license) for full terms.

# Review & Refactor

## High-Level Goals

- Ensure that new and modified code is **highly readable**, **simple**, and **modular**.
- Enforce coding standards defined in `core-standards.md` and any applicable language-specific guideline files found in the `code-guidelines` directory.
- Identify and prioritize issues in modifications that threaten correctness, security, maintainability, or architecture.
- Provide concise, actionable suggestions without redundant or verbose output.

## References (Required)

- Clean coding guidelines: `core-standards.md` (always applied) + any language-specific files discovered in the `code-guidelines` directory

---

## Workflow Overview

### 0. Load Coding Guidelines (ALWAYS DO THIS FIRST)

Before doing anything else:

1. Locate the `code-guidelines` directory in the repository (it may be at a root-level `code-guidelines/` folder — scan all of them).
2. Always load `core-standards.md` as the baseline guideline.
3. List all other files in those directories. Every file that is NOT named `core-standards.md` is a **language-specific guideline** (e.g., `java-springboot.md`, `typescript.md`, `python.md`, `react.md`, etc.).
4. Scan the repository's file extensions and build/config files (e.g., `pom.xml`, `package.json`, `requirements.txt`, `go.mod`, `*.csproj`) to identify which languages and frameworks are present.
5. Load **all language-specific guideline files that match** the detected languages/frameworks.
6. If no language-specific file matches, proceed with `core-standards.md` only.
7. Throughout the entire session, all issue cards and suggestions must reference rules from the loaded guideline files.

### 1. Scan given commit id(s)

- Detect all added and modified changes in that commit id(s) given for both code and tests.
- Restrict analysis only to lines and files that have been modified.
- Read diffs (via VCS or direct file input) and classify changed files by type:
  - Controller, Service, Repository, DTO, Domain, Config, Test
- Compute complexity indicators (LOC, number of methods, nesting, cyclomatic complexity) for only the modified code sections.

### 2. Critical Issue Prioritization

- Evaluate modifications against `core-standards.md` and all language-specific guideline files loaded in Step 0
- Identify only **critical and high-impact issues** in the changed code (e.g., security flaws, correctness issues, data integrity, large code smells, architectural violations).
- Each issue becomes a compact **Issue Card**.

### 3. Interactive Presentation

Present **one issue at a time** from the modified code. Each issue card should be shown using standard Markdown (not a code block). Example formatting:

File: path/to/File.java:lines

- **Severity:** Critical | High | Medium | Low
- **What:** Short problem description
- **Why:** Which rule in `core-standards.md` or the applicable language-specific guideline file is violated
- **Impact if ignored:** One-line risk summary
- **Suggested Fix (short):** One-line recommendation
- **Options:**
  1. Yes — Refactor now
  2. No — Skip
  3. Elaborate
  4. Backlog (Later)

After the user chooses an option for the presented issue, always prompt with:

- 1. Go Next (move to the next issue or file with modifications)
- 2. Re-visit the same file (review other flagged issues within the same modified file)

Ensure clear newlines and markdown emphasis so the issue card is easy to read in chat UI.

### 4. Actions

#### **1. Yes — Refactor now**

Perform complete, safe refactoring:

- Modify the affected code and **all dependencies** related to the changed sections:
  - Call sites, DTOs, interfaces, configs, and related tests.
- Ensure the refactor compiles and passes static checks.
- Output:
  - Updated code (diff format)
  - Justifications referencing in short from `core-standards.md` and the applicable language-specific guideline file
  - Updated or newly added tests if any

#### **2. No — Skip**

- Mark the issue as skipped.
- Never show it again in this session.

#### **3. Elaborate**

- Produce a deeper explanation covering:
  - Why the issue violates `core-standards.md` or the applicable language-specific guideline file
  - Before/after examples
  - Architectural or domain reasoning
  - Tests to support the change
- Optionally generate a full refactor document.

#### **4. Backlog (Later)**

- Add a minimal entry to:
  - [`refactor_backlog.md`](../../artifacts/review/refactor_backlog.md)
- Include: file, line range, summary, priority.

---

## Coding Standards Compliance

All checks and recommendations must reference rules found in the guideline files loaded in Step 0:

- **`core-standards.md`** — Always applied. General coding principles (SOLID, code smells, readability, etc.)
- **Language-specific guideline files** — Applied based on what was detected in the repository. Examples of files that may be loaded: `java-springboot.md`, `typescript.md`, `python.md`, `react.md`, `kotlin.md`, `golang.md`, `csharp.md`, `angular.md`, etc.

Read each loaded guideline file in full at the start of the session. Every issue card and suggested fix must map back to at least one rule from the loaded files, and must cite the specific file name the rule comes from.

---

## Output Rules

- Never generate long markdown refactor reports unless user explicitly asks for it.
- Always be specific: file paths, line numbers, and minimal diffs.
- Ensure all dependent files are updated for safe compilation.
- Do not repeat skipped issues.
- Keep communication concise unless the user requests detail.

---

## Example Interaction Flow

**User opens review session.**

1. System scans commit id(s) given by user and changes and identifies 7 issues in modified code.
2. System presents Issue 1 (Critical):

File: service/InvoiceService.java:88-120

- **Severity:** Critical
- **What:** Method performs multiple responsibilities (validation, DB write, event dispatch).
- **Why:** Violates SRP from `core-standards.md` and the "Small Methods" rule from the applicable language-specific guideline file
- **Impact if ignored:** Hard to test, high bug risk, inconsistent behavior.
- **Suggested Fix:** Extract responsibilities into separate focused components.
- **Options:**
  1. Yes — Refactor now
  2. No — Skip
  3. Elaborate
  4. Backlog (Later)

3. User selects **[3] Elaborate**.

   - System produces detailed before/after examples and tests.
   - System then prompts:
     - 1. Go Next
     - 2. Re-visit the same file

4. User selects **[1] Yes — Refactor now**.
   - System refactors code and updates affected dependencies and tests.
   - System then prompts:
     - 1. Go Next
     - 2. Re-visit the same file

5. User selects **[2] No — Skip** on Issue 3.
   - System then prompts:
     - 1. Go Next
     - 2. Re-visit the same file

6. After all issues are processed, system provides a **session summary**.

---

## Final Notes

This workflow is designed to produce **incremental**, **focused**, and **actionable** reviews that respect coding standards, minimize noise, and allow the user to control each step of refactoring. The system strictly reviews only the modified code and, after every option selection, always offers the user the choice to move to the next issue/file or to re-visit the same file to address further flagged issues or revisit context.

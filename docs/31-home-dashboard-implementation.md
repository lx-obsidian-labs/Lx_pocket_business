# 31 — Home Dashboard Implementation Record

Status: implemented in the Android debug build; pending release validation.

## Purpose

This record documents the production-oriented Home dashboard implemented in `app/src/main/java/com/obsidianlabs/lxpocketbusiness/ui/screens/HomeScreen.kt`. It supersedes the earlier three-card prototype layout and makes Home the owner's daily command center: financial position first, then actions, then real recent records.

Traceability: docs 14, 16, 21, 22, 25, 26 and 29, plus owner design audit requirements.

## Design rationale

- Net cash movement is the dominant value because the owner must understand actual cash performance before navigating elsewhere.
- Money in and money out remain visible as supporting metrics inside one summary card instead of repeating as separate cards.
- Outstanding debt is visually separate from cash received and carries an explicit as-of date.
- Quick actions are equal-width, equal-height controls with concise labels and one consistent lime accent.
- Needs attention is driven by actual tasks, overdue debts and open quote follow-ups; no generated or demonstration records are created.
- Recent activity is derived only from stored cash events, debts and documents.
- The header shows the registered business name with a private-stored logo or name initials; it does not invent a logo.
- Advertisements are not integrated in this build. No reserved ad space is shown when ads are unavailable, satisfying docs 21 and 29 without introducing an SDK.

## Component hierarchy

```text
LxPocketBusinessApp
└── Scaffold
    └── HomeScreen
        ├── BusinessAvatar
        ├── Overview title + PeriodFilter dropdown
        ├── Custom period AlertDialog
        ├── Financial summary Card
        │   ├── Net cash movement
        │   ├── Money in / Money out MetricCell pair
        │   └── Refund line when applicable
        ├── Outstanding debt Card
        ├── Quick actions Row
        │   ├── Money in
        │   ├── Money out
        │   └── Record debt
        ├── Needs attention Card
        │   ├── AttentionRow (task/debt/quote)
        │   └── caught-up empty state
        └── Recent activity Card
            ├── ActivityRow (cash/debt/document)
            └── empty state with Record transaction action
```

## State and data flow

- `AppRepository` is the single source of truth. It exposes `StateFlow` values and computes dashboard data in `getDashboardData(...)`.
- `LxPocketBusinessApp` collects repository flows, stores navigation/selection state with `rememberSaveable`, and passes `DashboardData` into `HomeScreen`.
- `HomeScreen` is stateless regarding ledger data. Its only local state is UI state for dropdowns, custom date entry and scroll position.
- Persistence uses Room version 1 through `WorkspaceDatabase` and `RoomWorkspaceStore`. Related changes are written in one transaction; foreign keys and unique indexes are sanitized before insert.
- Onboarding progress and terms acceptance are persisted in the versioned workspace snapshot.

## Financial calculation rules

- Amounts are integer minor units. `formatMoneyMinor` uses `BigDecimal` and never divides by a binary floating-point value for display.
- Periods use local civil dates and ISO `yyyy-MM-dd` comparison:
  - Today: current day.
  - This week: Monday through current day.
  - This month: first day of month through current day.
  - Last month: complete previous calendar month.
  - Custom: inclusive start/end entered by the user.
- `periodEndExclusive` is retained for half-open interval semantics.
- Money received = `RECEIPT + SETTLEMENT` events in the selected period.
- Money spent = `EXPENSE` events in the selected period.
- Refunds are tracked separately and subtracted from net movement.
- Net cash movement = received − spent − refunds. It is explicitly labelled “Not profit.”
- Owner contributions and withdrawals are excluded from operating movement and tracked separately in the summary model.
- Customers owe you is an as-of balance. Principal incurred by the as-of date minus settlement events effective by that date, floored at zero. The displayed as-of date is the earlier of the selected period end and today, so a current period never claims a future balance.
- Invoiced debt is not cash until a settlement event is recorded.
- Needs attention always uses today's date, independent of the selected financial period.
- Dirty or legacy references are sanitized during Room writes so dangling customer/debt/document IDs do not crash persistence.

## Screen states

- Loading: milestone-based splash driven by `repository.startupStage`; no artificial delay.
- Fresh workspace: computed `R0.00` values, “No activity yet”, “Your activity will appear here.” and “Record your first sale or expense.” No seeded records.
- Populated: real summary metrics, attention items sorted by due date and up to five recent records sorted by actual record time.
- Empty attention: “You're all caught up”, “Nothing to follow up today.” and an explanation that reminders and tasks will appear there.
- Storage error: blocking message with Retry, backed by `repository.retryLoading()`.
- Custom period: validated `YYYY-MM-DD` dialog with an inline error and explicit Apply/Cancel actions.

## Visual and accessibility implementation

- Palette: `#C6FF45`, `#A8EF35`, `#292C1B → #181D16 → #111710`, `#252B20`, `#F4F5EC`, `#B9C1AF`, `#FFB4AB`, `#F2C879`.
- 4dp spacing grid, 16dp horizontal margins, 12/16/24dp vertical gaps and 16dp card radius.
- Financial values use `tnum` font feature settings and `sp` text sizes.
- Icons are paired with text labels; colour alone never communicates money direction or urgency.
- Quick actions and navigation use 48dp-plus Material touch targets and TalkBack content descriptions.
- Content scrolls above the persistent five-destination bottom bar insets.

## Acceptance criteria

- [x] Home immediately shows net cash movement, money in, money out and customers owe you.
- [x] Net movement is never labelled profit or bank balance.
- [x] All values come from real local records; no demo data is seeded.
- [x] Period selector supports Today, This week, This month, Last month and Custom.
- [x] Debt as-of date never exceeds today for current periods.
- [x] Quick actions open the existing capture workflows.
- [x] Needs attention uses real tasks, debts and quote due dates and shows a calm empty state.
- [x] Recent activity uses real ledger/document records and shows an empty-state action.
- [x] Business header uses the real business name with logo/initials fallback.
- [x] Bottom navigation preserves Home, Customers, Money, Documents and More.
- [x] Loading, empty, populated and recoverable error states are represented.
- [x] Build, unit tests and lint pass locally.
- [ ] AdMob banner, consent flow and ad-a11y tests remain future work.
- [ ] Encrypted backup/restore remains unimplemented.
- [ ] Final publisher terms, privacy route and SARS template certification remain release gates.

## Verification record

Executed with `JAVA_HOME=C:\Users\KAIRO VILANE NATHAN\.jdks\openjdk-24.0.2+12-54`:

```powershell
& ".\gradlew.bat" :app:assembleDebug :app:testDebugUnitTest :app:lintDebug --console=plain
```

Result: `BUILD SUCCESSFUL`. Lint report: `app/build/reports/lint-results-debug.html`.

Device verification used the connected `25028RN03A` device. The APK was installed with `adb install --no-streaming -r`, launched with `am start -W -n com.obsidianlabs.lxpocketbusiness/.MainActivity`, and the UI hierarchy confirmed the business header, Overview, period selector, financial summary, outstanding debt, quick actions and bottom navigation. No app-process AndroidRuntime or AppRepository errors appeared after clearing stale logcat entries and relaunching.

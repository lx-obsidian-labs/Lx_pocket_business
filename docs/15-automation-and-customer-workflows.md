# 15 — Customer lifecycle and automation
2026-11-18 | Candidate design for M02 M04 M05 M06 M07 M09
## Customer creation
1. From Home quick action, quote or debt form, search by normalized name or phone; existing matches displayed first.
2. Add customer: display name required, phone/email optional, business/address/VAT info under More (required only when applicable to formal document).
3. Optional select individual phone/email via system contact picker; review before save; no bulk contacts permission, no automatic sync. Normalize phone to canonical form where possible, preserve entered display; warn duplicate phone/email/name but allow shared numbers after confirmation.
4. Save transactionally, return selected ID and retain originating draft; show customer overview with balance, documents, activity and next action. No customer creation for anonymous cash sale.
## Trigger/action contract
| Event | Condition | Result | Owner control |
|---|---|---|---|
| customer created | explicit save | offer quote, debt or follow-up; no external communication | dismiss |
| quote issued | valid expiry | expiry task; optional follow-up before expiry | configure/cancel |
| quote accepted | manually recorded | suggest invoice draft; no auto-issued tax document | review/issue |
| invoice issued or debt recorded | open balance and due date | create/update due task; default reminder offsets proposed -3, 0, +7 days | enable/change/snooze |
| partial payment | amount valid | refresh balance and reminder; receipt preview | share explicitly |
| full settlement/credit | balance zero | cancel pending due reminders and close debt task | view history |
| expected payment promised | date set | owner follow-up on date | reschedule |
| recurring invoice template due | active and occurrence absent | create one draft, notify owner | approve, skip, pause |
| recurring expense due | active | create task to confirm actual expense/payment | confirm, skip |
| month ends | recorded activity | prepare month summary from immutable source calculations; label revision if data changes | export/review |
| backup stale | threshold exceeded | in-app task and optional notification | choose destination |
## Technical model
Schedule(id, business_id, type, target_id, rule, anchor_local_date, zone_id, next_occurrence_date, enabled, revision); Task(id, trigger_id, occurrence_key unique, due_date, status OPEN/SNOOZED/DONE/CANCELLED, reason, related_entity); AutomationEvent(event_id, entity_id, event_type, committed_at, processed_at); RecurrenceTemplate(id, customer_id, line snapshot, frequency, anchor_date, end_date?, active); Occurrence(template_id, occurrence_date, unique composite, draft_id?, state). Store IDs, not customer details in worker inputs/notification payload. All relevant state changes and outbox event in same Room transaction. Reconcile tasks based on current DB state; workers can retry without duplicate drafts or financial transactions. Ledger remains source of truth; tasks never themselves record money. Use calendar months, clamp 29–31 to last valid day while retaining original anchor; define timezone change behavior and test.
## Scheduling implementation
Android WorkManager unique periodic reconciliation plus one-time work for pending tasks; in-app launch reconciliation is mandatory. No exact-alarm permission for ordinary business reminders; Android may delay background tasks under battery restrictions/force-stop, device off or notification denial. Notification permission on Android 13+ requested only after user opts into reminders; denial leaves full in-app queue. Use generic lock-screen notifications; no customer names/amounts by default. Deep link to task requiring app unlock. Re-check open balance before displaying. Rebuild pending tasks after restore; no external messages. Mark reminder as prepared/notified, not delivered to customer. Share via chooser with editable template and owner confirmation; no accessibility automation or SMS permissions.
## Edge cases and tests
Double tap and worker retry: one occurrence. Full payment before scheduled execution: no new debt reminder. Partial payment: latest outstanding shown. Pause/edit recurring template: obsolete tasks cancelled; previously issued docs unchanged. Missing several months while phone off: show missed occurrences for review, never auto-post. Device reboot, DST, timezone shift, month-end/leap date, notification denied, locked screen, permission revoked, force-stop and launch catch-up. Restore old backup warns of stale reminders and sequence risks. Verify worker constraints on physical devices; no exact-time claims.
## Primary technical sources verified
https://developer.android.com/guide/components/intents-common — selected contact data URI.
https://developer.android.com/develop/background-work/background-tasks/persistent — persistent work.
https://developer.android.com/develop/background-work/background-tasks/persistent/how-to/manage-work — unique work.
https://developer.android.com/develop/background-work/background-tasks/persistent/getting-started/define-work — scheduling and constraints.
https://developer.android.com/develop/background-work/services/alarms — prefer inexact scheduling.
https://developer.android.com/develop/ui/compose/notifications/notification-permission — notification permission.
https://www.zoho.com/za/invoice/help/settings/reminders.html — competitor already offers automatic customer/owner reminder rules.

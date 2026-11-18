# 08 — UX and interaction design
2026-11-18 | design specification; no screens built
## Navigation
Bottom destinations: Home, Documents, Customers, Money, More. Documents tabs Quotes/Invoices/Credits; Money tabs Payments/Expenses/Reports; More Catalogue, Business profile, Backup, Legal, Support. Global create action offers quote/invoice/customer/expense; avoid overlay near ad. Tablet adapts to navigation rail and list-detail. Search and filters preserve state.
## Journey 1: first launch
Language/theme defaults → concise privacy/terms → create business or restore backup → enter basic name/address, VAT status and document numbering → dashboard. Optional logo and tax details editable later; block issuing until required fields present. No forced login, ad or network.
## Journey 2: quote to cash
Customer selection/create → line editor with catalogue/manual item → quantity/discount/tax → live total and due/expiry → preview → save draft/issue quote → share PDF through chooser → manually mark sent/accepted → convert to invoice with snapshot and review → issue immutable serial → share → record partial/full payment with reference → receipt/payment history → reports. Display error inline, preserve drafts on back/rotation/process death. Prevent duplicate taps. Confirm issue and corrections.
## Journey 3: expenses and month end
Add expense → category/supplier/date/amount → optional receipt via picker → confirm whether paid → filter expense list → reports period → drill down to source rows → export CSV/PDF to chosen location. Show empty state and definitions. Avoid implying cash expense when payment not recorded.
## Journey 4: backup and restore
Settings → Backup → password confirmation and destination → success only after integrity verification → last successful timestamp. Restore → choose file → authenticate → validate → show counts/identity → confirm replace → rollback on failure. Explain uninstall and forgotten password risk.
## Design system proposal
Professional restrained business interface, light and dark modes, brand black/blue accent, clear typography, money tabular alignment, large readable totals, 48dp touch targets, contrast and TalkBack, font scaling to 200%, no colour-only status. Loading/error/empty states per screen. ZAR formatting and local dates; avoid ambiguous numeric dates in PDFs. No ads on transactional or sensitive screens. Test with low-end phones, tablets and users with accessibility needs.
## Screen acceptance
Every create/edit form validates, saves safely and handles empty data; every list has search/filter/archive where relevant; all destructive actions explained/confirmed; no hidden navigation or unreachable back action. PDF templates paginate long lists, include page numbers, Unicode names, totals, terms and tax fields; no advertisements in exports.

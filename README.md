# LX Pocket Business

**Status: Research and planning — no application implemented or published.**
A free, ad-supported, offline-first Android business productivity app for South African freelancers and small businesses. Core functionality requires no account, internet connection, or paid API.

## Documentation — read in order
| Document | Purpose |
|---|---|
| [01 — Product requirements](docs/01-product-requirements.md) | Feature scope, personas, acceptance criteria, exclusions |
| [02 — Market and validation](docs/02-market-and-validation.md) | Alternatives and discovery tests |
| [03 — Data and financial rules](docs/03-data-and-financial-rules.md) | Entities, state transitions, monetary invariants, tax |
| [04 — Architecture and dependencies](docs/04-architecture-dependencies.md) | Android design and candidate libraries |
| [05 — Security and privacy](docs/05-security-privacy.md) | Threat model, backup, storage, data boundaries |
| [06 — Play, website and ads](docs/06-play-website-ads.md) | Dual distribution, signing, AdMob and compliance |
| [07 — Legal policy specification](docs/07-legal-policy-specification.md) | Terms, privacy, licences and review gates |
| [08 — UX workflows](docs/08-ux-workflows.md) | Navigation, screen and accessibility requirements |
| [09 — Delivery and test plan](docs/09-delivery-and-test-plan.md) | Development phases, tests, release gates |
| [10 — Source register](docs/10-source-register.md) | Confirmed official references and remaining checks |
| [11 — Problem evidence and review audit](docs/11-problem-evidence-and-review-audit.md) | National evidence, local alternatives and differentiation hypothesis |
| [12 — Free competitors and AI feasibility](docs/12-free-competitors-and-ai-feasibility.md) | Free pricing, AI reporting and NVIDIA production constraints |
| [13 — Problem register and review evidence](docs/13-problem-register-review-evidence.md) | Stable B/E/U identifiers, dated review observations and explicit owner requirements |
| [14 — MVP product definition](docs/14-mvp-product-definition.md) | Revised daily-first scope, measurable criteria and prioritization |
| [15 — Automation and customer workflows](docs/15-automation-and-customer-workflows.md) | Customer entry, trigger/action rules, scheduling and tests |
| [16 — MVP screen specification](docs/16-mvp-screen-specification.md) | 26 proposed screens, routes, actions, states and workflow map |
| [17 — Splash screen and motion design](docs/17-splash-screen-motion-design.md) | Owner-defined logo pulse, edge glow, spin and exit transition |
| [18 — Welcome, onboarding and Firebase handover](docs/18-welcome-onboarding-and-firebase-handover.md) | First-run flow, local state, no mandatory login, owner-managed Firebase integration |
| [19 — Brand colour and welcome visuals](docs/19-brand-colour-and-welcome-visuals.md) | Lime accents, dark olive gradient, numeric progress and artwork direction |
| [20 — Terms acknowledgement and first-run flow](docs/20-terms-acknowledgement-and-first-run-flow.md) | Scrollable terms, explicit acceptance, local record and first-run gate |

## Current product direction — proposed, not yet validated
Start with the problem: owners without consistent records cannot readily understand cash movement, spending and customer debt. The original invoice-first emphasis in documents 01–10 is historical; documents 11–16 propose a daily-first MVP, subject to validation and owner review. Begin with simple Money In, Money Out and Someone Owes Me; add customer reuse, due actions, formal documents and monthly packs without double counting. No subscriptions. Advertising must never obstruct offline work or expose business records. AI and recurring drafts are candidates, not committed launch dependencies.

## Traceability
Read 13 → 14 → 15 → 16 for the latest proposal. Every new feature or screen must cite a problem ID (B), evidence ID (E) or explicit user requirement (U), a feature ID (M), and acceptance tests. Do not treat review anecdotes as representative evidence. Field interviews, competitor hands-on benchmarking, annotated wireframes and final technical/legal validation remain outstanding. No implementation until scope is approved.

## Decision gates
Read and approve requirements before implementing. Validate user demand, tax templates, dependency compatibility, security, and applicable Play/legal requirements before release. No credentials, real customer records, signing keys or production ad IDs in this repository. Documents are versioned individually; link future changes to requirement IDs and test evidence.

Maintained by [LX Obsidian Labs](https://www.lxobsidianportal.co.za/).

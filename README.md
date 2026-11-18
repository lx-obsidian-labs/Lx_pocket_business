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

## Initial product focus
Customer → quotation → invoice → manual payment → report/export. Expenses, catalogue, backup and restore complete the proposed first release. Advertising must never obstruct offline work or expose business records.

## Decision gates
Read and approve requirements before implementing. Validate user demand, tax templates, dependency compatibility, security, and applicable Play/legal requirements before release. No credentials, real customer records, signing keys or production ad IDs in this repository. Documents are versioned individually; link future changes to requirement IDs and test evidence.

Maintained by [LX Obsidian Labs](https://www.lxobsidianportal.co.za/).

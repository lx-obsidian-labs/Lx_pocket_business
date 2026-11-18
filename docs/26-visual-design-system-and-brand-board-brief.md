# 26 — Visual design system and brand board brief
2026-11-18 | Proposed specification for owner approval | U04–U10; C01–C12 from doc 25. No artwork or app code produced.

## Sequence and rationale
Evidence and product characteristics (13, 14, 25) → visual design system (this document) → visual brand board → screen wireframes → original artwork and production assets → implementation. The brand board communicates the design system visually; artwork is the set of individual illustrations, logos and graphic assets that implement it. They are related but distinct deliverables. Never generate final assets before approving a coherent visual direction.

## Brand identity
Personality: calm, approachable, capable, professional; financial clarity without accounting jargon. Audience: first-time recordkeepers and small-business owners, not necessarily accounting professionals. Voice: short sentences and familiar words, clear action verbs, no false promises or invented business data. Owner-defined lime accents and earthy dark olive/charcoal gradient (U08) differentiate visual identity, not proven market differentiation. Product brand independent from parent-company electric-blue branding; retain exact splash credit "powered by LX Obsidian Labs".

## Colour and surfaces (proposed tokens)
| Token | Hex | Use |
|---|---|---|
| Primary | #C6FF45 | Main CTA, selected navigation, progress |
| Glow | #A8EF35 | Splash halo only or subtle brand emphasis |
| BackgroundStart | #292C1B | Muted olive/brown-green gradient |
| BackgroundMiddle | #181D16 | Dark charcoal green |
| BackgroundEnd | #111710 | Near-black green |
| Surface | #252B20 | Cards, dialogs, sheets |
| TextPrimary | #F4F5EC | Main text |
| TextSecondary | #B9C1AF | Supporting text |
| OnPrimary | #111710 | Dark text on lime |
| ErrorCandidate | #FFB4AB | Error text/icons on dark surfaces |
| WarningCandidate | #F2C879 | Warning text/icons on dark surfaces |

Use the gradient on splash/welcome and optionally a restrained background on Home; content-heavy pages prefer solid, readable surfaces. No glow on every control, no neon body text, no large saturated gradients behind financial data. Error/warning choices are new proposals, not user-approved. Pair status colours with labels/icons; never use green/red alone for money or status. Check contrast against actual composited surfaces, including disabled states, large text and system bars. Target 4.5:1 for normal text and 3:1 for large text and meaningful control boundaries. Printed PDFs use white background, dark text, actual customer-business logo and restrained accents; no app watermark or dark dashboard styling.

## Layout and typography
Proposed spacing grid 4dp, common gaps 8/12/16/24dp; screen margins 16–24dp depending on available width; cards 16dp radius, primary buttons 14dp radius. Minimum interactive target 48dp. Start with system Roboto (no external font dependency); headings 24–28sp, section headings 18–20sp, body 16sp, supporting 14sp, captions 12sp where readable. Financial values 24–32sp with tabular numerals where supported; never truncate a monetary value without a way to read the full figure. Use scalable sp; allow 200% font scaling with vertical reflow. Avoid all-caps paragraphs and unexplained accounting abbreviations. No horizontal scroll for core actions on phone; use adaptive two-column/list-detail on wider displays. Sizes are candidates subject to wireframe and accessibility checks.

## Components and interaction
Primary action: lime fill and dark label; one clear primary action per focused form. Secondary: outlined or text action with visible label. Inputs: persistent labels, helpful defaults, optional fields marked, errors adjacent to input, numeric keypad where appropriate, no placeholder-only label. Cards: readable hierarchy, consistent units and source date, empty/error/loading distinct. Bottom navigation: Home, Customers, Money, Documents, More (doc 21; order proposed); icon + label, lime selected indicator; Today queue on Home. Forms/transaction screens no ads; banners only approved Home/Reports placement; never compete with CTA. Motion: splash pulse/spin and true percentage per 17/19, modest transitions elsewhere; reduced motion and no forced wait. Keyboard/back/rotation preserve work. Use vector icons with consistent outline weight and accessible labels; no custom icon pack needed unless licensed. No decorative chart on empty workspace.

## Evidence-to-visual decisions
| Evidence / characteristic | Design consequence |
|---|---|
| B01/E01/E02/C01 | Show useful money actions immediately, not a dense accounting dashboard |
| B02/B03/E02/E03/E04/C02/C03 | Plain-English copy, reusable customer/item entries, optional details, concise forms |
| B04/E04/C04 | Make remaining debt and partial-payment status readable at a glance; explicit amounts and dates |
| B05/E07/U02/C05 | Today action queue, clear reminder state and owner control |
| B06/E05/C06 | Verified backup status, clear recoverable errors, no false success |
| B07/E06/U01/C07 | No paywall or interruption; ad separation and honest labels |
| B08/E03/E04/U10/C08 | Professional PDF previews, logo sizing, reusable terms and accessible document actions |
| B09/E05/C09 | Visible period controls, reconciled report metrics and drill-down |
| B12/U05/C10 | Offline actions remain enabled; no mandatory login; no network-dependent onboarding |
| U04/U06–U10/C12 | Lime/olive brand, motion, artwork, legal acknowledgement and empty live workspace; owner preference not review evidence |

## Brand board deliverable (not yet generated)
One coherent visual reference: approved logo/wordmark and monochrome variant; exact colour swatches and gradient; typography hierarchy; icon style; button/input/card examples; splash and welcome composition samples; illustration direction; light print example. Include hex codes, component states and rationale. Approve board before production artwork. Avoid third-party competitor logos, unlicensed assets and generated text embedded in UI illustrations.

## Production artwork inventory
| Planned filename | Purpose | Proposed format |
|---|---|---|
| brand_mark_master.svg | Original scalable mark, source of truth | SVG/vector |
| brand_wordmark_master.svg | Product name lockup | SVG/vector |
| launcher_foreground.svg / launcher_background.svg | Adaptive launcher icon layers; safe-zone check | Vector |
| splash_mark.svg | Centered logo suitable for glow and rotation | Vector |
| welcome_hero.webp | Original approachable small-business owner scene, no embedded UI text | WebP, portrait-friendly |
| onboarding_money.webp | Money in/out benefit | WebP |
| onboarding_customers.webp | Customer debt and follow-up benefit | WebP |
| onboarding_reports.webp | Monthly clarity and documents benefit | WebP |
| empty_state_generic.svg | Optional quiet empty-state illustration, never fake data | Vector |
| brand_board source + preview | Approved visual system reference | Editable source + preview |
Asset filenames and count proposed; final onboarding panel count remains open. Generate consistent original illustrations only after visual approval; check transparency, small-size legibility, crop, file size and rights. Maintain asset manifest with filename, intended screen, dimensions, format, generation/source, rights and review status. Raster-generated logos require deliberate vector reconstruction and visual QA before claiming editable SVG source. Do not imply an illustration is a live dashboard.

## Design acceptance
Approve colour, logo concept, typography, visual board and sample screens with owner. Test first-time users on plain-language welcome, quick entry, debt and monthly summary; verify accessibility and actual contrast, one-handed use, small-screen/landscape/tablet layouts, empty and error states. Source for Android accessibility guidelines already registered in doc 24. Exact values remain proposals; no field testing, finished brand board or assets yet.

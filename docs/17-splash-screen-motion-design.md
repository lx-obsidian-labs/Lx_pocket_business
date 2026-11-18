# 17 — Splash screen and motion design
2026-11-18 | UI/UX specification | Owner requirement U04 | Not implemented

## Owner-defined experience
- Display the LX Pocket Business logo centrally (logo artwork to be generated and approved separately).
- At the bottom display the exact attribution: "powered by LX Obsidian Labs".
- On entry, pulse the logo with a luminous glow along its edges.
- On exit, the logo spins, disappears, and the splash page animates away to reveal the main landing screen.
- This is a branding and first-impression requirement, not a validated solution to a recordkeeping problem. Trace to U04 rather than inventing customer research.

## Proposed motion storyboard
| Stage | Proposed timing | Visual behavior |
|---|---|---|
| OS launch | System controlled | Matching background and centered adaptive app icon; no duplicate activity |
| Branded entry | 0–100 ms after first app frame | Center the full logo; footer attribution inside bottom safe inset |
| Pulse | Up to 500 ms | Logo scale 1.00 → 1.06 → 1.00; edge halo opacity 0.20 → 0.65 → 0.20; soft glow follows logo silhouette; no strobing |
| Spin and exit | Up to 300 ms when destination ready | Logo rotates clockwise up to 360°, scales down to 0.70 and fades to zero; footer fades; splash overlay translates upward and fades to reveal underlying content |
| Complete | Immediately after transition | Remove overlay; destination fully visible and interactive; no repeated splash on ordinary resume |

Timings, scale, clockwise direction and upward exit are proposed defaults for owner review, not fixed user requirements. Target a brief transition, not an artificial loading delay; skip or shorten animation when needed for startup responsiveness. No animated audio.

## Layout and asset requirements
Proposed background obsidian black #0B0B0F, accent electric blue #2E6BFF, footer high-contrast light text. Palette subject to approval. Center logo on available viewport, independent of footer; safe areas for cutouts/navigation; adapt to portrait, landscape and tablets. Footer text approximately 12–14sp, respects font scale and contrast. Separate adaptive launcher foreground/background, splash-safe vector mark, full brand logo, monochrome variant and source artwork. Logo artwork not yet created. Avoid cropping under Android system splash mask.

## Android implementation approach
Use AndroidX core-splashscreen installSplashScreen before Activity super.onCreate, matching system splash to Compose content. OS splash is intentionally constrained to icon/background; custom branded Compose overlay draws exact footer and glow over prepared destination. Avoid separate splash Activity and visually mismatched double splash. Compose Animatable/graphicsLayer for rotation, scale, translation and opacity; draw a bounded radial halo or pre-rendered glow to avoid expensive continuous blur. Use one state machine (initializing → ready → exiting → complete; recoverable error separate), cancel safely on lifecycle interruption. System exit listener must call remove when finished if used. Do not artificially keep system splash alive for fixed animation duration. No ad requests or network dependency in startup critical path.
Routing: first run → S01 Welcome (or setup flow); returning owner → S03 Home; optional app lock → authenticated destination; deep link → authorized target. Initialization failure reveals a recoverable error with retry/restore, not an endless animation.

## Accessibility and testing
Honor system animation duration scale and reduced-motion preference: static logo and immediate or minimal fade, no forced rotation/pulse. No rapid flashes. Footer accessible; logo decorative unless meaningful. TalkBack focus moves to destination once overlay removed. Test cold/warm launch, process death, rotation, app lock, system animations disabled, offline, slow DB initialization, low-end GPU, API 26 and API 36; measure startup with macrobenchmark before setting performance claims. Exit must occur once, overlay removed, no blank frame or blocked touches. Actual implementation and visual assets remain outstanding.

## Primary references (reviewed 2026-11-18)
- https://developer.android.com/develop/ui/views/launch/splash-screen — Android splash constraints, custom exit listener and icon animation timing.
- https://developer.android.com/reference/androidx/core/splashscreen/SplashScreen — compatibility API and icon safe area.
- https://developer.android.com/develop/ui/compose/animation/quick-guide — animation timing and graphicsLayer transforms.
- https://developer.android.com/develop/ui/compose/animation/customize — easing and keyframes.


## Owner refinement U06 — visible startup progress
On the branded Compose splash, beneath the centered pulsing logo and above the unchanged footer "powered by LX Obsidian Labs", show a slim horizontal progress indicator and plain-English status. Suggested states: "Getting things ready…", "Opening your business…", "Almost ready" (only when final readiness checks have begun). Keep footer in bottom safe area. Match the glow accent and maintain contrast. Layout is proposed; logo and palette still await approval.

Track actual startup milestones: local preferences loaded, business database opened/migrated, destination resolved, destination ready. If representing completed milestones, explicitly label the count (e.g. "Step 2 of 4"); do not present equally weighted stages as a reliable time estimate. Use determinate percentage only when measurable work units are available; otherwise an indeterminate bar and current status. No fabricated countdown, percentage, minimum wait or guaranteed ETA. Fast launches may complete before the indicator is visible. At readiness, fill the indicator if determinate, perform the brief pulse/spin/fade exit once and reveal Welcome or Home. If startup fails, stop the animation and show a recoverable Retry/Restore path; if unusually slow, show a reassuring truthful status without blocking forever. No network, Firebase or ad request in the startup critical path. Support disabled animation, accessible progress semantics, and avoid repeated announcements.

Acceptance: real progress never claims readiness early; cold and warm launch, slow migration, failed load, process death, offline startup and reduced motion verified; footer stays readable, exit never leaves user on a blank screen. Android's system splash itself is limited; the branded progress UI is in the first Compose frame, not an attempt to add arbitrary widgets to the OS splash.

Source checked 2026-11-18: https://developer.android.com/develop/ui/compose/components/progress — determinate versus indeterminate indicators; https://developer.android.com/develop/ui/views/launch/splash-screen — platform constraints and exit.

## Owner refinement U08 — lime theme and numeric progress
Use the lime/dark olive gradient tokens in [doc 19](19-brand-colour-and-welcome-visuals.md), superseding the earlier blue proposal. Display the percentage beside or under the bar (0–100% of completed defined startup milestones, not time remaining). Four milestones give 25% increments; 100% only after readiness. Do not fabricate progress or delay the transition. See doc 19 for palette and placement.

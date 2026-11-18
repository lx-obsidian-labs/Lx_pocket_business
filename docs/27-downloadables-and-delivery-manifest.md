# 27 — Downloadables and delivery manifest
2026-11-18 | Owner requirement U11 | Planned deliverables, not generated assets or releases.

## Requirement
The project must provide downloadable, reusable deliverables rather than only previews or chat references. Preserve editable source where appropriate and publish approved files in the correct GitHub repository. This applies to design handover, documentation, future application releases and user-created business documents.

## Design and artwork handover
| Planned deliverable | Format | Contents |
|---|---|---|
| LX_Pocket_Business_Brand_Board.pdf | PDF | Approved mark, colour values, typography, iconography, layout and visual examples |
| LX_Pocket_Business_Brand_Board.png | High-resolution PNG | Shareable visual reference |
| LX_Pocket_Business_Design_Guidelines.pdf | PDF | Component rules, spacing, colour/contrast, motion and accessibility |
| LX_Pocket_Business_Wireframes.pdf | PDF | Annotated screens and flows including empty, error and loading states |
| LX_Pocket_Business_Artwork.zip | ZIP | Individual named logo variants, launcher layers, splash mark, welcome and onboarding graphics, empty-state assets and manifest |
| LX_Pocket_Business_Design_Source.zip | ZIP | Actual editable vector/layout source where created, design tokens, and asset inventory; never claim a raster image is editable vector |
| LX_Pocket_Business_Research_Pack.zip | ZIP | Versioned Markdown research and planning documents, README index and source register |

Individual files remain accessible alongside archives; no flattening or loss of editable sources. Use SVG for genuine vector marks, PNG for transparent raster variants and WebP for optimized app illustrations; retain high-quality masters. Include filename, intended use, dimensions, format, source, rights, version and approval status. Exact artwork inventory in doc 26; final files require owner visual approval. Store small textual/vector sources in Git; use suitable release assets for large archives and binaries. Do not include signing keys, production secrets, real business records, or unlicensed fonts.

## App and website distribution
Publish only verified signed APK and versioned release notes/checksum through the official product download page and appropriate GitHub release assets. AAB for Play Console submission; not marketed as an installable APK. Verify signing identity and cross-channel upgrades (doc 06). Offer user guide and legal notices as downloadable PDFs where useful, keeping accessible HTML and Markdown versions as authoritative references. Website links must point to real published artifacts, not placeholder downloads.

## In-app downloads and exports
Actual user-created branded quotes, invoices, receipts, credit notes, customer statements and monthly reports downloadable/printable as PDF. Transaction data downloadable as CSV. Encrypted portable backup downloadable to a user-selected location. See docs 21–23 for financial and document semantics. No fictional records or advertisements in business exports; use actual business logo when supplied. Export/share/print remains user initiated.

## Acceptance
Before claiming an artifact exists: create it, open/validate it, verify filename/format, version and rights, ensure downloadable link resolves and record manifest entry. Test ZIP extraction, PDF readability, image dimensions/transparency and app-resource mapping. Approved release packages must have verified checksum and no secrets. Include direct links in project README/release notes when available. Do not publish unapproved designs as final.
 
Current status: specification only. No brand board, artwork archive, compiled APK or design PDF has been generated yet. 

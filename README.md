Wine Shop - Simple .NET project

Short description

- This repository is a small teaching project (Wine shop) implemented with multiple projects:
  - `WinFormsUI` - Windows Forms UI for manager and cashier flows
  - `BL` - Business Logic layer (BL)
  - `DalList`, `DalXML` - two DAL implementations
  - `DalFacade` - DAL factory and shared interfaces
  - `BlTest`, `DalTest` - small test/demo consoles

Requirements

- .NET 8 SDK
- Visual Studio 2022/2023 or `dotnet` CLI

How to build

- From the repository root run:
  - `dotnet build`
- Open solution in Visual Studio if you prefer the IDE.

How to run the UI

- Start the `WinFormsUI` project (set as startup project in Visual Studio) or run:
  - `dotnet run --project WinFormsUI\WinFormsUI.csproj`

Notes and recent changes

- Terminology: all places that previously used the word "Promotion" were renamed to "Sale" in the UI to match the BL (`PromotionForm` -> `SaleForm`, `Promotions` tabs -> `Sales`, related control names updated). No business logic was changed.

- There is a known compile error in `BL\BlImplementation\CustomerImplementation.cs` (duplicate `GetCustomerByParameter` definition). This existed in the BL project and is not caused by the promo->sale rename. To get a clean build you should fix the duplicate method (remove or merge the duplicate overload).

- Many projects have nullable/warning messages from the compiler (nullability warnings). They do not block build in most cases but are worth cleaning gradually.

Useful commands

- Build all projects: `dotnet build`
- Run WinForms UI: `dotnet run --project WinFormsUI\WinFormsUI.csproj`
- Run unit/demo projects: `dotnet run --project BlTest\BlTest.csproj` (or `DalTest`)

If you want, I can:
- fix the BL compile error now (remove/merge the duplicate method), or
- run a repository-wide search and rename any remaining `Promotion` usages that were missed.

Author

- Student project. Comments in code were adjusted to be short and student-friendly.

# Digital Auction Management System

Welcome to the **Digital Auction Management System**! This application is designed to simulate a robust system for managing users, admins, auction events, and bidding mechanisms using Java SE.

## 🚀 How to Run the Application (CLI)

Since the project uses a custom directory structure matching logical domains rather than strict Java package nesting rules, you need to compile all files into a unified `out` directory to link the UI and Backend manually.

### Compilation and Execution
Open your terminal inside the project root (`d:\Java_project\auction`) and run the following commands:

```powershell
# 1. Create the output directory (ignore errors if it already exists)
mkdir out

# 2. Compile every file from every subdirectory safely into the out directory
javac -d out core\*.java entities\*.java exceptions\*.java main\*.java services\*.java frontend\*.java

# 3. Launch the AppRunner class from the compiled output folder
java -cp out frontend.AppRunner
```

### Logging Into the UI
When the Swing Application pops up, use the mock user injected in the system to log in:
- **Email:** `alice@example.com`
- **Password:** `password`

---

## 🏛️ Project Architecture

This application is built with strong adherence to **Object-Oriented Programming principles** and **Model-View-Controller (MVC) logic**, loosely separated into domains:

### 1. The Core Infrastructure (`package com.auction.core`)
The backbone files that standardize database/internal models:
- **`BaseEntity.java`**: Adds abstract IDs and fields common among all entities.
- **`Authenticatable.java`**: Interface contract to guarantee `login()` operations on User and Admin models.

### 2. Form & Structure (`package com.auction.entities`)
The schema and model formats representing the business data without injecting business logic:
- `User` & `Admin`: Handles the different roles navigating the UI.
- `AuctionEvent`: Stores dates and arrays of items mapped to a specific online event.
- `Item`: The product being sold, complete with Starting Prices and references to its parent Category/Auction.
- `Category`: Used to filter items.
- `Bid`: A user's financial commitment linked to an item.
- `Payment`: A logged receipt showing a finalized and approved auction purchase.

### 3. Business Logic (`package com.auction.services`)
Where all computations safely modify database arrays and lists:
- `AuctionManager.java`: Interface dictating public functionalities.
- `AuctionSystem.java`: Implements array-modifications, validation of bids, handling login matching, and automatically moving money tracking data.

### 4. Custom Error Handling (`package com.auction.exceptions`)
Graceful exceptions handling to prevent application termination:
- `AuctionException`: Triggers when data attempts to pull from inactive or non-existent IDs.
- `InvalidBidException`: Validates real-time price rules (such as forcing a bid increment to be higher than the current maximum bit recorded).

### 5. Frontend UI (`package frontend`)
Uses pure Java Swing to create an interactive layout with custom painting rules:
- `Theme.java`: Hardcodes the Application palette (Colors, Fonts) and overwrites default Windows Button logic with sleek `fillRoundRect` designs.
- `MainFrame.java` (using `CardLayout`): Swaps UI Pages gracefully in memory without closing the Java App Window.
- `AppRunner.java`: The primary entry point which populates the `AuctionSystem` with fake users and items, then spins up the visuals.

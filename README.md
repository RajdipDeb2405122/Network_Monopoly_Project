# 🎩 JavaFX Multiplayer Monopoly 🎲

> A fully networked, 2D Monopoly game built in Java with custom animations, dynamic audio, and a unique late-game twist — all over local Wi-Fi.

---

## ✨ Features

- **🌐 Local Network Multiplayer** — Play with up to 8 players over a shared Wi-Fi connection. Dice rolls, player movements, property ownership, and money sync in real-time across all screens.
- **🎬 Custom Animations** — Watch the dice physically spin and shake, and follow your player token (Domino) as it hops tile-by-tile across the board.
- **🎵 Dynamic Audio System** — Background music and distinct sound effects for rolling, walking, buying, and going bankrupt. Fully adjustable from the Settings menu.
- **⚙️ Graceful Disconnects** — If a player quits mid-game, they automatically bankrupt themselves, return their properties to the bank, and exit the server without crashing the game for everyone else.

---

## 📈 The "Experience" Twist (Custom Rule!)

This game includes a custom dynamic rent system designed to speed up gameplay and reward survivors.

**How it works:**

1. Every player starts with an **Experience Multiplier** of `0`.
2. Each time you pass **GO**, your Multiplier increases by `+1` (and you collect your standard $200).
3. When someone lands on your property, rent is calculated as:

```
Rent = Base Rent × Your Experience Multiplier
```

> **Note:** Since everyone starts at `0`, rent is free on the first lap. The game starts friendly — but as players complete more laps, landing on a monopoly becomes devastatingly expensive.

---

## 🛠️ Installation & Setup

### Prerequisites

- **Java Development Kit (JDK)**
- **JavaFX SDK** configured in your IDE (IntelliJ IDEA, Eclipse, etc.)

### Running the Game

1. Clone this repository:
   ```bash
   git clone https://github.com/your-username/javafx-monopoly.git
   ```
2. Open the project in your preferred Java IDE.
3. Link JavaFX modules in your run configuration or `module-info.java`.
4. Run `Main.java` located in the `application` package.

---

## 🎮 Multiplayer Guide

All players must be connected to the **same Wi-Fi network**.

### Hosting (Player 1)

1. Open the game, select the total number of players, and click **Host Game**.
2. **⚠️ Important:** When prompted by Windows Defender or macOS Firewall, click **Allow Access** so Java can communicate on the network.
3. Find your local IPv4 address and share it with your friends:
   - **Windows:** Open Command Prompt → type `ipconfig`
   - **Mac:** Open Terminal → type `ipconfig getifaddr en0`

### Joining (Players 2–8)

1. Open the game and click **Join Game**.
2. Enter the host's IPv4 address exactly.
3. Wait in the lobby — once the expected number of players connects, the board opens automatically for everyone and player names sync instantly.

---

## 🕹️ Gameplay Rules

| Action | How |
|--------|-----|
| **Roll** | When your green arrow flashes, click the dice |
| **Buy** | A popup appears when you land on an unowned property |
| **Sell** | Sell owned properties back to the bank for half-price at end of turn |
| **Win** | Bankrupt all other players — last one standing wins! |

---

## 💻 Technical Stack

| Layer | Technology |
|-------|------------|
| Language | Java |
| UI Framework | JavaFX (FXML) |
| Networking | Java Sockets (`ServerSocket` / `Socket`) & Object Serialization |
| Threading | Async background network listeners with `Platform.runLater()` UI updates |

---

[//]: # (## 📄 License)

[//]: # ()
[//]: # (This project is open source. See [LICENSE]&#40;LICENSE&#41; for details.)

[//]: # ()
[//]: # (---)

*Enjoy the game — and try not to lose all your money on the railroads!* 🚂

# Rolling Numbers - Odometer Scrolling Effect TextView 🎰

[![](https://jitpack.io/v/delacrixmorgan/rollingnumbers.svg)](https://jitpack.io/#delacrixmorgan/rollingnumbers)

**RollingNumbers** is a Kotlin Multiplatform Compose library that animates text changes by rolling
individual characters vertically — just like a **odometer**, an **old school cash
register**, or those nostalgic **airport split-flap boards**.

Whether you’re showing stock prices, countdowns, money, or even flight numbers,
RollingNumbers makes every change feel *satisfyingly alive* with Levenshtein distance effect!
It also comes with a separate ready-to-use `CurrencyRollingNumbers` for **beautifully animated,
locale-friendly money displays**.

| Integer                                                                                       | Decimal                                                                                       | 
|-----------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------|
| <video src="https://github.com/user-attachments/assets/44aef2d5-3bda-4afb-a2f3-cc0735465c8d"> | <video src="https://github.com/user-attachments/assets/7d3c025b-076e-429a-9bca-51e74a7714b1"> | 

| Currency                                                                                      | Alphanumeric                                                                                  |
|-----------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------|
| <video src="https://github.com/user-attachments/assets/034ffb97-0858-4e0d-addc-5a3b431e63e0"> | <video src="https://github.com/user-attachments/assets/d12ff122-1e95-4879-88b2-9003f1ac53cb"> |

## ✨ Features

- 🔢 **Animate anything** — numbers, letters, symbols, emojis… if it’s a character, it can roll.
- 💰 **Built-in currency mode** — grouping separators, decimal points, and currency symbol
  placement (front or back) out of the box.
- ↕️ **Smart direction detection** — rolls up when values go up, down when they drop.
- 🎯 **Custom character sets** — hex digits, flight numbers, scoreboard letters… your animation path,
  your rules.
- 🎨 **Fully styleable** — integrates seamlessly with Compose’s `TextStyle`.
- ⚡ **Adjustable speed** — from dramatic slow rolls to rapid-fire flicks.
- 🪶 **Lightweight & clean** — minimal dependencies, easy to drop into any project.
- 🌍 **KMP ready** — works across Android, iOS, Desktop and beyond.
- 🧠 **Levenshtein-powered animation** — calculates the smoothest, shortest scroll path between
  states.

## 🕹️ Fun Facts

1. Inspired by [Robinhood Ticker library](https://github.com/robinhood/ticker) — but built for the
   modern Kotlin Multiplatform Compose world.
2. Fully **Kotlin Multiplatform** — the same rolling goodness on Android, iOS and Desktop.
3. Comes with **two ready-made components**:
    - `RollingNumbers` for any kind of text
    - `CurrencyRollingNumbers` for perfectly formatted animated amounts with currency.
4. Supports **alphanumeric** and custom sets — perfect for flight numbers, scoreboard codes, or
   creative displays.
5. **Optimized for performance** while still feeling smooth and fun to watch.

## 📦 Installation

Add the dependency in your `build.gradle`:

### Step 1

Add the JitPack repository to your `build.gradle` file:

```gradle
allprojects {
	repositories {
		maven { url "https://jitpack.io" }
	}
}
```

### Step 2

Add the dependency:

```groovy
dependencies {
    implementation "com.github.delacrixmorgan:rollingnumbers:X.X.X"
}
```

## 🚀 Usage

### Numbers

```kotlin
RollingNumbers(
    text = amount,
    characterLists = listOf(Utils.provideNumberString()),
    animationDuration = DefaultAnimationDuration.Default.duration,
)
```

### Currency

```kotlin
CurrencyRollingNumbers(
    amount = amount,
    characterLists = listOf(Utils.provideNumberString()),
    animationDuration = DefaultAnimationDuration.Default.duration,
)
```

### Alphanumeric

```kotlin
RollingNumbers(
    text = text,
    characterLists = Utils.provideAlphanumericList(),
    animationDuration = DefaultAnimationDuration.Slow.duration,
)
```

## 📄 API Reference

### `RollingNumbers`

| Parameter            | Type                 | Default                                     | Description                                           |
|----------------------|----------------------|---------------------------------------------|-------------------------------------------------------|
| `text`               | `String`             | **required**                                | The text to display and animate.                      |
| `modifier`           | `Modifier`           | `Modifier`                                  | Compose modifier for layout/styling.                  |
| `characterLists`     | `List<String>`       | `listOf(Utils.provideNumberString())`       | List of character sequences defining animation paths. |
| `animationDuration`  | `Int`                | `DefaultAnimationDuration.Default.duration` | Duration of the scroll animation in milliseconds.     |
| `textStyle`          | `TextStyle`          | `LocalTextStyle.current`                    | Style applied to the text.                            |
| `scrollingDirection` | `ScrollingDirection` | `Any`                                       | Force scroll direction: `Up`, `Down`, or `Any`.       |
| `animateChanges`     | `Boolean`            | `true`                                      | Whether to animate changes or update instantly.       |

### `CurrencyRollingNumbers`

| Parameter                 | Type           | Default                                     | Description                                            |
|---------------------------|----------------|---------------------------------------------|--------------------------------------------------------|
| `amount`                  | `Double`       | **required**                                | Target amount to display.                              |
| `modifier`                | `Modifier`     | `Modifier`                                  | Compose modifier for layout/styling.                   |
| `characterLists`          | `List<String>` | `listOf(Utils.provideNumberString())`       | List of character sequences defining animation paths.  |
| `animationDuration`       | `Int`          | `DefaultAnimationDuration.Default.duration` | Duration of the scroll animation in milliseconds.      |
| `textStyle`               | `TextStyle`    | `LocalTextStyle.current`                    | Style applied to the text.                             |
| `animateChanges`          | `Boolean`      | `true`                                      | Whether to animate changes or update instantly.        |
| `decimals`                | `Int`          | `2`                                         | Number of decimal places to format.                    |
| `currencySymbol`          | `String`       | `"$"`                                       | Currency symbol.                                       |
| `isCurrencySymbolInFront` | `Boolean`      | Locale preference                           | Whether the currency symbol appears before the number. |
| `decimalSeparator`        | `Char`         | Locale preference                           | Decimal separator character.                           |
| `groupingSeparator`       | `Char`         | Locale preference                           | Thousands separator character.                         |

### `DefaultAnimationDuration in ms`

| Parameter                          | Type  | Value |
|------------------------------------|-------|-------|
| `DefaultAnimationDuration.Slow`    | `Int` | 850   | 
| `DefaultAnimationDuration.Medium`  | `Int` | 450   | 
| `DefaultAnimationDuration.Fast`    | `Int` | 250   | 
| `DefaultAnimationDuration.Default` | `Int` | 450   | 

## 🔧 Developer Notes

The library uses:
• Levenshtein distance to determine minimal scrolling changes between the current and target text.
• `AnimatedCharacterColumn` for per-character scroll animation.
• `clipToBounds()` to keep animations inside their columns.

• Multiple Character Sets:
You can pass multiple strings in characterLists if you want certain characters to scroll
independently (e.g., digits and symbols in different sequences).

• Custom Direction Control:
For number-based animations, use `ScrollingDirection.Up` or `ScrollingDirection.Down` to force a
scroll direction or it can be `ScrollingDirection.Any` for the nearest distance.

## 💡 Contributing

Pull requests are welcome!
If you find a bug or have a feature request, please open an issue on GitHub.

## ❤️ Acknowledgements

- [Robinhood Ticker](https://github.com/robinhood/ticker)
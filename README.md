# GrokkingIt 🧠

> An interactive, chapter-by-chapter Android companion app for **Grokking Algorithms** by Aditya Bhargava — built with modern Kotlin & Jetpack Compose.

![MIT License](https://img.shields.io/badge/license-MIT-green.svg)
![Platform](https://img.shields.io/badge/platform-Android-brightgreen.svg)
![Language](https://img.shields.io/badge/language-Kotlin-purple.svg)
![Status](https://img.shields.io/badge/status-in--progress-yellow)
![PRs Welcome](https://img.shields.io/badge/PRs-welcome-blue.svg)

---

## 📖 About

**GrokkingIt** is a native Android app that brings the *Grokking Algorithms* book to life — chapter by chapter — with interactive visualizations, step-by-step algorithm animations, and runnable Kotlin code examples.

Perfect for anyone learning algorithms for the first time or preparing for technical interviews.

---

## 🗺️ Chapter Roadmap

| # | Chapter | Topics | Status |
|---|---------|--------|--------|
| 1 | Introduction to Algorithms | Binary Search, Big O Notation | 🔲 Todo |
| 2 | Selection Sort | Arrays, Linked Lists, Selection Sort | 🔲 Todo |
| 3 | Recursion | Call Stack, Base Case, Recursive Case | 🔲 Todo |
| 4 | Quicksort | Divide & Conquer, Quicksort | 🔲 Todo |
| 5 | Hash Tables | Hash Functions, Collisions, Use Cases | 🔲 Todo |
| 6 | Breadth-First Search | Graphs, BFS, Shortest Path | 🔲 Todo |
| 7 | Dijkstra's Algorithm | Weighted Graphs, Shortest Path | 🔲 Todo |
| 8 | Greedy Algorithms | Classroom Scheduling, Knapsack Problem | 🔲 Todo |
| 9 | Dynamic Programming | Knapsack, Longest Common Substring | 🔲 Todo |
| 10 | K-Nearest Neighbors | KNN Classification, Machine Learning Intro | 🔲 Todo |

---

## 🚀 Getting Started

### Prerequisites

- Android Studio Hedgehog (2023.1.1) or newer
- JDK 17+
- Android SDK 34
- Kotlin 1.9+

### Installation

```bash
# Clone the repo
git clone https://github.com/Iamvive/GrokkingIt.git

# Open in Android Studio
# File → Open → Select the cloned folder

# Build and run on emulator or physical device
```

---

## 🛠️ Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Kotlin |
| UI | Jetpack Compose |
| Navigation | Compose Navigation |
| Architecture | MVVM + Clean Architecture |
| Animations | Compose Animation API |
| Dependency Injection | Hilt |
| Testing | JUnit 4, Espresso |

---

## 📁 Project Structure

```
GrokkingIt/
├── app/
│   └── src/main/
│       ├── java/com/iamvive/grokkingit/
│       │   ├── ui/
│       │   │   ├── chapters/       # One screen per chapter
│       │   │   ├── components/     # Reusable Compose components
│       │   │   └── theme/          # Material3 theme
│       │   ├── domain/
│       │   │   └── algorithms/     # Pure Kotlin algorithm implementations
│       │   └── MainActivity.kt
│       └── res/
├── gradle/
└── README.md
```

---

## 🤝 Contributing

Contributions are welcome! Please open an issue first to discuss changes.

1. Fork the project
2. Create your feature branch
   ```bash
   git checkout -b feat/chapter-3-recursion
   ```
3. Commit using conventional commits
   ```bash
   git commit -m "feat: add recursion visualization with call stack animation"
   ```
4. Push and open a Pull Request

---

## 📄 License

This project is licensed under the MIT License — see the [LICENSE](LICENSE) file for details.

---

<p align="center">Made with ❤️ by <a href="https://github.com/Iamvive">Iamvive</a></p>

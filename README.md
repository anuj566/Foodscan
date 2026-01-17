# 🍎 FoodScan - Smart Nutritional Scanner

**FoodScan** is an Android application designed to help users make healthier choices by instantly scanning product barcodes to reveal detailed nutritional information. It normalizes all data to a **100g serving size**, making it easy to compare products fairly.

## ✨ Key Features

* **Barcode Scanning:** Instantly scans food barcodes using the device camera.
* **Detailed Nutrition:** Displays Calories, Protein, Carbs, Fats, and more.
* **Smart Scaling (100g Logic):** Automatically calculates and displays nutritional values per **100g**, regardless of the package serving size, allowing for accurate product comparisons.
* **Scan History:** Locally saves scanned items so users can refer back to them later (works offline).
* **Search by Keyword:** Allows users to manually search for food items if no barcode is available.

## 🛠️ Tech Stack

* **Language:** Kotlin
* **UI:** Android XML Layouts (Views)
* **Networking:** Retrofit / OkHttp
* **Data Parsing:** Gson
* **Database:** SQLite / Room (for saving Scan History)
* **APIs Used:**
    * **OpenFoodFacts API** (Primary source)
    * **Nutritionix API** (Secondary source)

## 📂 Project Structure

com.myawesomegames.foodscan
├── database/              # Local database handling
│   ├── HistoryDbHelper.kt # SQLite helper for saving scans
│   └── HistoryItem.kt     # Model for saved items
├── network/               # API Communication
│   ├── ApiService.kt      # Retrofit interface
│   ├── RetrofitClient.kt  # Network configuration
│   └── Responses.kt       # Data models (Nutritionix/OpenFoodFacts)
├── ui/                    # Activities and Adapters
│   ├── MainActivity.kt    # Scanner and Home screen
│   ├── ProductDetailsFragment.kt # Nutritional info display
│   ├── HistoryActivity.kt # List of past scans
│   └── KeywordsActivity.kt# Manual search
└── res/layout/            # XML UI Designs

🚀 Getting Started
Prerequisites
Android Studio

JDK 17 or higher

Installation
Clone the repository:

Bash

git clone [https://github.com/anuj566/food-scanner-android.git](https://github.com/anuj566/Foodscan.git)
Open in Android Studio: File -> Open -> Select the project folder.

🔑 API Configuration (Important)
This app requires API keys to fetch nutritional data. You must obtain your own keys to run the app.

Get a free API key from Nutritionix or OpenFoodFacts.

Open src/main/java/network/RetrofitClient.kt (or Constants.kt).

Replace the placeholder with your key:

Kotlin

// Example
const val API_KEY = "YOUR_API_KEY_HERE"
const val APP_ID = "YOUR_APP_ID_HERE"
Sync Gradle and Run the app!

🤝 Contributing
Contributions are welcome!

Fork the repo.

Create a feature branch (git checkout -b feature/NewFeature).

Commit your changes.

Push to the branch.

Create a Pull Request.

👤 Author
Anuj

GitHub: anuj566

Eat Healthy, Live Healthy.

* RealTime-Antivirus
Real-Time Antivirus App is a lightweight Java-based security application that detects threats the moment a file is accessed. It learns from your responses, remembers past threats, and adapts over time—offering personalized protection through a clean JavaFX interface backed by PostgreSQL.

🛡️ Real-Time Antivirus Application

A lightweight, intelligent, and user-adaptive antivirus application built in Java with JavaFX UI and PostgreSQL support. This project brings dynamic real-time file monitoring and personalized threat handling by learning from user interactions.


🌟 Introduction

This project enhances a previously built static antivirus scanner by introducing **real-time threat detection**, **behavioral learning**, and **user decision memory**. The system monitors file access events, alerts the user when suspicious files are opened, and adapts based on the user's previous responses—creating a smart, responsive defense mechanism.

✅ Features

- 🔍 Real-time file access monitoring
- ⚠️ Alert prompts for suspicious files
- 💾 PostgreSQL integration to store and learn from user responses
- 🧠 Auto-handling of known threats using behavioral memory
- 📄 Logging of all scans and alerts
- 🎨 JavaFX-based user-friendly UI
- 🗃️ Quarantine support (basic implementation)

🛠️ Tech Stack

- **Java** (Core logic)
- **JavaFX** (User Interface)
- **PostgreSQL** (Threat decision storage)
- **FileSystem Watcher API** (For real-time monitoring)

🗂️ Project Structure

RealTimeAntivirusApp/
│
├── out/ # Compiled classes
├── quarantine/ # Files moved here when denied
├── src/
│ ├── AntivirusApp.java
│ ├── AntivirusDashboardUI.java
│ ├── FileMonitor.java
│ ├── FileAnalysis.java 
│ ├── FileScannerLogger.java 
│ ├── ThreatDatabase.java 
│ ├── DatabaseConnection.java 
│ ├── RealTimeAntivirus.java 
│ ├── antivirusdb.sql 
│ ├── scan_log.txt 
│ └── testfile.bat/.txt 

👩‍💻 Authors

- Vishalakshi Amale  
- Sakshi Kharatmal 
- Guide: Prof. Sushant Shelar

This project was developed as part of our academic coursework in the Department of Computer Science & Engineering at SSWCOE, Solapur.



# Data Science Integration Project: Hotel Recommendation for Events

Alfonso Santana Morquecho <br>
Nicole María Ortega Ojeda <br> <br>
University of Las Palmas de Gran Canaria <br>
Subject: Data science application development

---
## 1. Project Overview

**Project Objectives:**

The main objective of this project is to integrate and process real-time and delayed data from 
the **Xotelo** and **Ticketmaster** APIs to offer users personalized accommodation recommendations 
based on their event interests. By analyzing the events selected on Ticketmaster, the system will identify key locations
and dates and cross-reference this information with Xotelo to suggest accommodation options tailored to different 
price ranges. This seeks to optimize the user experience, facilitating travel and stay planning, 
while adding value by combining two data sources into a single, functional product.
It is a modular, event-driven system built in Java 21 that uses **ActiveMQ** as a message broker 
and supports both **real-time processing** and **historical data analysis**.

---
## 2. Value Proposition
> Help users plan smarter by recommending hotels based on events they plan to attend — filtered by price and rating.

The system:
- Periodically fetches data from two public APIs.
- Publishes standardized events to a message broker.
- Stores events in `.events` files for later analysis.
- Analyzes data via an in-memory **datamart** and interactive CLI.

---
## 3. API & Datamart Justification

### APIs used:

| API            | Why it was chosen |
|----------------|-------------------|
| **Ticketmaster** | Dynamic event data (concerts, sports, etc.) with location and time |
| **Xotelo**        | Hotel listings with real-time rates by city and date |

### Datamart Design:

The in-memory `Datamart` in the business unit module allows:

- Fast filtering by city, price, and rating
- Real-time event tracking
- Cross-analysis of hotel data with scheduled events

---
## 4. Build and Run Instructions

### Requirements

- Java 21
- Maven
- ActiveMQ (running on `localhost:61616`)
- SQLite driver (included)

---

### Build All Modules

---
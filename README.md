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

| API               | Url                                                                          |Why it was chosen |
|-------------------|------------------------------------------------------------------------------|-------------------|
| **Ticketmaster**  | https://developer.ticketmaster.com/products-and-docs/apis/discovery-api/v2/  | Dynamic event data (concerts, sports, etc.) with location and time |
| **Xotelo**        | Hotels: https://data.xotelo.com/api/list?location_key=citykey&offset=0&limit=5<br>  Offers: https://data.xotelo.com/api/rates?hotel_key=hotelkey&chk_in=2025-07-16&chk_out=2025-07-20   | Hotel listings with real-time rates by city and date |    

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

### Modules

Event-Feeder Module:
- Retrieves event data from the Ticketmaster API.
- Publishes this data to the event.Event topic on ActiveMQ.
- Includes classes like EventController, TicketmasterProvider, and EventStore.

  (foto diagrama)

Hotel-Feeder Module:
- Retrieves hotel and pricing data from the Xotelo API.
- Publishes this data to the hotel.Hotel topic on ActiveMQ.
- Includes classes like XoteloController, HotelProvider, and HotelStore.

  (foto diagrama)

Event Store Module:
- Subscribes to the message broker and stores events in .events files for later processing.
- Manages event serialization and file storage.

Business Unit Module:
- Processes stored events to generate user-friendly recommendations based on city, date, price, and rating.
- Provides in-memory data structures for fast analysis.

User Interface Module: 
- Provides a simple CLI for interacting with the in-memory datamart and viewing hotel recommendations.
- Handles user inputs and presents filtered results.

---
### How to run the program



---
## 5. Resources used

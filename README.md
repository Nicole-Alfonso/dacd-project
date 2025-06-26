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

The Datamart in the Business Unit module acts as a in-memory data store for events and hotels, providing efficient data retrieval and filtering for real-time and historical analytics. It has two main components: <br>
1. Data Structures
- `hotelEvents (List<HotelEvent>)` - Stores hotels grouped by city, allowing efficient city-based hotel lookups.
- `eventos (set<EventInfo>)` - Stores all known events for fast retrieval and search by name.
- `hoteless (List<HotelEvent>)` - Stores all known hotels for fast retrieval and search by name.

2. Main Methods
- `addHotel(HotelEvent)` - Adds a hotel event to the city-specific map.
- `addEvent(EventInfo)` - Adds a general event to the event list.
- `getEventos()` - Returns the list of all known events.
- `getEventosPorNombreYCiudad(String name)` - Finds an event by name and city.
- `getHotelesFiltrados(String ciudad, double eventoLat, double eventoLon, FiltroHotel filtro)` - Gets hotels filtered by city, price, category, rating, and distance.

---
## 4. Build and Run Instructions

### Requirements

- Java 21
- Maven
- ActiveMQ (running on `localhost:61616`)
- SQLite driver (included)

---

## Modules

### **Event-Feeder Module:** <br>
Retrieves event data from the Ticketmaster API and publishes this data to the `event.Event` topic on ActiveMQ. <br>
Includes classes and packages like:
- **Main:** Entry point for the event feeder. Initializes the controller and sets up a scheduler for periodic event data fetching.
- **TicketmasterController:** Coordinates fetching, storing, and publishing hotel data to ActiveMQ, using the HotelProvider and HotelStore interfaces. <br>
- *application:* <br>
**EventProvider:** Interface for classes that fetch event data from external sources. <br>
**EventStore:** Interface for classes that store event data.<br>
- *domain.model:* <br>
**Event:** Represents an event with attributes like ID, name, location, date, and URL.<br>
- *infrastructure:* <br>
**TicketmasterProvider:** Fetches event data from the Ticketmaster API and converts it into Event objects. <br>
**EventSqliteStore:** Stores event data in a local SQLite database.

![event-feeder.jpeg](event-feeder.jpeg)

### **Hotel-Feeder Module:** <br>
Retrieves hotel and pricing data from the Xotelo API. Publishes this data to the hotel.Hotel topic on ActiveMQ. <br>
Includes classes and packages like:
- **Main:** Entry point for the hotel feeder. Initializes the controller and sets up a scheduler for periodic hotel data fetching.
- **XoteloController:** Coordinates fetching, storing, and publishing hotel data to ActiveMQ, using the HotelProvider and HotelStore interfaces. <br>
- *application:* <br>
**HotelProvider:** Defines de fetching of hotel data received. <br>
**HotelStore:** Defines the storing hotel data in a persistent database.<br>
- *model:* <br>
**HotelData:** Represents hotel details including prices, location, and rating.<br>
- *infrastructure:* <br>
**XoteloProvider:** Implements HotelProvider, fetching hotel and price data from the Xotelo API. <br>
**HotelSqliteStore:** Implements HotelStore, managing local SQLite storage for hotel and offer data.

![hotel-feeder.jpeg](hotel-feeder.jpeg)


### **Event-Store-Builder Module:** <br>
Subscribes to the message broker and stores events in `.events` files for later processing. Manages event serialization and file storage. <br>
It has the following structure:
- *core:* <br>
**EventStoreBuilder:** Connects to ActiveMQ and creates subscribers to receive event messages. <br>
- *launcher:* <br>
**BuilderLauncher:** Starts the event listening process using EventStoreBuilder. <br>
- *listener:* <br>
**GenericEventListener:** Handles incoming messages and sends them to an EventWriter for persistence.<br>
- *writer:* <br>
**FileEventWriter:** Saves events to files, organized by topic and date. <br>
**EventWriter:** Defines the contract for writing events to persistent storage.

  
![event-store-builder.jpeg](event-store-builder.jpeg)


### **Business-Unit Module:** <br> 
Processes stored events to generate user-friendly recommendations based on city, date, price, and rating. Provides in-memory data structures for fast analysis. <br>
The structure of this module is: 

- **BusinessLauncher:** Starts the business unit and filters hotels for a given event based on user input. <br>
- **BusinessUnit:** Manages the datamart, loads historical data, and provides filtered hotel recommendations for events. <br>
- **Datamart:** Stores and retrieves event and hotel data, applying filters for location, price, rating, and distance. <br>
- **EventSubscriber:** Subscribes to ActiveMQ topics for real-time event and hotel data updates. <br>
- **HistoricalEventLoader:** Loads historical hotel events from files into the datamart.


### **Business-Api Module:** <br>
Serves as the RESTful interface for the project, allowing external clients to query hotel data based on event preferences. It exposes HTTP endpoints for filtering and retrieving hotel recommendations, bridging the gap between the core business logic in the Business Unit and user-facing applications. <br>
Structure of this module: 

- **BusinessApplication:** Main entry point for the Spring Boot application, initializes the web server. <br>
- *controller:* <br>
**HotelViewController:** Exposes REST endpoints to search for hotels near events using filters. <br>
- *dto:* <br>
**HotelDTO:** Data transfer object for hotel filter parameters.

  
### **Shared-Model Module:** <br>
Is responsible for providing common data structures and utilities that are used across the various project modules, including Event-Feeder, Hotel-Feeder, and Business-Unit. <br>
Structure of Shared-Model module:

- **EventInfo:** Holds basic information about an event, including location, date, and URL. <br>
- **FiltroHotel:** Represents filter criteria for searching hotels (category, price, rating, distance). <br>
- **HotelEvent:** Contains detailed hotel data, including pricing, rating, location, and availability dates. <br>
- **InstantTypeAdapter:** Handles JSON serialization and deserialization for Instant timestamps.<br>
- **LocalDateTypeAdapter:** Handles JSON serialization and deserialization for LocalDate dates.<br>
- **PriceOffer:** Represents a hotel price offer from a specific provider, including price and currency.


> Each module has a package called test, which includes some tests to verify the correct operation of the module.
> Packages are not included on the class diagrams.
---
### How to run the program
This project is developed in Java using IntelliJ IDEA as the development environment. Below are the steps to run the application from the IDE.

1️⃣ **Open the project in IntelliJ IDEA.**
Launch IntelliJ IDEA and select the "Open" option. Then, navigate to the project's root folder and open it. IntelliJ will automatically recognize the project structure and begin indexing the files.


2️⃣ **Run BuilderLauncher.**
Before running this class, you need to ensure the ActiveMQ broker is up and running at tcp://localhost:61616. Then, from IntelliJ, run the BuilderLauncher class located in the event-store-builder module. This class connects to the broker and durably subscribes to the "HotelPrice" and "TicketmasterEvents" topics. It is used to receive and save events sent by the feeders, writing them to flat files organized by date using FileEventWriter.

<img src="run2.PNG" width="425"/>  <img src="run3.PNG" width="550"/>

*Figure 1.1. ActiveMQ running on port 6161*
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*Figure 1.2. Module Builder Launcher running*

3️⃣ **Run the Feeders.**
From IntelliJ, run the main method of the hotel-feeder and events-feeder modules. When started, each feeder queries its corresponding API, saves the data to the database, and publishes the events to the ActiveMQ broker, where they will be received by the BuilderLauncher. Additionally, each feeder has a scheduler that keeps the process active, automatically making new queries to the API every hour to keep the data up to date.

<img src="run4.PNG" width="800"/>

*Figure 2.1. Hotels feeder active*

<img src="run5.PNG" width="1000"/>

*Figure 2.2. Stored hotel events example*

<img src="run6.PNG" width="475"/> <img src="run7.PNG" width="500"/>

*Figure 2.3/4. Sql tables with stored hotels*

<img src="run8.PNG" width="800"/> 

*Figure 2.5. Events feeder started*

<img src="run9.PNG" width="1000"/>

*Figure 2.6. Stored event events example*

<img src="run10.PNG" width="800"/> 

*Figure 2.7. Sql tables with stored events*

4️⃣ **Run BusinessLauncher.**
Finally, run the BusinessLauncher class, which acts as the entry point for the system's business logic. This class loads historical data from files, initializes the in-memory datamart, and connects to the ActiveMQ broker to receive real-time updates. Using the input parameters, it allows you to query hotels related to events by applying various filters such as date, price, category, rating, or distance.

<img src="run11.PNG" width="500"/> 

*Figure 3.1. Parameters examples used to start the Business Unit*

<img src="run12.PNG" width="800"/>

*Figure 3.2. Business Unit Answer example*


5️⃣ **Run business-api.**
You can also run the business-api module, which creates a web interface using Spring Boot. Running the BusinessApplication class starts a server with endpoints to search for hotels related to events. This is an accessible way for users or REST clients to interact with the business logic. To use the visual environment search for localhost:8080 on the browser.

<img src="run13.PNG" width="800"/> 

*Figure 4.1. Business Api running*

<img src="run14.PNG" width="800"/>

*Figure 4.2. Web interface on localhost:8080*

<img src="run15.PNG" width="800"/> 

*Figure 4.3. Graphical answer example, using only mandatory filters*

<img src="run16.PNG" width="400"/>

*Figure 4.4. Optional filter example*

<img src="run17.PNG" width="800"/>

*Figure 4.5. Graphical answer example, using optional filters*

---
## 7. Architecture and Patterns Used

The project applies several recognized design principles and patterns, aiming to achieve a modular, maintainable, and easily testable solution:

**Clean Architecture:** A layered structure was adopted to completely decouple business logic (such as BusinessUnit and HotelEvent) from the infrastructure (CsvFeeder, JsonFeeder) and the interface (HotelViewController, DTOs). This makes it easy to scale or modify components without affecting the core of the system.

**Event Sourcing:** All system information is modeled as immutable events. Feeders generate these events, which are then stored and processed to reconstruct the state in memory (Datamart). This enables traceability, auditing, and replay of historical state.

**Builder Pattern:** Used to build complex objects such as HotelEvent, allowing for clearer, safer, and error-free creation, especially when there are multiple optional fields.

**Collection Pipeline:** Java Streams is used extensively to apply chained filters to collections (e.g., hotels related to events). This allows for clean, declarative, and extensible code.

These patterns were chosen for their suitability to the problem, and together they contribute to a robust, flexible, and evolution-oriented architecture for the system.

---
## 6. Resources

The project was developed using IntelliJ IDEA, a powerful and highly integrated IDE widely adopted in the software industry. It provides seamless integration with various tools and technologies, streamlining the development process. For version control, Git was used to meticulously track source code changes, while GitHub served as the cloud-based repository for hosting the project's codebase.

For dependency management and build automation, Maven was utilized, simplifying the compilation, testing, and packaging of the project. The following dependencies were included:

Gson - Library to convert Java objects to JSON.
```
<dependency>
            <groupId>com.google.code.gson</groupId>
            <artifactId>gson</artifactId>
            <version>2.9.1</version>
</dependency>
```

JUnit - Framework for writing and running unit tests in Java.
```
<dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <version>5.9.3</version>
            <scope>test</scope> 
</dependency>
```

OkHttp3 - HTTP client for making network requests efficiently.
```
<dependency>
            <groupId>com.squareup.okhttp3</groupId>
            <artifactId>okhttp</artifactId>
            <version>4.9.3</version>
</dependency>
```

SQLite JDBC - JDBC driver to connect Java applications to SQLite databases.
```
<dependency>
            <groupId>org.xerial</groupId>
            <artifactId>sqlite-jdbc</artifactId>
            <version>3.36.0.3</version>
</dependency>
```

Json - Library for parsing, generating, and manipulating JSON data.
```
<dependency>
            <groupId>org.json</groupId>
            <artifactId>json</artifactId>
            <version>20220320</version>
</dependency>
```

Jsoup - HTML parser for extracting and manipulating data from web pages.
```
<dependency>
            <groupId>org.jsoup</groupId>
            <artifactId>jsoup</artifactId>
            <version>1.14.3</version>
</dependency>
```

Apache Spark - Lightweight web framework for creating web applications and APIs.
```        
<dependency>
            <groupId>com.sparkjava</groupId>
            <artifactId>spark-core</artifactId>
            <version>2.9.4</version>
</dependency>
```


SLF4J - Simple logging facade to plug in various logging frameworks.
```
<dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>2.0.9</version>
</dependency>
```

ActiveMQ - Client library to interact with Apache ActiveMQ message broker.
```
<dependency>
            <groupId>org.apache.activemq</groupId>
            <artifactId>activemq-client</artifactId>
            <version>5.18.4</version>
</dependency>
```

jakarta.annotation:jakarta.annotation-api - Provides standard Jakarta annotations required for compatibility with frameworks like Spring.
```
<dependency>
            <groupId>jakarta.annotation</groupId>
            <artifactId>jakarta.annotation-api</artifactId>
            <version>2.1.0</version>
</dependency>
```

spring-boot-starter-web - Includes everything needed to build web applications with Spring Boot, including REST controllers and an embedded server.
```
<dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
            <version>3.2.5</version>
</dependency>
```

spring-boot-starter-thymeleaf - Enables support for the Thymeleaf template engine, used to generate dynamic HTML views.
```
<dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
            <version>3.3.4</version>
</dependency>
```

---
## 7. Future improvements
Some future improvements for this Hotel Recommender project could be: 

1. **Enhanced Error Handling and Logging:**
Implement more robust error handling mechanisms and comprehensive logging to improve the reliability and the debugging.

2. **Integration with Additional APIs:**
Expand the system's capabilities integrating other relevant APIs to provide a broader range of data.

3. **User Interface Development:**
   Optimize the user interface through a visual redesign, the addition of new features, and improved error handling for greater stability.

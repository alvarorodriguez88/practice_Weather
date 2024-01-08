# Weather Data Capture

###### Development of Applications for Data Science
###### Degree in Data Science and Engineering - 2nd Year
###### University of Las Palmas de Gran Canaria - School of Computer Science and Mathematics

---

## Functionality Summary

This program's functionality revolves around recommending hotels based on the weather conditions desired by the user. It suggests, among the scope of hotels across the 8 Canary Islands, those that align with the user's request and displays the different options along with their respective prices, allowing the user to make a choice. 

The application will showcase a user interface where the user can input:
- Check-in date
- Check-out date
- Weather condition

Subsequently, the program will display a table listing hotels that have (at least on 1 of their 5 forecasted days) the specific weather condition desired by the user. This table will arrange hotels in ascending order of price. The initial check-in will be the next day we are checking the application, and the check-out will be 3 days later(maximum 4 days of estance), because of the prediction weather.

>**NOTE**: The interface won’t display any content in the table if there's no available data or if the check-in and check-out dates are entered incorrectly.

---

## Design

**Patterns used:** For this project, I've employed the observer pattern, as both passive modules await notification of incoming events to handle them appropriately (Datalake-Builder and Recommendation-Business-Unit). I've utilized the MVP (Model-View-Presenter) design principle, evident in how I distinguish between business logic (Model), user information representation (View), and presentation logic to manage user interaction (Controller).

---

## Deployment

**Instructions for deploying the program**

1. Access the "practice_Weather" repository and navigate to the latest release (proyecto-dacd).
2. Download the 4 zipped files from the latest release (proyecto-dacd) named:
   - Datalake-builder
   - Hotel-provider
   - Weather-provider
   - Recommendation-business-unit
3. Save their contents in your desired working directory.
4. Access the terminal and start the local server.
5. Once the local server is running, go to the working directory containing the contents of the 4 zip files.
6. While there, in the terminal, execute the datalake-builder with a command line like this:
    ![Captura de pantalla 2024-01-08 015620](https://github.com/alvarorodriguez88/practice_Weather/assets/145196321/0c6285fc-ded2-4b57-94dc-b4e356c52a96)
   >**NOTE**: Ensure to end the argument with a *backslash*.
7. Then, start the execution of the recommendation-business-unit with a line like this:
   ![Captura de pantalla 2024-01-08 015654](https://github.com/alvarorodriguez88/practice_Weather/assets/145196321/cc5d19c0-6638-4957-a846-8eac33da469e)
   >**NOTE**: This will prompt a pop-up where you can input your check-in, check-out, and the weather condition.
8. After initializing these passive modules, start both active modules, the hotel-provider, and the weather-provider.
   ![Captura de pantalla 2024-01-08 015719](https://github.com/alvarorodriguez88/practice_Weather/assets/145196321/98d475b4-ec90-434e-85c4-a4102e24f2bb)
   ![Captura de pantalla 2024-01-08 015753](https://github.com/alvarorodriguez88/practice_Weather/assets/145196321/1e328796-1a7f-4172-b041-98338e80954c)
9. Finally, the data mart will be in the same directory as the recommendation-business-unit module, continuously updating until no more events are sent.

---
## Class Diagrams
##### Datalake Builder
![Captura de pantalla 2024-01-08 150402](https://github.com/alvarorodriguez88/practice_Weather/assets/145196321/4ec3ccdb-9258-4a76-a5aa-e2ecbb9ed538)

##### Hotel Provider
![Captura de pantalla 2024-01-08 162922](https://github.com/alvarorodriguez88/practice_Weather/assets/145196321/5c933c19-bf89-440e-9eff-0add506a783c)

#### Recommendation-Business-Unit
![Captura de pantalla 2024-01-08 201759](https://github.com/alvarorodriguez88/practice_Weather/assets/145196321/6a3473ef-b761-42e5-a51d-5d94d012e2e1)

#### Weather-Provider
![Captura de pantalla 2024-01-08 204247](https://github.com/alvarorodriguez88/practice_Weather/assets/145196321/93379fdd-0ee0-4467-8172-cf1c8ec6a0b5)

---

## Program Logic

The program comprises active modules responsible for communicating with 2 APIs to retrieve data and send it to the broker.

- **Weather-provider:** Communicates with the OpenWeatherMap API, sending events every 6 hours with the forecast for the next 5 days at 12 PM.
- **Hotel-provider:** Manages communication with the Xotelo API, sending events regarding hotels with their nightly prices every 6 hours.

On the other hand, passive modules process events from both topics for exploitation.

- **Datalake-Builder:** Stores all events occurring in the topics that feed the program to maintain a historical record.
- **Recommendation-Business-Unit:** Bears significant responsibility, processing events from both topics and storing them in the datamart that powers the user interface, which is designed to be very user-friendly and intuitive.

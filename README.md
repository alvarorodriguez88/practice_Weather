# Weather Data Capture

### Executive Summary
This program is designed to acquire specific meteorological data from the OpenWeatherMap (https://openweathermap.org/) website through its REST API. These data will be stored in a database containing 8 tables, aiming to store the respective meteorological data for each of the Canary Islands (Gran Canaria, Fuerteventura, Lanzarote, La Graciosa, Tenerife, La Gomera, El Hierro, and La Palma).

------------

### Table of Contents

- Introduction
- Used Technologies
- Program Design
	- Requirements
	- Database Design
	- User Interface
- Implementation
  - Source Code Structure
	- Key Features
	- Issues and Solutions
- Testing
	- Testing Strategy
	- Test Results
- Deployment
- Future Enhancements
- Conclusions
- References

------------

### Introduction
For the first practical assignment of the course, we were required to create a program aimed at obtaining meteorological data. My project, "practice_Weather," successfully achieves the aforementioned objective by retrieving the required meteorological data outlined in the assignment prompt. This includes data on date, temperature, precipitation probability, humidity, cloud coverage, and wind speed from specific points located on each of the 8 Canary Islands.

The program operates locally, displaying informative messages on the screen to keep the user informed about updates. It refreshes every 6 hours, providing predictions for the next 5 days at the specified points based on their latitude and longitude.

Additionally, the code has been developed in the "IntelliJ" development environment, using the version number 17.

------------

### Used Technologies

The programming language I used for my project was Java, which provides greater flexibility for the required task by allowing more comfortable handling of different classes and external information. The libraries I used in my project were obtained from "Maven Repository" (https://mvnrepository.com/repos/central),  and they are as follows:

- Jsoup
		<dependency>
            <groupId>org.jsoup</groupId>
            <artifactId>jsoup</artifactId>
            <version>1.16.2</version>
        </dependency>
- Gson
		<dependency>
            <groupId>com.google.code.gson</groupId>
            <artifactId>gson</artifactId>
            <version>2.10.1</version>
        </dependency>
- SQLITE-JDBC
		<dependency>
            <groupId>org.xerial</groupId>
            <artifactId>sqlite-jdbc</artifactId>
            <version>3.43.2.2</version>
        </dependency>


------------

### Program Design

###### Requirements
The program was tasked with obtaining information through the OpenWeather website's REST API for storage in its own database. It was required to update the weather forecast every 6 hours, specifically at 12 pm, for the upcoming 5 days.

###### Database Design
The database consists of 7 columns, including the required meteorological data, preceded by a column that identifies each record in the database (ID).

![Uploading Captura de pantalla 2023-11-12 192529.png…]()

###### Class Diagram
Hacerlo en el IPad

###### User Interface
Upon running the program, the user will be able to see various informative messages in the console each time the routine is executed and each time each table is updated with its respective name. Additionally, a message will appear when the program automatically finishes after 48 hours since it started (unless stopped earlier).

------------

### Implementation
###### Source Code Structure
![Captura de pantalla 2023-11-13 103928](https://github.com/alvarorodriguez88/practice_Weather/assets/145196321/81b4d301-21ee-4bea-8a36-db539cfbb36b)


------------

###### Key Features
- Fetching the parameters of interest from the JSON received by the program from the REST API (OpenWeatherMapSupplier).
		         String popString = jsonObject1.get("pop").toString();
                double pop = Double.parseDouble(popString);
                JsonObject windJson = jsonObject1.getAsJsonObject("wind");
                String windString = windJson.get("speed").toString();
                double windSpeed = Double.parseDouble(windString);
                JsonObject mainJson = jsonObject1.getAsJsonObject("main");
                String tempString = mainJson.get("temp").toString();
                double temp = Double.parseDouble(tempString);
                String humidityString = mainJson.get("humidity").toString();
                int humidity = Integer.parseInt(humidityString);
                JsonObject cloudsJson = jsonObject1.getAsJsonObject("clouds");
                String cloudsString = cloudsJson.get("all").toString();
                int clouds = Integer.parseInt(cloudsString);
- Storing the information of the different islands in their respective tables (WeatherController).
		for (Location loc : locations) {
                ArrayList<Weather> weathers = openWeatherMapSupplier.getWeather(loc, apiKey);
                sqlite.save(statement, weathers);
                System.out.println("Uploaded " + loc.getIsla());
            }
- Automating the program execution every 6 hours (ProgramController).
		public void start(String apiKey, String dataBase) {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                WeatherController controller = new WeatherController();
                try {
                    controller.execute(apiKey, dataBase);
                    System.out.println("Execution done...");
                } catch (IOException e) {
                    System.out.println("ERROR: " + e);
                }
            }
        };
        long interval = 1000 * 60 * 60 * 6;
        timer.schedule(task, 0, interval);

###### Problems and Solutions
During the practice, I encountered several issues that I addressed as I progressed. One of the most recent ones occurred in the "OpenWeatherMapSupplier" class. When creating an "ArrayList<Weather>()," my intention was to create one for each island. However, I found that I was creating one for all islands, and as I added more locations, each iteration appended the data from the previous ones, rendering it rather ineffective. To solve this, I simply had to create a list before starting the loop and return it as the result.

Additionally, another problem I faced was committing changes in Spanish. I had to repeat the process in a new project, so the time difference between commits in the "GitHub" repository reflects this.

------------

### Testing

###### Testing Strategy
I conducted tests on the project as I progressed. These tests were performed in the main section, aiming to print the results provided by the functions.

I focused on a gradual approach. Initially, I tested the information received from the JSON object. Subsequently, I created a database with a single table and inserted a single record.

Later on, I discovered how to insert information into each table and created a method to inject the necessary information to ensure that everything was functioning correctly.

The final aspect I addressed was the creation of a class to automate the program, to which I assigned a quick time frame to observe how new records were added to each table during peak hours.

###### Test Results
At first, I encountered several errors, which I addressed by examining the console messages. One of the most challenging ones to identify was a mistake I made with the data type of the variable "POP." I initially thought it was an integer, which led to multiple errors when attempting to insert the location.

------------

### Deployment

###### Instructions for Deploying the Program
1. Download the program from the GitHub repository (https://github.com/alvarorodriguez88/practice_Weather) as a zip file and extract the contents of the zip file to the folder of your choice.

2. Open IntelliJ and navigate to the folder "practice_Weather-master" within the folder where you extracted the contents of the previous download.

3. With the file open, go to the "src" folder, then within "rodriguezgonzalez" and "control," open the class "Main."

4. In the upper part of the screen, you should see "Current File." Enter it and select "Edit Configurations."

5. Inside, click the "+" symbol in the upper-left corner and choose "Application."

6. In the window that appears, give it a name (recommendation: "Main"). Then, in the "Main class" box, click the icon next to it and choose the option that says "Main of rodriguezgonzalez.control."

7. Next, in the box below ("Program arguments"), insert two parameters. The first should be your personal API Key, and the second should be the path where you want to save the database, ending with the name "/Tiempo_Canarias.db" to create the connection correctly (keep this information handy, as it will be useful later).

8. Click "Apply" and then "Ok," and proceed to run the program.

9. Ensure you have a program called "DB Browser for SQLite" installed on your computer to visualize the database.

10. Open the application and select "Open Database" at the top. Navigate to the location you entered earlier to view how the records have been inserted.

------------

### Future Enhancements
The program opens the door to several possible improvements. For instance, the incorporation of data analysis from the stored information to conduct a study and create tables and graphs for easier user visualization.

------------

### Conclusions
As the days progress, the weather predictions for the last day being updated become more accurate, although the differences between them may not be very noticeable.

------------

### References
These are some of the resources I used for this project:
- https://www.youtube.com/watch?v=sArp10JAIaM&t=40s
- https://www.youtube.com/watch?v=TipyOAYGsdc
- https://www.youtube.com/watch?v=ENCwOv2lCms&t=141s
- https://stackoverflow.com/
- https://chat.openai.com/
- Materials available on the Moodle of the subject.

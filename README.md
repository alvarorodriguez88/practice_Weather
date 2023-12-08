# Weather Data Capture

### Executive Summary
The program will have the function of storing the weather predictions for the next 5 days on the 8 Canary Islands. It is composed of two modules:

Weather-provider: This module is responsible for gathering the relevant information obtained from the OpenWeatherMap API (https://openweathermap.org/) and formatting it as desired. Additionally, it is subscribed to a broker, to which it will send events every 6 hours.

Event-store-builder: This module will have a durable subscription to the topic prediction.weather, through which it will receive events sent by the previous module and interpret them. Furthermore, after interpreting them, it creates a structure in the provided directory to store the content of the events in a file with the .events extension.

------------

### Table of Contents

- Introduction
- Used Technologies
- Program Design
	- Requirements
   	- Class diagram
	- User Interface
- Implementation
  	- Source Code Structure
	- Key Features
- Testing
	- Testing Strategy
	- Test Results
- Deployment
- Future Enhancements
- Conclusions
- References

------------

### Introduction
As the second practical assignment for the subject, we were required to create a program aimed at obtaining information and sending and receiving events to a local broker. My project, 'practice_Weather,' focuses on the aforementioned objective. The first module successfully gathers the required meteorological data specified in the assignment (data on date, temperature, precipitation probability, humidity, cloud cover, and wind speed) from various exact points located on each of the 8 Canary Islands. It processes this data to send it as events to the broker, which handles notifying the subscribers.

On the other hand, the second module subscribes to the topic created by the active module to receive these events for subsequent storage in a file.

The program operates locally, displaying informative messages on the screen to keep the user updated on the updates occurring every 6 hours. It provides forecasts for the next 5 days at specific points identified by their latitude and longitude. Additionally, the code has been developed in the 'IntelliJ' working environment, using version 17.

------------

### Used Technologies

TThe programming language I utilized in my project was Java, which offers greater flexibility for the required task by allowing comfortable working with different classes and handling external information more conveniently.
Additionally, the tool I employed for version control was Git, and I created a repository on GitHub. This repository served as a resource whenever I needed to investigate errors or identify the origins of any issues.
Furthermore, the libraries I used in my project were sourced from the 'Maven Repository' (https://mvnrepository.com/repos/central),  and they are as follows:

- Jsoup:
  
   		<dependency>
  			<groupId>org.jsoup</groupId>
            		<artifactId>jsoup</artifactId>
            		<version>1.16.2</version>
        	</dependency>

- Gson:
  
		<dependency>
           		<groupId>com.google.code.gson</groupId>
            		<artifactId>gson</artifactId>
            		<version>2.10.1</version>
        	</dependency>

- Activemq:
  
		<dependency>
            		<groupId>org.apache.activemq</groupId>
  			<artifactId>activemq-client</artifactId>
  			<version>5.15.12</version>
  		</dependency>

- Logback:
  
  		<dependency>
            		<groupId>ch.qos.logback</groupId>
            		<artifactId>logback-classic</artifactId>
            		<version>1.2.3</version>
        	</dependency>

In addition to the plugins required for copying the dependencies for the executable.

	<build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-jar-plugin</artifactId>
                <version>3.1.0</version>
                <configuration>
                    <archive>
                        <manifest>
                            <mainClass>rodriguezgonzalez.control.Main</mainClass>
                            <addClasspath>true</addClasspath>
                            <classpathPrefix>lib/</classpathPrefix>
                        </manifest>
                    </archive>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-dependency-plugin</artifactId>
                <version>3.6.0</version>
                <executions>
                    <execution>
                        <phase>prepare-package</phase>
                        <goals>
                            <goal>copy-dependencies</goal>
                        </goals>
                        <configuration>
                            <outputDirectory>${project.build.directory}/lib</outputDirectory>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>

------------

### Program Design

###### Requirements
We were required to retrieve information from the OpenWeather website's REST API, and the module was to send a series of events every 6 hours for the prediction of the next 5 days at 12 pm to the broker. Subsequently, another module subscribed durably to the same broker would handle the information reception and store it in a file.

###### Class Diagrams

###### Event-store-builder


###### Weather-provider


###### User Interface
Upon running the program, the user will be able to view various informative messages on the console that appear each time the routine is executed. Additionally, confirmation of the location where the events have been saved will be displayed.

------------

### Implementation
###### Source Code Structure

###### Event-store-builder

![Captura de pantalla 2023-12-08 175513](https://github.com/alvarorodriguez88/practice_Weather/assets/145196321/758bdbc8-3c9e-4fe7-9c2d-68379f46a948)

###### Weather-provider

![Captura de pantalla 2023-12-08 175551](https://github.com/alvarorodriguez88/practice_Weather/assets/145196321/ef0799c2-2d4a-4f29-8b6d-6f2afba5c367)

------------

###### Key Features
- Fetching the parameters of interest from the JSON received by the program from the REST API (OpenWeatherMapSupplier):
  
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
- Creating a connection with the broker and sending events to it (JMSWeatherStore):

		public void save(ArrayList<Weather> weathers) throws StoreException {
        try {
            for (Weather weather : weathers) {
                String json = weatherToJson(weather);
                TextMessage text = session.createTextMessage(json);
                producer.send(text);
            }
            System.out.println("Messages already sent...");
        } catch (JMSException e) {
            throw new StoreException(e.getMessage());
        }
  
- Automating the program execution every 6 hours (ProgramController):
  
		public void start(String apiKey) {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                WeatherController controller = null;
                controller = new WeatherController();
                try {
                    controller.execute(apiKey);
                    System.out.println("Execution done...");
                } catch (StoreException e) {
                    System.out.println("ERROR: " + e);
                }
            }
        };
        long interval = 1000 * 60 * 60 * 6;
        timer.schedule(task, 0, interval);
    }

------------

### Testing

###### Testing Strategy
I conducted the project's tests as I progressed. I performed these tests in the main section, aiming to print the results provided by the functions.

I focused on progressing gradually, starting with tests to understand how to connect to the broker and thus send only one event. Later on, I began sending all the events at once and managed to create a durable subscriber in the second module.

The last task I handled was creating a class responsible for automating the program. I set it to a quick interval to observe during peak hours how the data from the islands was added to the file created by the subscriber module.

###### Test Results
At first, I encountered several errors, which I resolved by checking the messages displayed in the console. One of the most challenging issues to identify was when obtaining information from the broker because I was creating a queue instead of a topic.

------------

### Deployment

###### Instructions for Deploying the Program
1. Enter the "practice_Weather" repository and access the latest release (v1.1).

2. Download the 2 zipped files named "event-store-builder" and "weather-provider".

3. Save their contents in the desired working directory.

4. Access the terminal and initialize the local server.

5. Once the local server is initialized, navigate to the working directory where you have the contents of the 2 zip files.

6. While there, in the terminal, initiate the execution of event-store-builder with a command line like this: (INSERT IMAGE). It's important to end the argument, which will be the user's chosen directory when starting the executable, with a "".

7. Next, locate the executable for weather-provider and input a line like this in the terminal: (INSERT IMAGE). It requires the user's unique API key as a parameter.

8. Finally, to verify the program's effectiveness, navigate to the directory the user inserted as an argument when starting event-store-builder to check the outcome of each operation.

------------

### Future Enhancements
The program lends itself to several possible improvements, for instance, the inclusion of data analysis from the stored information to conduct a study. This could be presented through tables and charts, enabling users to visualize the data more conveniently.

------------

### Conclusions
The data stored in the files becomes more accurate as the prediction date approaches. Furthermore, the method of working with the broker provides greater scalability to the program and numerous possibilities for enhancements.

------------

### References
These are some of the resources I used for this project:
- https://stackoverflow.com/
- https://chat.openai.com/
- Materials available on the Moodle of the subject.

# Take Home Exercise - Karan Sharma

In order to complete this exercise, I went through numerous speed bumps due to deprecated code in the original repository - although the original README file said that this project would run with Gradle 7.0, that was unfortunately not the case, with the `build.gradle` file being something resemblant of Gradle 4.8.

Moving on, I decided to use Gradle 6.0 and Spring Boot 1.5.3 (instead of the given old version 1.3.3). Once I got things running on my laptop I quickly coded up the model and controller classes to implement the Rest API. All that was required (as I understood) was a slight change to the Quote model class to incorporate the possibility of a vehicle specification in the quote, as well as some modifications to the controller's POST request endpoint, to calculate the markup prices given the vehicle in the input quote.

I then proceeded to write a randomised test for the new vehicle functionality, that can be found in the test class as an additional test-case method.

## The Frontend

I am a big fan of React.js for web frontend development due to its child-parent component relationships that gives super-efficient automatic re-rendering. I decided to make a react web-app that would serve as the frontend for this quoting system. The UI was pretty quick as I am familiar with Axios - a useful React library for interfacing with Rest APIs. The main problem was integrating the frontend into the Spring Boot project, because I could find very few useful solutions online. Most solutions were for Maven users, but I needed to integrate this into the Gradle project. Eventually I found out that Gradle had nothing to do with it, and in fact the javascript build from my frontend could simply be plonked into a specific directoy in the Spring Boot project, and Spring Boot would automatically serve that frontend on the base url.

#### Mini Extension

Although it was not specified in the task, I simply could not see the app being useful if this feature did not exist. As you will see, I took the initiative of implementing a vehicle validation step so that an incorrect vehicle input on the frontend results in a advisory error message instead of some erroneous price quote.


Althought the source code for the React frontend is no longer required to run the app, I have included it under the `/react_frontend/` top level directory.

## Running the app

To run the app, simply do the following in the command-line (Linux/Macos users):

Change directory to the repo directory
`cd take_home_exercise_intern`

Gradle-build
`gradle build`

Run
`./gradlew bootRun`
_This runs the app on port 1234. Thought a change in port number might be suitable given the popularity of port no. 8080._

To use the app, type this into a browser:
`http://localhost:1234/`

## After-thoughts

This test taught me a lot about java backend development using Spring Boot, and I am proud that I was able to put my existing React.js skills to use to develop a neat, responsive frontend that also hit the two bonus marks - the frontend does not require refreshing the page in order to view the quote price result, and also the frontend is responsive to changes in the dimensions of the window. In fact, my choice to use React was partly driven by how little work I would need to do in order to achieve these two bonus points - that design decision was definitely a crucial one and saved me a load of time.

To take this app further, one could use some open source existing service to get more realistic postal prices between different postcodes, and integrate that into the backend. Also, a database of postal vehicles and their drivers could be added in the future, when the availability of a particular vehicle depends on both the existence of a free vehicle and a free driver for the vehicle...

# Cards With Friends

Server: [https://cardswithfriends.herokuapp.com/](https://cardswithfriends.herokuapp.com/)

[![Build Status](https://travis-ci.org/SWhelan/cardswithfriends.svg?branch=master)](https://travis-ci.org/SWhelan/cardswithfriends)

## EECS 393 Deliverables

### Final Source Code

All application code is in /src/main/java/cardswithfriends.

The starting code is in /src/main/java/Main.java and all it does is register routes and set port number (get randomly assigned on start by heroku or set to a default for localhost). The main method also indicates that our static files will be served from the 'static' directory under resources.

The static files (css, js) are under /src/main/resources/static.
The mustache templates are under /src/main/resources/templates.

The unit tests are under src/test/java/.

The source codes gets compiled/built then run from a target folder that is created at build time.

### Executable/Link

To execute the code run Main.java with no arguments. (Jetty or mvn required.) The dev team has been using eclipse and if you run or debug the Main.java file "as a Java Application" eclipse will handle the Jetty launching for you. There is only one database set up (a heroku mongo db that has been shared by the developers and is used by the live site so if you run this code the same database will be used or you can specify your own mongoDB in DatabaseConnector.java.) 

However none of that work is required as the app is continuously running on a free tier heroku server and can be accessed and used there.

Here is the link: [https://cardswithfriends.herokuapp.com/](https://cardswithfriends.herokuapp.com/)

### Test Results

A sample run of the tests can be found in testresults.txt in the root directory.

To run tests install maven onto your machine: [https://maven.apache.org/download.cgi](https://maven.apache.org/download.cgi). This project is currently using maven 3.3.3 and has not been tested using other versions.

Then in the root directory run the command:
	mvn test
	
Additionally with every commit and push to github TravisCI automatically builds the app and runs the tests. The build is currently:

[![Build Status](https://travis-ci.org/SWhelan/cardswithfriends.svg?branch=master)](https://travis-ci.org/SWhelan/cardswithfriends)

Functional tests are also all passing but as there is no automated documentation system and there are so few of them we have found it simpler to just run all the functional tests before each major demo/release which equates to clicking all the links on the site and playing several full games. These tests were run before submission on December 4th 2015. The tests themselves are enumerated in the Functional Tests document.

#### Code Coverage

Every time the tests are run JaCoCo, a maven code coverage tool, runs a code coverage report. By settings in the pom.xml this coverage report which happens to be a dynamically generated html website is placed into target/site/jacoco-ut/ and can be accessed via index.html.

However for convenience included in the submission on December 4th the site is included under the codecoverage directory.

### Bug Reports

Bugs and issues are tracked via github issues and can be found at: [https://github.com/SWhelan/cardswithfriends/issues](https://github.com/SWhelan/cardswithfriends/issues)

At the time the project was initially submitted (December 4th, 2015) there were no known bugs with the features implemented.

### User Manual

A basic instructional page for rules about how to play the game can be found on the about page: [http://cardswithfriends.herokuapp.com/tutorial](http://cardswithfriends.herokuapp.com/tutorial) but a more in-depth look at how to navigate and use the app/website can be found in the UserManual.docx or if preferred UserManual.pdf in the root directory.


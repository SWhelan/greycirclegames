# Grey Circle Games

## Production Information
Server: [http://www.greycirclegames.com/](http://www.greycirclegames.com/)

[![Build Status](https://travis-ci.org/SWhelan/greycirclegames.svg?branch=master)](https://travis-ci.org/SWhelan/greycirclegames)

## Local Development
To develop clone or download code.
Create local.properties file in config folder with mongo database urls.
Run or debug the Main.java file from IDE/java and then visit http://localhost:4567/.

When a change is made to a template file the server will need to be restarted but otherwise the server can stay on through changes.

## Tests
Install Maven
Run `mvn test` in this root of this repo.

## Heroku Deploy
Run `mvn clean heroku:deploy`
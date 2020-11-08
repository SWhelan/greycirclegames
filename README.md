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
Run `mvn clean test` in this root of this repo.

## Heroku Deploy
Run `mvn clean heroku:deploy -P staging` or for production `mvn clean heroku:deploy -P production`
The `clean` is very important because without it the heroku build will upload all the way to heroku and then fail at some static imports in TemplateHandler for spark.Spark.before.

## Eclipse
If running eclipse you may want to run `mvn clean eclipse:eclipse` to compile classpath files and such for eclipse to use. Run this when things appear to be running old code or eclipse isn't saying something is an error but maven is reporting them. I think build automatically turned on in eclipse is also good.
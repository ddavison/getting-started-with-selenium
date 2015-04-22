[![star](http://github-svg-buttons.herokuapp.com/star.svg?user=ddavison&repo=getting-started-with-selenium)](http://github.com/ddavison/getting-started-with-selenium)
[![fork](http://github-svg-buttons.herokuapp.com/fork.svg?user=ddavison&repo=getting-started-with-selenium)](http://github.com/ddavison/getting-started-with-selenium/fork)

# Getting Started
To get right up and started,  you can [download the project (zip)](https://github.com/ddavison/getting-started-with-selenium/archive/master.zip) or you can checkout the project from github. If you don't know how, [this should help](http://git-scm.com/book/en/Git-Basics-Getting-a-Git-Repository).

**Prerequisites**
- Maven (if using eclipse, install Maven Integration for Eclipse)
- jUnit 4
- Java
- WebDriver (Chrome, Firefox, IE, etc)

### Drivers
Currently, not all drivers are not packaged with this project, but they may be in the future!
- [Chromedriver](http://chromedriver.storage.googleapis.com/index.html) (now is packaged with the project)
- Firefox driver IS actually packaged with the Selenium jar.
- [IEDriver](https://code.google.com/p/selenium/downloads/list)

Launch your IDE, and under ```src/tests/java``` you'll find a file under the ```functional``` package.  This is a very short a simple test. 
If you do not have Chromedriver installed, just switch the browser to ```FIREFOX``` and right click the file and ```Run As -> jUnit Test```

This project is a sample project that uses the [Conductor Framework](http://conductor.ddavison.io).  You can download this project to see quickly how you can layout your tests, and libraries.

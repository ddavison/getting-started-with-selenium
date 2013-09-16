# Getting Started
To get right up and started,  you need to checkout the project from github, if you don't know how, [this should help](http://git-scm.com/book/en/Git-Basics-Getting-a-Git-Repository).
- Maven (if using eclipse, install Maven Integration for Eclipse)
- jUnit 4
- Java
- WebDriver (Chrome, Firefox, IE, etc)

### Drivers
Currently, not all drivers are not packaged with this project, but they may be in the future!
- [Chromedriver](https://code.google.com/p/chromedriver/downloads/list)
- Firefox driver IS actually packaged with the Selenium jar.
- [IEDriver](https://code.google.com/p/selenium/downloads/list)

Launch your IDE, and under ```src/tests/java``` you'll find a file under the ```functional``` package.  This is a very short a simple test. 
If you do not have Chromedriver installed, just switch the browser to ```FIREFOX``` and right click the file and ```Run As -> jUnit Test```

# Goals
The primary goals of this project are to...
- Take advantage of method chaining, to create a fluent interface.
- Abstract the programmer from bloated scripts resulting from using too many css selectors.
- Provide a quick and easy framework in Selenium 2 using Java, to get started writing scripts.

# In-line validations
This is one of the most important features that I want to _*accentuate*_.
- validateText
- validateChecked
- validateUnchecked
- validatePresent
- validateNotPresent
- validateTextPresent
- validateTextNotPresent

All of these methods are able to be called in-line, and fluently without ever having to break your tests.

# Implicit Waiting
In addition to the Selenium 2 implicit waiting, the ```AutomationTest``` class extends on this concept by implenting a sort of ```waitFor``` functionality.

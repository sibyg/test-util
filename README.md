
# Test Util #
Helper APIs on top of day to day testing work

This project has sample codes to provide quick reference for java-selenium based testing. 

It has examples for both web and mobile versions.

Example, there is a sample.feature file to run with cucumber-jvm


## Prequisites##
1. This project requires drivers for the browsers to be on PATH variable
2. For mobile tesing using appium, you need to start Appium server before running Java based Appium client code (see, SafariIOSAppiumExample)

It is quite easy,

Using Node.js
```bash
$ npm install -g appium
$ appium &
```

Using the App
    a. [Download the Appium app](https://bitbucket.org/appium/appium.app/downloads/)
    b. Run it!



# References #
1. [Running Appium Tests](http://appium.io/slate/en/master/?ruby#running-tests)
2. [Running Android on Appium](https://eveningsamurai.wordpress.com/2014/05/16/mobile-web-automation-with-appium/)
3. [AVD Command Line](http://developer.android.com/tools/devices/managing-avds-cmdline.html)
4. [Nice Examples](http://meettheqagirl.blogspot.co.uk/)

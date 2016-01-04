var assert = require('assert')
var webdriver = require('selenium-webdriver')

var driver = new webdriver.Builder().
   withCapabilities(webdriver.Capabilities.chrome()).
   build();

driver.get('http://www.google.com');
driver.findElement(webdriver.By.name('q')).sendKeys('webdriver');
driver.findElement(webdriver.By.name('btnG')).click();

//driver.wait(function() {
// return driver.getTitle().then(function(title) {
//   return title === 'webdriver - Google Search';
// });
//}, 1000);

// assert on a value
driver.getTitle().then(function(value) {
    assert.equal(value, 'Google')
})

/*
You can register observers with a promise using the then() function.
This function takes two (optional) functions as arguments: a callback to invoke with the resolved value
and an errback to invoke if the promise was rejected. The observer will be invoked immediately if the promise
has already been resolved. Otherwise, it will be invoked as soon as it is resolved. If multiple observers have been
registered, they will be called in the order added:
*/

var promise = driver.getTitle();

promise.then(function(title) {
 console.log("title is: " + title);
});

promise.then(function(title) {
 console.log("title still is: " + title);
})

promise.controlFlow().on('uncaughtException', function(e) {
 console.error('Unhandled error: ' + e);
})

promise.rejected(new Error('boom'));
console.log('Leaving rejection unhandled');



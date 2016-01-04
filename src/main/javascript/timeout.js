/*
There are two halves to WebDriver's promise API: a promise, and a deferred.
A deferred object is a special version of a promise that provides two additional functions
for setting a promise's value: fulfill() and reject(). Calling fulfill() will set the promise
value and invoke the callback chain. Likewise, call reject() to set an error and trigger any error-handlers
*/
var webdriver = require('selenium-webdriver')

function timeout(ms) {
 var d = webdriver.promise.defer();
 var start = Date.now();
 setTimeout(function() {
   d.fulfill(Date.now() - start);
 }, ms);
 return d.promise;
}

function printElapsed(ms) {
 console.log('time: ' + ms + ' ms');
}

timeout(750).then(printElapsed);
timeout(500).then(printElapsed);
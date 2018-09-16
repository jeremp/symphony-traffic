# Symphony-traffic

Experiments with Micronaut [Micronaut](http://micronaut.io/) and [Symphony](https://symphony.com/)

Reading traffic jams data on here.com REST API and posting into a Symphony chat (using webhook) if traffic is slow.

## How to build ?

`mvn clean package`

## How to run ?

You'll need
* a Symphony webhook URL
* a here.com REST API id
* a here.com REST API code

```
java -jar
-Dmyapp.webhook.id=YOUR_SYMPHONY_WEBHOOK_ID
-Dmyapp.here.app.code=YOUR_HERE_APP_CODE
-Dmyapp.here.app.id=YOUR_HERE_APP_ID
    target/symphony-traffic-0.1.jar
```






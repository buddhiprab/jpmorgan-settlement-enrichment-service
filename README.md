## Settlements Enrichment Service

Developed by: Buddhi Prabhathtus


#### How to run the program.
This API has been developed using below tech stack

* Java 8
* Maven
* Spring Boot 2.3.0
* Spring Boot Embedded Tomcat
* H2 Embedded Database
* lombok 

Assuming JAVA_HOME environment variable already set to Java 8 sdk in your environment and you have maven installed.

###### 1. using IDE

- you can use your favorite IDE (ex. IntelliJ IDEA)
- extract the zip file and import the project sources as a maven project
- create a Spring Boot running config (Intellij IDEA will create automatically when import the sources)
- click the run button to run the Application, it will run the API on port 8080

###### 2. using maven commandline tool on linux, mac, windows

extract the zip file

```
# go to the zip extracted directory
$ cd <extracted dir>

# build the jar using maven
$ mvn package

# go to target directory
$ cd target

# run the API using java -jar command, it will run the API on port 8080
$ java -jar settlement-enrichment-service-0.0.1-SNAPSHOT.jar
```

API uses the Spring Boot Embedded Tomcat by default as Web Server and Embedded H2 database as the DB.

#### How to run all the unit tests.

```$ mvn test```

#### H2 Database

You can access the H2 Embedded Database after starting the API

http://localhost:8080/h2-console
```
jdbc url:   jdbc:h2:mem:testd
user name:  sa
password:   password
```

database is initialized using the schema.sql in resources directory

#### API end points

##### 1. Create a new market settlement message

> **Accepts:** Trade Id, SSI Code, Amount, Currency, Value Date
>
>**Produces:** Market Settlement Message

`POST URI /market/settlement_message/create`

| field | mandatory |
| ------ | ------ |
| tradeId | Yes |
| ssiCode | Yes |
| amount | Yes |
| currency | Yes |
| valueDate | Yes |


##### example 

######  request:
```
URI: http://localhost:8080/market/settlement_message/create
Method: HTTP POST
Content-Type: application/json
 
{
	"tradeId":"16846548",
	"ssiCode":"OCBC_DBS_2",
	"amount":12894.65,
	"currency":"USD",
	"valueDate":"20022020"
}
```

###### response:

```
Http Status: 200

{
    "tradeId": "16846548",
    "messageId": "d4953a25-7f7f-4e4f-9866-d8992087460e",
    "amount": 12894.65,
    "valueDate": "20022020",
    "currency": "USD",
    "payerParty": {
        "accountNumber": "438421",
        "bankCode": "OCBCSGSGXXX"
    },
    "receiverParty": {
        "accountNumber": "05461369",
        "bankCode": "DBSSSGSGXXX"
    },
    "supportingInformation": "BNF:FFC-482315"
}
```

#### Validations

##### invalid json

###### request: 

send some invalid json

```
URI: http://localhost:8080/market/settlement_message/create
Method: HTTP POST
Content-Type: application/json

	"tradeId":"16846548",
	"ssiCode":"OCBC_DBS_1",
	"amount":12894.65,
	"currency":"USD",
	"valueDate":"20022020"
}
```

###### response:

```
Http Status: 400

{
    "errorCode": "ERR_API_0002",
    "errorMessage": "Invalid JSON content",
    "fieldErrors": null
}
```

##### Invalid SSI Code validation

###### request: 

send an invalid SSI Code which is not in DB, API will give validation error

```
URI: http://localhost:8080/market/settlement_message/create
Method: HTTP POST
Content-Type: application/json

{
	"tradeId":"16846548",
	"ssiCode":"OCBC_DBS_3",
	"amount":12894.65,
	"currency":"USD",
	"valueDate":"20022020"
}
```

###### response:

```
Http Status: 400

{
    "errorCode": "ERR_API_VAL",
    "errorMessage": "invalid SSI Code",
    "fieldErrors": null
}
```

##### Duplicate Trade ID validation

###### request:

by sending the same tradeId on the payload more than once, API will give validation error

```
URI: http://localhost:8080/market/settlement_message/create
Method: HTTP POST
Content-Type: application/json

{
	"tradeId":"16846548",
	"ssiCode":"OCBC_DBS_2",
	"amount":12894.65,
	"currency":"USD",
	"valueDate":"20022020"
}
```

###### response:

```
Http Status: 400

{
    "errorCode": "ERR_API_VAL",
    "errorMessage": "Trade request already exists for Trade Id",
    "fieldErrors": null
}
```

##### Mandatory field validations and format validations

###### request:

send an empty request or send a request with missing mandatory fields, API will give validation errors

```
URI: http://localhost:8080/market/settlement_message/create
Method: HTTP POST
Content-Type: application/json

{}
```

###### response:

```
Http Status: 400

{
    "errorCode": "ERR_API_VAL",
    "errorMessage": "Request have validation errors",
    "fieldErrors": [
        {
            "fieldCode": "tradeId",
            "errorDesc": "ERR_API_VAL Field 'tradeId' is mandatory"
        },
        {
            "fieldCode": "ssiCode",
            "errorDesc": "ERR_API_VAL Field 'ssiCode' is mandatory"
        },
        {
            "fieldCode": "amount",
            "errorDesc": "ERR_API_VAL Field 'amount' is mandatory"
        },
        {
            "fieldCode": "amount",
            "errorDesc": "ERR_API_VAL Invalid format, value must be a number with optional 2 decimals"
        },
        {
            "fieldCode": "currency",
            "errorDesc": "ERR_API_VAL Field 'currency' is mandatory"
        },
        {
            "fieldCode": "valueDate",
            "errorDesc": "ERR_API_VAL Field 'valueDate' is mandatory"
        }
    ]
}
```


##### 2. Fetch an existing market settlement message

>**Accepts:** Trade ID
>
>**Produces:** Existing Market Settlement Message

`GET URI: /market/settlement_message/{tradeId}`

##### example 

######  request:

```
URI: http://localhost:8080/market/settlement_message/16846548
method: HTTP GET
```
 
######  response:
 
 ```
 Http Status: 200
 
 {
     "tradeId": "16846548",
     "messageId": "d4953a25-7f7f-4e4f-9866-d8992087460e",
     "amount": 12894.65,
     "valueDate": "20022020",
     "currency": "USD",
     "payerParty": {
         "accountNumber": "438421",
         "bankCode": "OCBCSGSGXXX"
     },
     "receiverParty": {
         "accountNumber": "05461369",
         "bankCode": "DBSSSGSGXXX"
     },
     "supportingInformation": "BNF:FFC-482315"
 }
 ```
 
##### send invalid tradeId

######  request:
 
```
URI: http://localhost:8080/market/settlement_message/1
method: HTTP GET
``` 

######  response:
 
```
Http Status: 400
 
{
     "errorCode": "ERR_API_VAL",
     "errorMessage": "invalid Trade Id",
     "fieldErrors": null
}
 ```


####  Assumptions and special cases handling

1. Assumed Trade ID is unique per Market Settlement Message. 
If duplicate Trade ID passed for creating new market settlement message, a validation error will be returned.

2. to create a new market settlement message, must pass a valid SSI Code.

3. Validation rules applied for the Json request for mandatory fields and format check using regex. 
And the validation framework can support for complex validations in future.


#### Error Messages

| errorCode | errorMessage |
| ------ | ------ |
| ERR_API_0001 | Sorry we can't process your request |
| ERR_API_0002 | invalid Reqeust JSON |
| ERR_API_VAL | invalid SSI Code |
| ERR_API_VAL | invalid Trade Id |
| ERR_API_VAL | Trade request already exists for Trade Id |
| ERR_API_VAL | Request have validation errors |
| ERR_API_VAL | Object name mandatory |
| ERR_API_VAL | Field {field name} is mandatory |
| ERR_API_VAL | Invalid format, value must be a number with optional 2 decimals |
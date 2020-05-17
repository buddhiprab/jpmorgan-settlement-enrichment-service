## Settlements Enrichment Service

#### How to run the program.
This API has been developed using below tech stack

Java 8, Maven, Spring Boot 2.3.0, H2 Embedded Database

##### To run the API 

- you can use your favorite IDE (ex. IntelliJ IDEA)
- import the project as a maven project
- create a Spring Boot running config (Intellij IDEA will create automatically when import the sources)
- run the Application 

API uses the Embedded Tomcat by default as Web Server and Embedded H2 database as the DB.

#### How to run all the unit tests.
```$ mvn test```


#### API end points

#####1. Create a new market settlement message

> **Accepts:** Trade Id, SSI Code, Amount, Currency, Value Date
>
>**Produces:** Market Settlement Message

##### example 

 request:
  
 URI: http://localhost:8080/market/settlement_message/create
 
 method: HTTP POST

```json
{
	"tradeId":"16846548",
	"ssiCode":"OCBC_DBS_2",
	"amount":12894.65,
	"currency":"USD",
	"valueDate":"20022020"
}
```

response:

```json
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

##### Validations



##### 2. Fetch an existing market settlement message

>**Accepts:** Trade ID
>
>**Produces:** Existing Market Settlement Message

 request:
  
 URI: http://localhost:8080/market/settlement_message/16846548
 
 method: HTTP GET
 
 response:
 
 ```json
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


####  Assumptions and special cases handling

1. Assumed Trade ID is unique per Market Settlement Message. 
If duplicate Trade ID passed for creating new market settlement message, a validation error will be returned.

2. Validation rules applied for the Json request for mandatory fields and format check using regex. 
And the validation framework can support for complex validations in future.
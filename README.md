## Settlements Enrichment Service

#### How to run the program.

#### How to run all the unit tests.
$ mvn test


#### API end points

#####1. Create a new market settlement message

> **Accepts:** Trade Id, SSI Code, Amount, Currency, Value Date
>
>**Produces:** Market Settlement Message

##### example 

 request:

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

```

#####2. Fetch an existing market settlement message

>**Accepts:** Trade ID
>
>**Produces:** Existing Market Settlement Message

####  Special cases handling

1. 
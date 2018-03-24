# coinmixer

Yet another coin mixer


## Build
``` 
mvn clean install 
```

## Run
``` 
cp target/coinmixer*gz ~/
cd
tar -xvf coinmixer*gz
mv coinmixer* coinmixer
./coinmixer/bin/launcher.sh
```
## API
Base URL: http://${host}:17320/coinmixer/v1

| Method | URL       | Payload  Example |
|-------|:-----------|:------------|
| POST | /addresses/ | {"source_addresses":["address1", "address2"] |
| GET | /addresses/{address} | N/A |
| POST | /withdrawal/{address} | None|

### Examples

Acquire a deposit address
```
[vregalado@localhost bin]$ curl -XPOST -H "Content-type: application/json" -d '{"source_addresses":["vane01", "vane02"]}' http://localhost:17320/coinmixer/v1/addresses/
{
  "total_balance" : 0.0,
  "deposit_address" : "hYigQXSDutdBosoizulJ",
  "addresses_with_balance" : [ {
    "address" : "vane01",
    "balance" : 0.0
  }, {
    "address" : "vane02",
    "balance" : 0.0
  } ]
}
```
Get source addresses and deposit address basic info
```
[vregalado@localhost bin]$ curl -XGET -H "Content-type: application/json" http://localhost:17320/coinmixer/v1/addresses/vane01
{
  "total_balance" : 0.0,
  "deposit_address" : "hYigQXSDutdBosoizulJ",
  "addresses_with_balance" : [ {
    "address" : "vane01",
    "balance" : 0.0
  }, {
    "address" : "vane02",
    "balance" : 0.0
  } ]
}
```
Initiate a withdrawl and check balances
```
[vregalado@localhost bin]$ curl -XPOST -H "Content-type: application/json" http://localhost:17320/coinmixer/v1/withdrawal/vane01
{
  "withdrawn" : true
}
[vregalado@localhost bin]$ curl -XGET -H "Content-type: application/json" http://localhost:17320/coinmixer/v1/addresses/vane01
{
  "total_balance" : 50.0,
  "deposit_address" : "hYigQXSDutdBosoizulJ",
  "addresses_with_balance" : [ {
    "address" : "vane01",
    "balance" : 33.67
  }, {
    "address" : "vane02",
    "balance" : 16.33
  } ]
}
```






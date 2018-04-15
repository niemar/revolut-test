
Money Transfer API
Produces and consumes only application/json.

    GET     /accounts/{id}
    Response body
    {
        "id": "e6e20fb1-4b59-492a-8054-052d74f2a1f7",
        "balance": 100,
        "currency": "USD"
    }
    
    GET     /accounts
    Response body
    [
        {
            "id": "ccf54eeb-b206-4ff4-95e5-141b6a398d28",
            "balance": 100,
            "currency": "USD"
        },
        {
            "id": "e6e20fb1-4b59-492a-8054-052d74f2a1f7",
            "balance": 100,
            "currency": "USD"
        }
    ]
    
    POST    /accounts
    Request body
    {
            "balance":100,
            "currency": "USD"
    }
    Response body
    {
        "id": "e6e20fb1-4b59-492a-8054-052d74f2a1f7",
        "balance": 100,
        "currency": "USD"
    }    
    
    PUT     /accounts/{id}
    Request body
    {
            "balance":100,
            "currency": "USD"
    }
    Response body
    {
        "id": "e6e20fb1-4b59-492a-8054-052d74f2a1f7",
        "balance": 100,
        "currency": "USD"
    }
        
    DELETE  /accounts/{id}
  
    GET     /transfers/{id}
    Response body
    {
        "fromAccount": "e6e20fb1-4b59-492a-8054-052d74f2a1f7",
        "toAccount": "ccf54eeb-b206-4ff4-95e5-141b6a398d28",
        "amount": 30,
        "currency": "USD",
        "id": "05653af7-be20-4211-ad44-f780f992f8a2",
        "status": "COMPLETED"
    }
    
    GET     /transfers
    Response body
    [
        {
            "fromAccount": "e6e20fb1-4b59-492a-8054-052d74f2a1f7",
            "toAccount": "ccf54eeb-b206-4ff4-95e5-141b6a398d28",
            "amount": 30,
            "currency": "USD",
            "id": "05653af7-be20-4211-ad44-f780f992f8a2",
            "status": "COMPLETED"
        },
        {
            "fromAccount": "ccf54eeb-b206-4ff4-95e5-141b6a398d28",
            "toAccount": "e6e20fb1-4b59-492a-8054-052d74f2a1f7",
            "amount": 30,
            "currency": "EUR",
            "id": "58e228cf-a585-4ae8-8560-aaeb4dc15aed",
            "status": "DECLINED"
        }
    ]
    
    POST    /transfers
    Request body
    {
            "fromAccount": "ccf54eeb-b206-4ff4-95e5-141b6a398d28",
            "toAccount": "e6e20fb1-4b59-492a-8054-052d74f2a1f7",
            "amount": 30.0,
            "currency": "EUR"
    }
    Response body
    {
        "fromAccount": "ccf54eeb-b206-4ff4-95e5-141b6a398d28",
        "toAccount": "e6e20fb1-4b59-492a-8054-052d74f2a1f7",
        "amount": 30,
        "currency": "EUR",
        "id": "f3113acf-3af1-4b7b-8691-10c331be5fcb",
        "status": "DECLINED"
    }
    
    This method cancel transfer, does opposite transfer to revert account's balance.
    DELETE  /transfers/{id}
    Response body
    {
        "fromAccount": "e6e20fb1-4b59-492a-8054-052d74f2a1f7",
        "toAccount": "ccf54eeb-b206-4ff4-95e5-141b6a398d28",
        "amount": 30,
        "currency": "USD",
        "id": "05653af7-be20-4211-ad44-f780f992f8a2",
        "status": "COMPLETED"
    }



TODO
 - consider using DTO classes, especially for Transfer 
 - more test for AccountResource
 - put money and currency in one POJO
 - more integration tests
 - don't return array for transfers/ and accounts resources - bad practice


OrderRequest JSON to create order via REST API:

-) http://localhost:8081/api/order  [direct to product service]

-) http://localhost:8000/api/order  [via API Gateway]



# bad request: no product in stock error:

{
  "orderLineItemsDtoList": [
    {
      "skuCode": "iphone_13_red",
      "price": 1200,
      "quantity": 1
    }
  ]
}



# success request: product is in the stock:

{
  "orderLineItemsDtoList": [
    {
      "skuCode": "iphone_13",
      "price": 1200,
      "quantity": 1
    }
  ]
}

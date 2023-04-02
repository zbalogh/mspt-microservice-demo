import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { OrderCreatedResponse } from "../models/order-created.model";
import { OrderRequest } from "../models/order-request.model";
import { OrderResponse } from "../models/order-response.model";

@Injectable({
    providedIn: 'root'
})
export class OrderService {

    constructor(private readonly http: HttpClient) { }

    public getMyOrders(): Observable<OrderResponse[]>
    {
        return this.http.get<OrderResponse[]>('/api/order');
    }

    public sendMyOrder(order: OrderRequest): Observable<OrderCreatedResponse>
    {
        /*
        // https://stackoverflow.com/questions/57404556/angular-httpclient-error-with-empty-200-201-response-always-calls-json-parse
        var options = {
            headers: {'Content-Type': 'application/json; charset=utf-8'},
            observe: "body",
            responseType: "text" as "json"
        }
        */
        // NOTE: we changed the response type from TEXT to JSON in the Order API,
        // so now we do not need to specify the options.
        return this.http.post<OrderCreatedResponse>('/api/order', order);
    }

}

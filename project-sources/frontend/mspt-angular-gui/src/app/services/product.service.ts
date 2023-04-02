import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { ProductResponse } from "../models/product-response.model";

@Injectable({
    providedIn: 'root'
})
export class ProductService
{
    constructor(private readonly http: HttpClient) { }

    public getProducts(): Observable<ProductResponse[]>
    {
        return this.http.get<ProductResponse[]>('/api/product');
    }
}

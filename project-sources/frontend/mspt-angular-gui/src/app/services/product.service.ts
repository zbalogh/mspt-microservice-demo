import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { ProductRequest } from "../models/product-request.model";
import { ProductResponse } from "../models/product-response.model";

@Injectable({
    providedIn: 'root'
})
export class ProductService
{
    private readonly apiUrl = '/api/product';

    constructor(private readonly http: HttpClient) { }

    // GET: Get all products
    public getProducts(): Observable<ProductResponse[]>
    {
        return this.http.get<ProductResponse[]>(this.apiUrl);
    }

    // GET: Get a single product by ID
    getProductById(id: string): Observable<ProductResponse>
    {
        const url = `${this.apiUrl}/${id}`;
        return this.http.get<ProductResponse>(url);
    }

    // POST: Create a new product
    createProduct(product: ProductRequest): Observable<ProductResponse>
    {
        return this.http.post<ProductResponse>(this.apiUrl, product);
    }

    // PUT: Update an existing product
    updateProduct(product: ProductRequest): Observable<ProductResponse>
    {
        return this.http.put<ProductResponse>(this.apiUrl, product);
    }

    // DELETE: Delete a product
    deleteProduct(id: string): Observable<void>
    {
        const url = `${this.apiUrl}/${id}`;
        return this.http.delete<void>(url);
    }
}

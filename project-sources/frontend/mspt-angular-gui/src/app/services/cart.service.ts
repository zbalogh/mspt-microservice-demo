import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { ShoppingCart } from "../models/shopping-cart.model";

const CART_LOCAL_STORAGE_NAME = 'MSPT_SHOPPING_CART';

@Injectable({
    providedIn: 'root'
})
export class CartService {

    constructor(private readonly http: HttpClient)
    { }

    /**
     * Deprecated function. We use the server-side caching.
     */
    public getMyCartFromLocalStorage(): ShoppingCart
    {
        // read data from local storage
        var cartData = localStorage.getItem(CART_LOCAL_STORAGE_NAME);

        if (cartData) {
            // we have cart data in the local storage
            // convert it to ShoppingCart object
            var cart = JSON.parse(cartData) as ShoppingCart;

            // convert the 'lastUpdatedAt' field to JavaScript Date object
            cart.lastUpdatedAt = new Date(cart.lastUpdatedAt);
            
            // check if the cart is expired: if the last updated time is too old
            var nowTime = new Date().getTime();
            var cartTime = cart.lastUpdatedAt.getTime();
            var diff = nowTime - cartTime;

            if (diff > (30 * 60 * 1000) ) {
                // the cart is expired, so we clear it
                this.clearMyCartInLocalStorage();
                // we return an empty cart object
                return this.createEmptyCart();
            }
            else {
                // the cart is still valid (not expired)
                return cart;
            }
        }
        else {
            // no cart in the storage
            // we return an empty cart object
            return this.createEmptyCart();
        }
    }

    /**
     * Deprecated function. We use the server-side caching.
     */
    public setMyCartToLocalStorage(cart: ShoppingCart)
    {
        if (cart) {
            // set the time for lastUpdate field
            cart.lastUpdatedAt = new Date();
            // convert the ShoppingCart object into JSON string
            var cartData = JSON.stringify(cart);
            // save data into local storage
            localStorage.setItem(CART_LOCAL_STORAGE_NAME, cartData);
        }
    }

    /**
     * Deprecated function. We use the server-side caching.
     */
    public clearMyCartInLocalStorage()
    {
        // remove the Shopping Cart from the local storage
        localStorage.removeItem(CART_LOCAL_STORAGE_NAME);
    }

    private createEmptyCart(): ShoppingCart
    {
        // create an empty shopping cart instance (no items in the cart)
        var cart: ShoppingCart = {
            lastUpdatedAt: new Date(),
            cartItems: []
        };
        return cart;
    }

    public getMyCart(): Observable<ShoppingCart>
    {
        return this.http.get<ShoppingCart>('/api/cart');
    }

    public setMyCart(cart: ShoppingCart): Observable<ShoppingCart>
    {
        return this.http.post<ShoppingCart>('/api/cart', cart);
    }

    public clearMyCart(): Observable<void>
    {
        return this.http.delete<void>('/api/cart');
    }

}

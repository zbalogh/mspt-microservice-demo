import { ShoppingCartItem } from "./shopping-cart-item.model";

export interface ShoppingCart
{
    lastUpdatedAt: Date;

    cartItems: ShoppingCartItem[];

}

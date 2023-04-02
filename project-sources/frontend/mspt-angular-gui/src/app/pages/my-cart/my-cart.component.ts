import { Component, OnInit } from '@angular/core';
import { OrderRequest } from 'src/app/models/order-request.model';
import { OrderLineItem } from 'src/app/models/orderline-item.model';
import { ShoppingCartItem } from 'src/app/models/shopping-cart-item.model';
import { ShoppingCart } from 'src/app/models/shopping-cart.model';
import { CartService } from 'src/app/services/cart.service';
import { OrderService } from 'src/app/services/order.service';
import { faSpinner, faTrashAlt } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-my-cart',
  templateUrl: './my-cart.component.html',
  styleUrls: ['./my-cart.component.css']
})
export class MyCartComponent implements OnInit {

  shoppingCart: ShoppingCart;

  loaded = false;

  isCheckoutProceeding = false;

  orderNumber = '';

  orderCheckoutSent = false;  // set to TRUE if order request sent

  orderErrorMessage = '';

  // font-awesome icons
  faSpinnerIcon = faSpinner;
  faTrashAltIcon = faTrashAlt;

  constructor(private readonly cartService: CartService,
              private readonly orderService: OrderService)
  {
    // initially set empty ShoppingCart
    this.shoppingCart = {} as ShoppingCart;
    this.shoppingCart.cartItems = [];
  }

  ngOnInit(): void
  {
    this.cartService.getMyCart().subscribe({
      next: data => {
        console.log('Loaded the shopping cart.');
        this.shoppingCart = data;
        this.loaded = true;
      },
      error: err => {
        console.log('Unable to get the shopping cart from the backend API server. ' + err);
      }
    });
  }

  isMyCartEmpty(): boolean
  {
    const cart = this.shoppingCart;

    if (cart && cart.cartItems && cart.cartItems.length > 0) {
      // we have items in the Shopping Cart
      return false;
    }
    else {
      // no items in the Shopping Cart
      return true;
    }
  }

  cartItemTotalPrice(cartItem: ShoppingCartItem)
  {
    var quantity = cartItem.quantity > 0 ? cartItem.quantity : 1;
    return quantity * cartItem.price; 
  }

  totalCartPrice()
  {
    var total = 0;
    for (var item of this.shoppingCart.cartItems) {
      total+= item.price * item.quantity;
    }
    return total;
  }

  totalCartQuantity()
  {
    var total = 0;
    for (var item of this.shoppingCart.cartItems) {
      total+=item.quantity;
    }
    return total;
  }

  removeProductFromShoppingCart(cartItem: ShoppingCartItem)
  {
    if (this.shoppingCart.cartItems.length > 0)
    {
        // finding the index for this cart item
        var cartItemindex = this.shoppingCart.cartItems.findIndex(ci => {
            // looking for an element where the productId equals
            return ci.productId === cartItem.productId;
        });

        if (cartItemindex >= 0) {
            // remove item from the array with its index
            this.shoppingCart.cartItems.splice(cartItemindex, 1);

            console.log('Removed the selected product from the cart: ' + cartItem.productId + ', ' + cartItem.productName);

            if (this.shoppingCart.cartItems.length > 0)
            {
              // The cart still contains item(s): we update the shopping cart in the service
              this.cartService.setMyCart(this.shoppingCart).subscribe({
                  next: (data) => {
                    console.log('Updated the shopping cart.');
                    // set the local shopping cart object
                    this.shoppingCart = data;
                  },
                  error: (err) => {
                    console.log('Unable to update the shopping cart in the backend API server. ' + err);
                  }
              });
            }
            else {
              // The last item has been removed, so the cart is now empty: we clear/remove cart in the service
              this.cartService.clearMyCart().subscribe(() => {
                console.log("ShoppingCart cleared.");
              });
            }
        }
    }
  }

  clearShoppingCart()
  {
    // clear the local variable
    this.shoppingCart.cartItems = [];
    // clear the shopping car in the service
    this.cartService.clearMyCart().subscribe(() => {
      console.log("ShoppingCart cleared.");
    });
  }

  proceedOrderCheckout()
  {
      if (this.isCheckoutProceeding) {
        // it looks like an checkout process is already running
        // meanwhile we prevent execution again
        console.log('Checkout/Order is already running.');
        return;
      }
      
      // we need to proceed the order checkout
      if (this.shoppingCart.cartItems.length > 0) {
          // turn on the flag
          this.isCheckoutProceeding = true;

          // convert shopping cart to OrderRequest
          var order: OrderRequest = {
            userid: '', // do not need it because Order API checks the user from the Bearer (JWT) token
            orderLineItemsDtoList: []
          };

          // set order items
          for (var item of this.shoppingCart.cartItems) {
              // create order item object
              var orderItem: OrderLineItem = {
                skuCode: item.skuCode,
                price: item.price,
                quantity: item.quantity
              }
              // add to the order
              order.orderLineItemsDtoList.push(orderItem);
          }

          // send my order to Order API
          this.orderService.sendMyOrder(order).subscribe({
            next: result => {
              // order sent successfully
              this.orderCheckoutSent = true;
              this.orderNumber = result.orderNumber;
              this.orderErrorMessage = '';
              // turn off the flag
              this.isCheckoutProceeding = false;
              // clear shopping cart via the service
              // but we keep the local reference to display the cart items
              this.cartService.clearMyCart().subscribe(() => {
                console.log("ShoppingCart cleared.");
              });
            },
            error: error => {
              // error when sending the order
              console.log('Error occurred while sending my order.' + error);
              this.orderCheckoutSent = true;
              this.orderNumber = '';
              this.orderErrorMessage = 'Unable to checkout your order: ' + error;
              // turn off the flag
              this.isCheckoutProceeding = false;
            }
          });
      }
  }

}

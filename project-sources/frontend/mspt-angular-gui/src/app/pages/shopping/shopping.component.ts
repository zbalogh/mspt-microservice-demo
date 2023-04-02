import { Component, OnInit } from '@angular/core';
import { ProductResponse } from 'src/app/models/product-response.model';
import { ShoppingCartItem } from 'src/app/models/shopping-cart-item.model';
import { ShoppingCart } from 'src/app/models/shopping-cart.model';
import { CartService } from 'src/app/services/cart.service';
import { ProductService } from 'src/app/services/product.service';
import { ToastService } from 'src/app/services/toast.service';
import { faSpinner, faCartShopping } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-shopping',
  templateUrl: './shopping.component.html',
  styleUrls: ['./shopping.component.css']
})
export class ShoppingComponent implements OnInit {

  products: ProductResponse[] = [];

  shoppingCart: ShoppingCart;

  // a flag that indicates that products are loaded from the remote service.
  loaded = false;

  // a flag that indicates that the adding a product to the shopping cart is being processed.
  isAddingToCartProcess = false;

  // font-awesome icons
  faSpinnerIcon = faSpinner;
  faCartShoppingIcon = faCartShopping;

  errorMessage = '';

  constructor(
          private readonly productService: ProductService,
          private readonly cartService: CartService,
          private readonly toastService: ToastService
  ) {
    // initially set empty ShoppingCart
    this.shoppingCart = {} as ShoppingCart;
    this.shoppingCart.cartItems = [];
  }

  ngOnInit(): void
  {
    this.getProducts();
    this.getMyShoppingCart();
  }

  private getProducts()
  {
      this.productService.getProducts().subscribe({
        next: (data) => {
          console.log('Loaded products.');
          this.products = data;
          this.loaded = true;
          this.errorMessage = '';
          this.initProductQuantity();
        },
        error: (error) => {
          this.errorMessage = 'Unable to get products. ' + error;
          console.log('Unable to get products from the backend API server. ' + error);
        }
      });
  }

  private getMyShoppingCart()
  {
    this.cartService.getMyCart().subscribe({
      next: data => {
        console.log('Loaded the shopping cart.');
        this.shoppingCart = data;
      },
      error: err => {
        console.log('Unable to get the shopping cart from the backend API server. ' + err);
      }
    });
  }

  private initProductQuantity()
  {
    // set the quantity field to 1 as default
    if (this.products) {
      for (var p of this.products) {
        p.quantity=1;
      }
    }
  }

  addToCart(product: ProductResponse)
  {
    if (this.isAddingToCartProcess) {
      // it looks like an adding product process is already running
      // we do not allow concurrent execution
      console.log('Adding a product to cart is already running. Ignore this request because no concurrent execution is allowed.');
      return;
    }

    // get the shopping cart
    var shoppingCart = this.shoppingCart;
    
    if (product && product.quantity > 0) {
        // turn on the flag
        this.isAddingToCartProcess = true;

        // declare the cartItem variable
        let cartItem: ShoppingCartItem | null = null;

        // first check if we have already this product in the cart
        for (let ci of shoppingCart.cartItems) {
          if (ci.productId === product.id) {
            // we found an item in the cart for this product
            // so we use this item object to increase the quantity
            cartItem = ci;
            break;
          }
        }

        if (cartItem) {
          // we have an existing cart item
          // increase the quantity
          cartItem.quantity += product.quantity;
        }
        else {
          // no cart item found, we create a new one with the given product and quantity
          cartItem = {
            productId: product.id,
            productName: product.name,
            skuCode: product.skuCode,
            price: product.price,
            quantity: product.quantity
          };
          // append into the items array
          shoppingCart.cartItems.push(cartItem);
        }
        
        //console.log("Added the product into the shopping cart: " + JSON.stringify(cartItem));
        //console.log("Your current shopping cart: " + JSON.stringify(shoppingCart));

        // set the shopping cart
        this.cartService.setMyCart(shoppingCart).subscribe({
            next: (data) => {
              console.log('Updated the shopping cart.');
              // set the local shopping cart object
              this.shoppingCart = data;
              // clear error message
              this.errorMessage = '';
              // show toaster with message
              this.showToaster('Product added to your shopping cart.');
               // turn off the flag
               this.isAddingToCartProcess = false;
            },
            error: (err) => {
              this.errorMessage = 'Unable to update your shopping cart. ' + err;
              console.log('Unable to update the shopping cart in the backend API server. ' + err);
              // turn off the flag
              this.isAddingToCartProcess = false;
            }
        });
    }
  }

  showToaster(message: string)
  {
		this.toastService.show(message, { classname: 'bg-success text-light', delay: 3000 });
	}

}

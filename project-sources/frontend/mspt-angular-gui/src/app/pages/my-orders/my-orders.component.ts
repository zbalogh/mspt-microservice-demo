import { Component, OnInit } from '@angular/core';
import { getBaseWebURL } from 'src/app/app-constants';
import { OrderResponse } from 'src/app/models/order-response.model';
import { OrderLineItem } from 'src/app/models/orderline-item.model';
import { OrderService } from 'src/app/services/order.service';

@Component({
  selector: 'app-my-orders',
  templateUrl: './my-orders.component.html',
  styleUrls: ['./my-orders.component.css']
})
export class MyOrdersComponent implements OnInit {

  orders: OrderResponse[] = [];

  loaded = false;

  errorMessage = '';

  selectedOrder: OrderResponse | null = null;

  constructor(private readonly orderService: OrderService) { }

  ngOnInit(): void
  {
    this.getMyOrders();
  }

  getMyOrders()
  {
    /*
    this.orderService.getMyOrders().subscribe((data) => {
        console.log(data);
        this.orders = data;
    });
    */
    this.orderService.getMyOrders().subscribe({
      next: (data) => {
        console.log('Loaded orders for the current logged user.');
        this.orders = data;
        this.loaded = true;
      },
      error: (error) => {
        this.errorMessage = 'Unable to get orders. ' + error;
        console.log('Unable to get orders from the backend API server. ' + error);
      }
    });
  }

  totalPrice(order: OrderResponse)
  {
    var total = 0;
    for (var item of order.orderLineItemsDtoList) {
      total+= item.price * item.quantity;
    }
    return total;
  }

  totalQuantity(order: OrderResponse)
  {
    var total = 0;
    for (var item of order.orderLineItemsDtoList) {
      total+=item.quantity;
    }
    return total;
  }

  orderItemTotalPrice(orderItem: OrderLineItem)
  {
    var quantity = orderItem.quantity > 0 ? orderItem.quantity : 1;
    return quantity * orderItem.price; 
  }

  displayShortOrderIdentifier(order: OrderResponse)
  {
    var oid = order.orderNumber;
    if (oid && oid.length > 8) {
      return oid.substring(0, 8);
    }
    else {
      return oid;
    }
  }

  selectedOrderForDetails(order: OrderResponse)
  {
    console.log('selectedOrderForDetails: ' + order.id);
    this.selectedOrder = order;
  }

  unSelectedOrderForDetails()
  {
    this.selectedOrder = null;
  }

}

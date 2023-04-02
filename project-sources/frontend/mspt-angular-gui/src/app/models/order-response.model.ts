import { OrderLineItem } from "./orderline-item.model";

export interface OrderResponse {

    id: number;
	
	orderNumber: string;
	
	orderCreatedAt: Date;

	userid: string;

    orderLineItemsDtoList: OrderLineItem[];

}

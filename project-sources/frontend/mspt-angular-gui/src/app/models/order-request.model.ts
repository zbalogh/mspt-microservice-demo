import { OrderLineItem } from "./orderline-item.model";

export interface OrderRequest {

    userid?: string;

    orderLineItemsDtoList: OrderLineItem[];
    
}

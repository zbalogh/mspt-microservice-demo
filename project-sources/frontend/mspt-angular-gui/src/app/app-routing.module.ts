import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthenticationComponent } from './auth/authentication/authentication.component';
import { MsptFirebaseAuthGuard } from './auth/firebase-auth.guard';
import { HomeComponent } from './pages/home/home.component';
import { InventoryListComponent } from './pages/inventory-list/inventory-list.component';
import { MyCartComponent } from './pages/my-cart/my-cart.component';
import { MyOrdersComponent } from './pages/my-orders/my-orders.component';
import { PageNotFoundComponent } from './pages/page-not-found/page-not-found.component';
import { ProductCatalogListComponent } from './pages/product-catalog-list/product-catalog-list.component';
import { ShoppingComponent } from './pages/shopping/shopping.component';

const routes: Routes = [
    // shopping page
    {
      path: 'shopping',
      component: ShoppingComponent,
      canActivate: [MsptFirebaseAuthGuard]
    },

    // my-cart page
    {
      path: 'my-cart',
      component: MyCartComponent,
      canActivate: [MsptFirebaseAuthGuard]
    },   

    // my-orders page
    {
      path: 'my-orders',
      component: MyOrdersComponent,
      canActivate: [MsptFirebaseAuthGuard]
    },

    // product catalog page
    {
      path: 'product-catalog',
      component: ProductCatalogListComponent,
      canActivate: [MsptFirebaseAuthGuard]
    },

    // inventory page
    {
      path: 'inventory',
      component: InventoryListComponent,
      canActivate: [MsptFirebaseAuthGuard]
    },

    {
      path: 'auth',
      component: AuthenticationComponent
    },

    // home component the default when open browser without path
    {
      path: '',
      redirectTo: 'home', 
      pathMatch: 'full'
    },
    {
      path: 'home',
      component: HomeComponent
    },
  
    // last route entry for case when no matching, then we display 'page not found' component
    {
      path: '**',
      component: PageNotFoundComponent,
      pathMatch: 'full'
    }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: true })],
  exports: [RouterModule]
})
export class AppRoutingModule { }

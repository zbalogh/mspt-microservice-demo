import { Component, OnInit, TemplateRef } from '@angular/core';
import { ProductResponse } from 'src/app/models/product-response.model';
import { ProductService } from 'src/app/services/product.service';
import { ToastService } from 'src/app/services/toast.service';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
import { faPlus, IconDefinition } from '@fortawesome/free-solid-svg-icons';
import { NgForm } from '@angular/forms';
import { ProductRequest } from 'src/app/models/product-request.model';

@Component({
  selector: 'app-product-catalog-list',
  templateUrl: './product-catalog-list.component.html',
  styleUrls: ['./product-catalog-list.component.css']
})
export class ProductCatalogListComponent implements OnInit {

  // store products in this array
  products: ProductResponse[] = [];

  // a flag that indicates that products are loaded from the remote service.
  loaded: boolean = false;

   // font-awesome icons
  faPlusIcon: IconDefinition = faPlus;

  // we use this variable to display error message
  errorMessage: string = '';

  // variable for modal reference, we use it to access the modal
  modalRef?: BsModalRef;

  // variable to store the selected product item.
  //It is used by the delete or edit operation along with the modal
  selectedItem: ProductResponse = {} as ProductResponse;

  constructor(
    private readonly productService: ProductService,
    private readonly toastService: ToastService,
    private readonly modalService: BsModalService,
  ) { }

  ngOnInit(): void
  {
    this.getProducts();
  }

  private getProducts()
  {
      this.productService.getProducts().subscribe({
        next: (data) => {
          console.log('Loaded products.');
          this.products = data;
          this.loaded = true;
          this.errorMessage = '';
          //this.initProductQuantity();
        },
        error: (error) => {
          this.errorMessage = 'Unable to get products. ' + error;
          console.log('Unable to get products from the backend API server. ' + error);
        }
      });
  }


  showToaster(message: string)
  {
		this.toastService.show(message, { classname: 'bg-success text-light', delay: 3000 });
	}


  onClickDeleteButton(item: ProductResponse, template: TemplateRef<any>)
  {
  // set the selectedItem
  this.selectedItem = { ...item };

  // open the modal for the further action
  this.modalRef = this.modalService.show(template, {class: 'modal-sm'});
  }

  onClickEditButton(item: ProductResponse, template: TemplateRef<any>)
  {
  // set the selectedItem to the given object for modification
  this.selectedItem = { ...item };

  // open the modal for the further action
  this.modalRef = this.modalService.show(template, {class: 'modal-md'});
  }

  onClickCreateButton(template: TemplateRef<any>)
  {
  // set the selectedItem to a new empty object for creation
  this.selectedItem = {} as ProductResponse;

  // open the modal for the further action
  this.modalRef = this.modalService.show(template, {class: 'modal-md'});
  }


  confirmDelete()
  {
    // hide the modal
    this.modalRef?.hide();
  
    // get the itemId
    const itemId = this.selectedItem?.id;
    console.log('Delete product by ID: ' + itemId);

    // TODO: delete the selected product by the given ID
    this.productService.deleteProduct(itemId).subscribe(
    {
      next: () => {
        console.log('Deleted products.');
         // clear error message
        this.errorMessage = '';
        // show toaster with message
        this.showToaster("Product '" + this.selectedItem.name + "' deleted.");
        // reset the selectedItem
        this.selectedItem = {} as ProductResponse;
        // get products to refresh the data
        this.getProducts();
      },
      error: (error) => {
        this.errorMessage = 'Unable to delete the product. ' + error;
        console.log('Unable to delete the product on the backend API server. ' + error);
      }
    });
  }

  closeModal()
  {
    // hide the modal
    this.modalRef?.hide();
    // reset the selectedItem
    this.selectedItem = {} as ProductResponse;
  }


  onFormSubmit(itemEditorForm: NgForm)
  {
    // if the form is invalid then return
    if (!itemEditorForm.form.valid) {
      return false;
    }

    // hide the modal
    this.modalRef?.hide();

    if (this.selectedItem.id) {
      // edit
      let productRequest: ProductRequest = {
        id: this.selectedItem.id,
        name: this.selectedItem.name,
        description: this.selectedItem.description,
        price: this.selectedItem.price,
        skuCode: this.selectedItem.skuCode
      } as ProductRequest;

      // updating the product
      this.updateProduct(productRequest);
    }
    else {
      // create
      let productRequest: ProductRequest = {
        name: this.selectedItem.name,
        description: this.selectedItem.description,
        price: this.selectedItem.price,
        skuCode: this.selectedItem.skuCode
      } as ProductRequest;

      // creating the product
      this.createProduct(productRequest);
    }

    return true;
  }

  createProduct(productRequest: ProductRequest)
  {
    this.productService.createProduct(productRequest).subscribe(
      {
        next: (data) => {
          console.log('Created product.');
           // clear error message
          this.errorMessage = '';
          // show toaster with message
          this.showToaster("Product '" + data.name + "' created.");
          // reset the selectedItem
          this.selectedItem = {} as ProductResponse;
          // get products to refresh the data
          this.getProducts();
        },
        error: (error) => {
          this.errorMessage = 'Unable to create the product. ' + error;
          console.log('Unable to create the product on the backend API server. ' + error);
        }
      }
    );
  }

  updateProduct(productRequest: ProductRequest)
  {
    this.productService.updateProduct(productRequest).subscribe(
      {
        next: (data) => {
          console.log('Updated product.');
           // clear error message
          this.errorMessage = '';
          // show toaster with message
          this.showToaster("Product '" + data.name + "' updated.");
          // reset the selectedItem
          this.selectedItem = {} as ProductResponse;
          // get products to refresh the data
          this.getProducts();
        },
        error: (error) => {
          this.errorMessage = 'Unable to update the product. ' + error;
          console.log('Unable to update the product on the backend API server. ' + error);
        }
      }
    );
  }

}

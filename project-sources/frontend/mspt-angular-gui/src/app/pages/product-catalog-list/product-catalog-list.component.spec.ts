import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProductCatalogListComponent } from './product-catalog-list.component';

describe('ProductCatalogListComponent', () => {
  let component: ProductCatalogListComponent;
  let fixture: ComponentFixture<ProductCatalogListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ProductCatalogListComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProductCatalogListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

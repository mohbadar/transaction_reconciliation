import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ListTransactionResultComponent } from './list-transaction-result.component';

describe('ListTransactionResultComponent', () => {
  let component: ListTransactionResultComponent;
  let fixture: ComponentFixture<ListTransactionResultComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ListTransactionResultComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ListTransactionResultComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

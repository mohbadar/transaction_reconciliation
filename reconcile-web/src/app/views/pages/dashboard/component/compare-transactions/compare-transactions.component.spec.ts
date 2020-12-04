import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CompareTransactionsComponent } from './compare-transactions.component';

describe('CompareTransactionsComponent', () => {
  let component: CompareTransactionsComponent;
  let fixture: ComponentFixture<CompareTransactionsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CompareTransactionsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CompareTransactionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

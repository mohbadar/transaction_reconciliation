import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SimilarTransactionsComponent } from './similar-transactions.component';

describe('SimilarTransactionsComponent', () => {
  let component: SimilarTransactionsComponent;
  let fixture: ComponentFixture<SimilarTransactionsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SimilarTransactionsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SimilarTransactionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

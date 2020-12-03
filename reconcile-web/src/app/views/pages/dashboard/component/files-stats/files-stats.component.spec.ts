import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FilesStatsComponent } from './files-stats.component';

describe('FilesStatsComponent', () => {
  let component: FilesStatsComponent;
  let fixture: ComponentFixture<FilesStatsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FilesStatsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FilesStatsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';

@Component({
  selector: 'kt-view-transaction',
  templateUrl: './view-transaction.component.html',
  styleUrls: ['./view-transaction.component.scss']
})
export class ViewTransactionComponent implements OnInit {

  hasFormErrors: boolean = false;
  loadingAfterSubmit: boolean = false;
  viewLoading: boolean = false;
  transaction: any;

  constructor(private dialogRef: MatDialogRef<ViewTransactionComponent>, @Inject(MAT_DIALOG_DATA) private data: any) {

      this.transaction = data.item;
  }

  ngOnInit() {
  }

}

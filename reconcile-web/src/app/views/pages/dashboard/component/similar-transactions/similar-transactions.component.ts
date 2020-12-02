import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import { MatDialogRef, MatPaginator, MatSort, MatTableDataSource, MAT_DIALOG_DATA } from '@angular/material';

@Component({
  selector: 'kt-similar-transactions',
  templateUrl: './similar-transactions.component.html',
  styleUrls: ['./similar-transactions.component.scss']
})
export class SimilarTransactionsComponent implements OnInit {


  transactions: any;

      // List of Columns which are to be displayed
      displayedColumns: string[] = ['transactionID', 'transactionAmount', 'transactionDate', 'similarity'];
      // The data variable will contain the fetched data
      dataSource: MatTableDataSource<any>;
      @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;
      @ViewChild(MatSort, { static: true }) sort: MatSort;
  

  constructor(private dialogRef: MatDialogRef<SimilarTransactionsComponent>, @Inject(MAT_DIALOG_DATA) private data: any) {

    this.transactions = data.item;
    console.log("SimilarComponent", this.transactions);
    
}



applyFilter(filterValue: string) {
      this.dataSource.filter = filterValue.trim().toLowerCase();

      if (this.dataSource.paginator) {
          this.dataSource.paginator.firstPage();
      }
  }

  setDataSourceData(data) {
    this.dataSource = new MatTableDataSource(data);
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  ngOnInit(): void {

    this.setDataSourceData(this.transactions);
  }

}

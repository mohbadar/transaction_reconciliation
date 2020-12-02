import { ChangeDetectorRef, Component, Input, OnInit, ViewChild } from '@angular/core';
import { MatDialog, MatPaginator, MatSort, MatTableDataSource } from '@angular/material';
import { TransactionDTO } from '../../model/transaction.dto';
import { ReconciliationService } from '../../service/reconciliation.service';
import { SimilarTransactionsComponent } from '../similar-transactions/similar-transactions.component';
import { ViewTransactionComponent } from '../view-transaction/view-transaction.component';

@Component({
  selector: 'kt-list-transaction-result',
  templateUrl: './list-transaction-result.component.html',
  styleUrls: ['./list-transaction-result.component.scss']
})
export class ListTransactionResultComponent implements OnInit {

   @Input() data;

    // List of Columns which are to be displayed
    displayedColumns: string[] = ['file','transactionID', 'transactionAmount', 'transactionDate', 'status', 'actions'];
    // The data variable will contain the fetched data
    unMatchedDataSource: MatTableDataSource<any>;
    @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;
    @ViewChild(MatSort, { static: true }) sort: MatSort;

  constructor(
     private cdref: ChangeDetectorRef,
     public dialog: MatDialog,
     private reconciliationService: ReconciliationService

  ) { 
    
  }

   /**
     * Apply user filter to configuration data
     *
     */
  applyFilter(filterValue: string) {
      this.unMatchedDataSource.filter = filterValue.trim().toLowerCase();

      if (this.unMatchedDataSource.paginator) {
          this.unMatchedDataSource.paginator.firstPage();
      }
  }

  setUnMatchDataSourceData(data) {
    this.unMatchedDataSource = new MatTableDataSource(data);
    this.unMatchedDataSource.paginator = this.paginator;
    this.unMatchedDataSource.sort = this.sort;
  }


  ngOnInit(): void {
    console.log("Data", this.data);
    this.setUnMatchDataSourceData(this.data.unmatched)
  }


  
  view(item) {
    const dialogRef = this.dialog.open(ViewTransactionComponent, {
        data: { item }
    });
    dialogRef.afterClosed().subscribe(res => {
        if (!res) {
            return;
        }
    });

}


presentSimilarTransactions(item) {
  const dialogRef = this.dialog.open(SimilarTransactionsComponent, {
      data: { item }
  });
  dialogRef.afterClosed().subscribe(res => {
      if (!res) {
          return;
      }
  });

}

getSimilarTransactions(item)
{
  this.reconciliationService.getSimilarTransactions(JSON.stringify(item)).subscribe(response => {
      console.log("Similar Transactions", response.similarTransactions);  
      this.presentSimilarTransactions(response.similarTransactions);
  }, 
  (err => {
    console.log("Error Whille Get Similar Transactions");
  }));
}


}

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

    badTransactionDataSource: MatTableDataSource<any>;
    probableMismatchDataSource: MatTableDataSource<any>;
    probableMatchDataSource: MatTableDataSource<any>;
    permissibleMatchDataSource: MatTableDataSource<any>;
    perfectMatchDataSource: MatTableDataSource<any>;
    duplicateDataSource: MatTableDataSource<any>;
    profectMismatchDataSource: MatTableDataSource<any>;


    

  constructor(
     private cdref: ChangeDetectorRef,
     public dialog: MatDialog,
     private reconciliationService: ReconciliationService

  ) { 
    
  }

  ngOnInit(): void {
    console.log("Data", this.data);
    this.setUnMatchDataSourceData(this.data.unmatched);
    this.setDuplicateDataSourceData(this.data.duplicate);
    this.setBadTransactionDataSourceData(this.data.bad);
    this.setProbableMatchDataSourceData(this.data.probableMatch);
    this.setProbableMismatchDataSourceData(this.data.probableMismatch);
    this.setPerfectMatchDataSourceData(this.data.perfectMatch);
    this.setPerfectMismatchDataSourceData(this.data.perfectMismatch);
    this.setPermissibleMatchDataSourceData(this.data.permissibleMatch)
  }

  setUnMatchDataSourceData(data) {
    this.unMatchedDataSource = new MatTableDataSource(data);
    this.unMatchedDataSource.paginator = this.paginator;
    this.unMatchedDataSource.sort = this.sort;
  }

  setDuplicateDataSourceData(data) {
    this.duplicateDataSource = new MatTableDataSource(data);
  }

  setBadTransactionDataSourceData(data) {
    this.badTransactionDataSource = new MatTableDataSource(data);
    // this.badTransactionDataSource.paginator = this.paginator;
    // this.badTransactionDataSource.sort = this.sort;
  }

  setProbableMismatchDataSourceData(data) {
    this.probableMismatchDataSource = new MatTableDataSource(data);
    // this.probableMismatchDataSource.paginator = this.paginator;
    // this.probableMismatchDataSource.sort = this.sort;
  }

  setProbableMatchDataSourceData(data) {
    this.probableMatchDataSource = new MatTableDataSource(data);
    // this.probableMatchDataSource.paginator = this.paginator;
    // this.probableMatchDataSource.sort = this.sort;
  }
  setPermissibleMatchDataSourceData(data) {
    this.permissibleMatchDataSource = new MatTableDataSource(data);
    // this.permissibleMatchDataSource.paginator = this.paginator;
    // this.permissibleMatchDataSource.sort = this.sort;
  }
  setPerfectMatchDataSourceData(data) {
    this.perfectMatchDataSource = new MatTableDataSource(data);
    // this.prefectMatchDataSource.paginator = this.paginator;
    // this.prefectMatchDataSource.sort = this.sort;
  }

  setPerfectMismatchDataSourceData(data) {
    console.log("PerfectMismatch", data);
    
    this.profectMismatchDataSource = new MatTableDataSource(data);
    // this.prefectMatchDataSource.paginator = this.paginator;
    // this.prefectMatchDataSource.sort = this.sort;
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

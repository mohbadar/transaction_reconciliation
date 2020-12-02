// Angular
import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
// Core Module
import { CoreModule } from '../../../core/core.module';
import { PartialsModule } from '../../partials/partials.module';
import { DashboardComponent } from './dashboard.component';
import { NgbDropdownModule, NgbTabsetModule, NgbTooltipModule } from '@ng-bootstrap/ng-bootstrap';
// Translate
import { TranslateModule } from '@ngx-translate/core';
import { NgxSpinnerModule } from 'ngx-spinner';
import { NgSelectModule } from '@ng-select/ng-select';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatAutocompleteModule, MatButtonModule, MatCardModule, MatCheckboxModule, MatDatepickerModule, MatDialogModule, MatExpansionModule, MatFormFieldModule, MatIconModule, MatInputModule, MatMenuModule, MatNativeDateModule, MatPaginatorModule, MatProgressBarModule, MatProgressSpinnerModule, MatRadioModule, MatSelectModule, MatSnackBarModule, MatSortModule, MatTabGroup, MatTableModule, MatTabsModule, MatTooltipModule } from '@angular/material';
import { ListTransactionResultComponent } from './component/list-transaction-result/list-transaction-result.component';
import { ViewTransactionComponent } from './component/view-transaction/view-transaction.component';
import { HttpClientModule } from '@angular/common/http';
import { SimilarTransactionsComponent } from './component/similar-transactions/similar-transactions.component';

@NgModule({
    imports: [
        CommonModule,
        PartialsModule,
        CoreModule,
        TranslateModule.forChild(),
        FormsModule,
        ReactiveFormsModule,
        RouterModule.forChild([
            {
                path: '',
                component: DashboardComponent
            },
        ]),

        NgbDropdownModule,
        NgbTabsetModule,
        NgbTooltipModule,
        NgxSpinnerModule,
        NgSelectModule,
        MatTabsModule,
        MatPaginatorModule,
        MatTableModule,
        MatIconModule,
        MatIconModule,
        MatNativeDateModule,
        MatProgressBarModule,
        MatDatepickerModule,
        MatCardModule,
        MatSortModule,
        MatCheckboxModule,
        MatProgressSpinnerModule,
        MatSnackBarModule,
        MatExpansionModule,
        MatTooltipModule,
        MatDialogModule,
        MatFormFieldModule,
        HttpClientModule,
        MatButtonModule,
        MatMenuModule,
        MatInputModule,
        MatAutocompleteModule,
        MatRadioModule,
        MatSelectModule,
        MatButtonModule
    
    ],
    providers: [],
    declarations: [
        DashboardComponent,
        ListTransactionResultComponent,
        ViewTransactionComponent,
        SimilarTransactionsComponent,
    ]
})
export class DashboardModule {
}

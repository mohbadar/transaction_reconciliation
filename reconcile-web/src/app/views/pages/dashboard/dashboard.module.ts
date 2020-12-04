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
import { FilesStatsComponent } from './component/files-stats/files-stats.component';
import { ModuleInfoComponent } from './component/module-info/module-info.component';
import { PagesModule } from '../pages.module';
import { CompareTransactionsComponent } from './component/compare-transactions/compare-transactions.component';

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
                component: DashboardComponent,
                children: [
                    {
                        path: '',
                        component: ModuleInfoComponent
                    },
                    {
                        path: 'compare-transaction',
                        component: CompareTransactionsComponent
                    }
                ]
            }                   
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
        MatButtonModule,
        PagesModule
    
    ],
    providers: [],
    declarations: [
        DashboardComponent,
        ListTransactionResultComponent,
        ViewTransactionComponent,
        SimilarTransactionsComponent,
        FilesStatsComponent,
        ModuleInfoComponent,
        CompareTransactionsComponent,
    ]
})
export class DashboardModule {
}

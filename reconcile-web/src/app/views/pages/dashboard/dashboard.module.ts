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
    ],
    providers: [],
    declarations: [
        DashboardComponent,
    ]
})
export class DashboardModule {
}

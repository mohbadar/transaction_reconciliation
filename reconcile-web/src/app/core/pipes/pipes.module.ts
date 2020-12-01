import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { JalaliPipe } from './jalali.pipe';



@NgModule({
    declarations: [JalaliPipe],
    imports: [
        CommonModule
    ],
    exports: [
        JalaliPipe
    ]
})
export class PipesModule { }

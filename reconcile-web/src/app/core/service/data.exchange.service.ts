import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class DataExchangeService {
    private dataSource = new BehaviorSubject(null);

    currentData = this.dataSource.asObservable();

    changeData(data: any) {
        this.dataSource.next(data);
    }
}

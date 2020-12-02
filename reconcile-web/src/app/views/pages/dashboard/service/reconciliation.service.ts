import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

const _BASE_URL = "/api";

@Injectable({
    providedIn: 'root'
})
export class ReconciliationService {

    constructor(
        private httpClient: HttpClient,
    ) { }

    public processFiles(data): Observable<any> {
        return this.httpClient.post(`${_BASE_URL}/compare-files`, data);
    }


}

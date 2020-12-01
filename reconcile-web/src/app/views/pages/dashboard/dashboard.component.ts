import { ChangeDetectorRef, Component, OnInit, ViewChild } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { MatDialog, MatExpansionPanel } from "@angular/material";
import { FileDownloadService } from 'app/core/service/file-download.service';
import { LayoutUtilsService, MessageType } from 'app/core/_base/crud';
import { NgxSpinnerService } from 'ngx-spinner';
import { Observable } from "rxjs";


@Component({
    selector: 'kt-dashboard',
    templateUrl: './dashboard.component.html',
    styleUrls: ['dashboard.component.scss'],
})
export class DashboardComponent implements OnInit {

    @ViewChild("searchPanel") searchPanel: MatExpansionPanel;
    @ViewChild("child")
  
    formGroup: FormGroup;

    clientFileAttachment: File;
    clientFileName = 'Client File Attachment';
    clientAttachmentAdded: boolean = false;

    tutukaFileAttachment: File;
    tutukaFileName = 'Tutuka File Attachment';
    tutukaAttachmentAdded: boolean = false;

    record: any;
    searchResult: any;
  
    constructor(
      private formBuilder: FormBuilder,
      public dialog: MatDialog,
      private cdr: ChangeDetectorRef,
      private fileDownloadService: FileDownloadService,
      private layoutUtilsService: LayoutUtilsService,
      private spinner: NgxSpinnerService
    ) { }
  
    ngOnInit(): void {

      this.formGroup = this.formBuilder.group({
        clientfile: [ [Validators.required]],
        tutukafile: [ [Validators.required]],
      });
    }


    resetForm() {
      this.formGroup.reset();
      this.searchPanel.toggle();
    }

    onClientFileSelected(event) {
        if (event.target.files && event.target.files[0]) {
            const reader = new FileReader();
            this.clientFileAttachment = event.target.files[0];
            console.log('FileName: ', event.target.files[0]);
            this.clientFileName = event.target.files[0].name;
            reader.readAsDataURL(event.target.files[0]); // read file as data url
            reader.onload = (e) => { // called once readAsDataURL is completed
                this.clientAttachmentAdded = true;
            }
        }
    }
  
    onTutukaFileSelected(event) {
        if (event.target.files && event.target.files[0]) {
            const reader = new FileReader();
            this.tutukaFileAttachment = event.target.files[0];
            console.log('FileName: ', event.target.files[0]);
            this.tutukaFileName = event.target.files[0].name;
            reader.readAsDataURL(event.target.files[0]); // read file as data url
            reader.onload = (e) => { // called once readAsDataURL is completed
                this.tutukaAttachmentAdded = true;
            }
        }
    }
  
    formSubmit() {
        const formData = new FormData();
        this.record = this.formGroup.value;
        formData.append('meterStockDTO', JSON.stringify(this.record));
        formData.append('clientFile', this.clientFileAttachment);
        formData.append('tutukaFile', this.tutukaFileAttachment);
        console.log(formData);
        
        // this.spinner.show();
        // this.service.uploadMetersFromXLS(formData).subscribe(
        //     response => {
        //         this.spinner.hide();
        //         this.reconcileForm.reset();
        //         const _createMessage = `File has been uploaded!`;
        //         this.layoutUtilsService.showActionNotification(
        //             _createMessage,
        //             MessageType.Create
        //         );
        //     },
        //     err => {
        //         const msg = 'There was an error on uploading';
        //         this.spinner.hide();
        //         this.layoutUtilsService.showActionNotification(msg);
        //     });
    }
    
  
    checkForm() {
      return this.searchResult == null;
    }

  }
  
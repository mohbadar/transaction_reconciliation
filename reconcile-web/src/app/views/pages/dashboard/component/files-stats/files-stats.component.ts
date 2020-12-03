import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'kt-files-stats',
  templateUrl: './files-stats.component.html',
  styleUrls: ['./files-stats.component.scss']
})
export class FilesStatsComponent implements OnInit {

  @Input() data;

  constructor() { }

  ngOnInit(): void {
    console.log("STATS", this.data);
    
  }

}

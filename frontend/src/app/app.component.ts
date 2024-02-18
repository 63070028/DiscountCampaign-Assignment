import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {ItemPageComponent} from "./item-page/item-page.component";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, ItemPageComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'frontend';
}

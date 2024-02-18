import { Component } from '@angular/core';
import {NgIf, NgOptimizedImage} from "@angular/common";
import {ItemComponent} from "./item/item.component";
import {ItemCartComponent} from "./item-cart/item-cart.component";

@Component({
  selector: 'app-item-page',
  standalone: true,
  imports: [
    NgOptimizedImage,
    ItemComponent,
    NgIf,
    ItemCartComponent
  ],
  templateUrl: './item-page.component.html',
  styleUrl: './item-page.component.css'
})
export class ItemPageComponent {

  openCart: boolean = true;

}

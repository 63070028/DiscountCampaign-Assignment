import {Component, inject} from '@angular/core';
import {NgIf, NgOptimizedImage} from "@angular/common";
import {ItemComponent} from "./item/item.component";
import {ItemCartComponent} from "./item-cart/item-cart.component";
import {DiscountService} from "./service/discount.service";

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
  discountService = inject(DiscountService);

  test(){
    this.discountService.discountCampaigns();
  }

}

import {Component, inject} from '@angular/core';
import {CommonModule, NgIf, NgOptimizedImage} from "@angular/common";
import {ItemComponent} from "./item/item.component";
import {ItemCartComponent} from "./item-cart/item-cart.component";
import {DiscountService} from "./service/discount.service";
import {CategoryCampaigns, DiscountDetail, Item, Product} from "./model/model";
import {FormsModule} from "@angular/forms";

@Component({
  selector: 'app-item-page',
  standalone: true,
  imports: [
    NgOptimizedImage,
    ItemComponent,
    NgIf,
    ItemCartComponent,
    CommonModule,
    FormsModule
  ],
  templateUrl: './item-page.component.html',
  styleUrl: './item-page.component.css'
})
export class ItemPageComponent {

  openCart: boolean = true;
  discountService = inject(DiscountService);

  category:String = ""

  cart:Item[] = [];

  products:Product[] = [
    {name:"Item1", category:"A", price:350},
    {name:"Item2", category:"A", price:250},
    {name:"Item3", category:"A", price:100},
    {name:"Item4", category:"A", price:200},
    {name:"Item5", category:"B", price:400},
    {name:"Item6", category:"B", price:120},
    {name:"Item7", category:"B", price:190},
    {name:"Item8", category:"C", price:225.25},
    {name:"Item9", category:"C", price:335.50},
    {name:"Item10", category:"C", price:225},
    {name:"Item11", category:"D", price:600},
    {name:"Item12", category:"D", price:1000},
  ];

  onTopPercentageCategory:String[] = [];

  discountDetail:DiscountDetail = {
      amount: "1000.00",
      useDiscountCampaign: {
        coupon: {
          fixedAmount: {
            amount: 50,
            campaign: "Fixed amount",
            type: CategoryCampaigns.COUPON,
            discount: 50.0
          },
          percentageDiscount: null
        },
        onTop: {
          percentageCategory: {
            campaign: "Percentage discount by category",
            discount: 15.0,
            category: [
              "C"
            ],
            type: CategoryCampaigns.ONTOP,
            percentage: 10
          },
          pointDiscount: null
        },
        seasonal: {
          everyAmount: {
            campaign: "Special campaigns",
            discount: 120.0,
            type: CategoryCampaigns.SEASONAL,
            every: 300,
            amount: 40
          }
        }
      },
      totalPrice: "815.00"
  };

  addOnTopCategory(){
    const index = this.onTopPercentageCategory.indexOf(this.category);
    if(index == -1){
      this.onTopPercentageCategory.push(this.category);
    }
    this.category = '';
  }

  removeOnTopCategory(index:number){
    this.onTopPercentageCategory.splice(index, 1)
  }

  addCart(product:Product){
      const target = this.cart.find(item => item.name == product.name);
      if(target){
        target.quantity++;
      }else{
        const item :Item = {name:product.name, category:product.category, price:product.price, quantity:1};
        this.cart.push(item)
      }
  }

   productQuantityInCart(cart:Item[]):number{
    let result = 0
     cart.forEach((item)=>{
        result += item.quantity
     });
    return result;
  }

  addItemQuantity(item:Item){
    item.quantity++;
  }

  minusItemQuantity(item:Item){
    if(item.quantity - 1 != 0){
      item.quantity--;
    }else{
      const index = this.cart.indexOf(item);
      if(index > -1){
        this.cart.splice(index, 1);
      }
    }
  }

  amountCart(cart:Item[]){
    let result = 0;
    cart.forEach(item=>{
      result += item.price * item.quantity;
    })

    return result;
  }



  test(){
    this.discountService.discountCampaigns();
  }

}

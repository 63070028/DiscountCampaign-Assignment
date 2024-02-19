import {Component, inject} from '@angular/core';
import {CommonModule, NgIf, NgOptimizedImage} from "@angular/common";
import {DiscountService} from "./service/discount.service";
import {
  CategoryCampaigns,
  DiscountDetail,
  DiscountRequest, errorResponse, EveryAmount,
  FixedAmount,
  Item, PercentageCategory,
  PercentageDiscount, PointDiscount,
  Product
} from "./model/model";
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule} from "@angular/forms";


@Component({
  selector: 'app-item-page',
  standalone: true,
  imports: [
    NgOptimizedImage,
    NgIf,
    CommonModule,
    FormsModule,
    ReactiveFormsModule
  ],
  templateUrl: './item-page.component.html',
  styleUrl: './item-page.component.css'
})
export class ItemPageComponent {
  discountService = inject(DiscountService);

  openCart: boolean = false;
  showFormJson: boolean = false;
  cart:Item[] = [];
  error?: errorResponse | null;

  useCouponFixedAmount = false;
  useCouponPercentageDiscount = false;
  useOnTopPercentageCategory = false;
  useOnTopPointDiscount = false;
  useSeasonalEveryAmount = false;



  discountCampaignsForm = new FormGroup({
    coupon: new FormGroup({
      fixedAmount: new FormGroup({
        campaign: new FormControl<string>("Fixed amount"),
        type: new FormControl<string>(CategoryCampaigns.COUPON),
        amount: new FormControl<number>(0),
        discount: new FormControl<number>(0)
      }),
      percentageDiscount: new FormGroup({
        campaign: new FormControl<string>("Percentage discount"),
        type: new FormControl<string>(CategoryCampaigns.COUPON),
        percentage: new FormControl<number>(0),
        discount: new FormControl<number>(0)
      }),
    }),
    onTop: new FormGroup({
      percentageCategory: new FormGroup({
        campaign: new FormControl<string>("Percentage discount by category"),
        type: new FormControl<string>(CategoryCampaigns.ONTOP),
        categoryInput: new FormControl<string>(""),
        category: new FormControl<string[]>([]),
        percentage: new FormControl<number>(0),
        discount: new FormControl<number>(0)
      }),
      pointDiscount:  new FormGroup({
        campaign: new FormControl<string>("Discount by points"),
        type: new FormControl<string>(CategoryCampaigns.ONTOP),
        point: new FormControl<number>(0),
        discount: new FormControl<number>(0)
      })
    }),
    seasonal: new FormGroup({
      everyAmount: new FormGroup({
        campaign: new FormControl<string>("Special campaigns"),
        type: new FormControl<string>(CategoryCampaigns.SEASONAL),
        every: new FormControl<number>(0),
        amount: new FormControl<number>(0),
        discount: new FormControl<number>(0)
      })
    })
  })




  products:Product[] = [
    {name:"Item1", category:"A", price:350},
    {name:"Item2", category:"A", price:250},
    {name:"Item3", category:"A", price:700},
    {name:"Item4", category:"A", price:230},
    {name:"Item5", category:"B", price:850},
    {name:"Item6", category:"B", price:640},
    {name:"Item7", category:"B", price:250},
    {name:"Item8", category:"B", price:550.5},
    {name:"Item9", category:"C", price:225.25},
    {name:"Item10", category:"C", price:335.50},
    {name:"Item11", category:"C", price:400.25},
    {name:"Item12", category:"D", price:600},

  ];

  discountDetail:DiscountDetail = {
      amount: "",
      useDiscountCampaign: {
        coupon: {
          fixedAmount: null,
          percentageDiscount: null
        },
        onTop: {
          percentageCategory: null,
          pointDiscount: null
        },
        seasonal: {
          everyAmount: null
        }
      },
      totalPrice: ""
  };

  showCart(){
    this.openCart = true;
  }

  closeCart(){
    this.resetAllDiscountCampaigns();
    this.openCart = false;
    this.error = null;
  }

  addOnTopCategory(){
    let percentageCategory = this.discountCampaignsForm.controls.onTop.controls.percentageCategory.value;

    if(percentageCategory){
      const index = percentageCategory.category?.indexOf(<string>percentageCategory.categoryInput);
      if(index == -1){
        percentageCategory.category?.push(<string>percentageCategory.categoryInput);
      }
      this.discountCampaignsForm.controls.onTop.controls.percentageCategory.controls.categoryInput.reset();
    }
  }

  removeOnTopCategory(index:number){
    let percentageCategory = this.discountCampaignsForm.controls.onTop.controls.percentageCategory.value;
    if(percentageCategory){
        percentageCategory.category?.splice(index, 1);
    }

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

  openCouponFixedAmount(){
    this.useCouponFixedAmount = true;
    this.useCouponPercentageDiscount = false;
    this.discountCampaignsForm.controls.coupon.controls.percentageDiscount.reset();
  }

  openCouponPercentageDiscount(){
    this.useCouponPercentageDiscount = true;
    this.useCouponFixedAmount = false;
    this.discountCampaignsForm.controls.coupon.controls.fixedAmount.reset();
  }

  openOnTopPercentageCategory(){
    this.useOnTopPercentageCategory = true;
    this.useOnTopPointDiscount = false;
    this.discountCampaignsForm.controls.onTop.controls.pointDiscount.reset();
  }

  openOnTopPointDiscount(){
    this.useOnTopPointDiscount = true;
    this.useOnTopPercentageCategory = false;
    this.discountCampaignsForm.controls.onTop.controls.percentageCategory.reset();
    this.discountCampaignsForm.controls.onTop.controls.percentageCategory.controls.category.setValue([]);
  }

  openSeasonalEveryAmount(){
    this.useSeasonalEveryAmount = true;
  }

  resetAllDiscountCampaigns(){
    this.useCouponFixedAmount = false;
    this.useCouponPercentageDiscount = false;
    this.useOnTopPercentageCategory = false;
    this.useOnTopPointDiscount = false;
    this.useSeasonalEveryAmount = false;

    this.discountDetail = {
      amount: "",
      useDiscountCampaign: {
        coupon: {
          fixedAmount: null,
          percentageDiscount: null
        },
        onTop: {
          percentageCategory: null,
          pointDiscount: null
        },
        seasonal: {
          everyAmount: null
        }
      },
      totalPrice: ""
    };

   this.discountCampaignsForm.reset();
   this.discountCampaignsForm.controls.onTop.controls.percentageCategory.controls.category.setValue([]);
   this.error = null;
  }

  prepareDiscountRequest(){
    let request: DiscountRequest = {
      cart:[],
      discountCampaigns:{
        coupon: {
          fixedAmount:null,
          percentageDiscount: null
        },
        onTop: {
          percentageCategory: null,
          pointDiscount: null
        },
        seasonal: {
          everyAmount: null
        }
      }
    }

    if(this.useCouponFixedAmount){
      request.discountCampaigns.coupon.fixedAmount = <FixedAmount>this.discountCampaignsForm.controls.coupon.controls.fixedAmount.getRawValue();
    }
    else if(this.useCouponPercentageDiscount){
      request.discountCampaigns.coupon.percentageDiscount = <PercentageDiscount>this.discountCampaignsForm.controls.coupon.controls.percentageDiscount.getRawValue();
    }

    if(this.useOnTopPercentageCategory){
      request.discountCampaigns.onTop.percentageCategory = <PercentageCategory>this.discountCampaignsForm.controls.onTop.controls.percentageCategory.getRawValue();
    }
    else if(this.useOnTopPointDiscount){
      request.discountCampaigns.onTop.pointDiscount = <PointDiscount>this.discountCampaignsForm.controls.onTop.controls.pointDiscount.getRawValue();
    }

    if(this.useSeasonalEveryAmount){
      request.discountCampaigns.seasonal.everyAmount = <EveryAmount>this.discountCampaignsForm.controls.seasonal.controls.everyAmount.getRawValue();
    }

   if(this.cart.length != 0){
     request.cart = this.cart;
   }
   return request;
  }


  calculateTotalPrice(){
    this.error = null;
    let request = this.prepareDiscountRequest();
    this.discountService.discountCampaigns(request).subscribe(response=>{
        this.discountDetail.useDiscountCampaign = response.payload.useDiscountCampaign;
        this.discountDetail.amount = response.payload.amount;
        this.discountDetail.totalPrice =response.payload.totalPrice;
    }, errResponse=> {
      if(errResponse.status == 417){
        this.error = errResponse.error;
      }else{
        this.error = {
          timestamp: "",
          status: "",
          error: "Failed server"
        }
      }

    })


  }

  useColor= 'bg-yellow-500 hover:bg-yellow-700 text-white font-bold py-2 px-4 border border-yellow-700 rounded'


  noUseColor= 'bg-gray-500 hover:bg-gray-700 text-white font-bold py-2 px-4 border border-gray-700 rounded'


}

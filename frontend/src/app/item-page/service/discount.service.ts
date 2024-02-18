import {Injectable} from '@angular/core';
import {CategoryCampaigns, DiscountRequest, DiscountResponse} from "../model/model";
import {HttpClient} from "@angular/common/http";


@Injectable({
  providedIn: 'root'
})
export class DiscountService {

  constructor(private http: HttpClient) { }

  mockRequest: DiscountRequest = {
    cart:[
      {name:"Item1", category:"A", price:350, quantity: 1},
      {name:"Item2", category:"B", price:250, quantity: 2},
      {name:"Item2", category:"C", price:150, quantity: 1}
    ],
    discountCampaigns:{
      coupon: {
        fixedAmount:{
          amount: 50,
          campaign: "Fixed amount",
          type: CategoryCampaigns.COUPON,
          discount: 0.0
        },
        percentageDiscount: null
      },
      onTop: {
        percentageCategory: {
          "percentage": 10,
          campaign: "Percentage discount by category",
          type: CategoryCampaigns.ONTOP,
          category:["C"],
          discount: 0.0
        },
        pointDiscount: null
      },
      seasonal: {
        everyAmount: {
          campaign: "Special campaigns",
          type: CategoryCampaigns.SEASONAL,
          every: 300,
          amount: 40,
          discount: 0.0
        }
      }
    }
  }
  public discountCampaigns(){
    this.http.post<DiscountResponse>("http://localhost:8001/discount/campaigns", this.mockRequest).subscribe(res => console.log(res));
  }
}

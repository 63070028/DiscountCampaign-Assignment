import {Injectable} from '@angular/core';
import {CategoryCampaigns, DiscountRequest, DiscountResponse} from "../model/model";
import {HttpClient} from "@angular/common/http";


@Injectable({
  providedIn: 'root'
})
export class DiscountService {

  constructor(private http: HttpClient) { }

  public discountCampaigns(request: DiscountRequest){
    return  this.http.post<DiscountResponse>("http://localhost:8001/discount/campaigns", request);
  }
}

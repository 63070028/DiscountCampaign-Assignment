import {stat} from "fs";

export interface Product{
  name:string;
  category:string;
  price:number;
}

export interface Item{
  name:string;
  category:string;
  price:number;
  quantity:number;
}

export interface DiscountCampaigns{
  coupon:CouponCampaigns;
  onTop:OnTopCampaigns;
  seasonal:SeasonalCampaigns;
}

export interface CouponCampaigns{
  fixedAmount:FixedAmount | null;
  percentageDiscount:PercentageDiscount | null;
}

export interface OnTopCampaigns{
  percentageCategory:PercentageCategory | null;
  pointDiscount:PointDiscount | null;
}

export interface SeasonalCampaigns{
  everyAmount:EveryAmount | null;
}

export interface FixedAmount{
  campaign:string;
  type: CategoryCampaigns.COUPON;
  amount:number;
  discount:number;
}

export interface PercentageDiscount{
  campaign:string;
  type: CategoryCampaigns.COUPON;
  percentage: number;
  discount:number;
}

export interface PercentageCategory{
  campaign:string;
  type: CategoryCampaigns.ONTOP;
  category:string[];
  percentage: number;
  discount:number;
}

export interface PointDiscount{
  campaign:string;
  type: CategoryCampaigns.ONTOP;
  point: number;
  discount:number;
}

export interface EveryAmount{
  campaign:string;
  type: CategoryCampaigns.SEASONAL;
  every:number;
  amount:number;
  discount:number;
}

export interface DiscountRequest{
  cart:Item[];
  discountCampaigns:DiscountCampaigns;
}

export interface DiscountResponse{
  payload:DiscountDetail
}

export interface DiscountDetail{
  amount:String| null;
  useDiscountCampaign:DiscountCampaigns;
  totalPrice:String | null;
}


export enum CategoryCampaigns{
  COUPON = "COUPON",
  ONTOP = "ONTOP",
  SEASONAL = "SEASONAL"
}

export interface errorResponse{
  timestamp:string;
  status:string;
  error:string;
}

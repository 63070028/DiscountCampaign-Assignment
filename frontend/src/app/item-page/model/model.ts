export interface Item{
  name:string;
  category:string;
  price:number;
  quantity:number;
}

export interface DiscountCampaigns{
  coupon:CouponCampaigns | null;
  onTop:OnTopCampaigns | null;
  seasonal:SeasonalCampaigns | null;
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
  amount:number;
  useCampaigns:DiscountCampaigns;
  totalPrice:number;
}


export enum CategoryCampaigns{
  COUPON = "COUPON",
  ONTOP = "ONTOP",
  SEASONAL = "SEASONAL"
}
export interface MenuItemDetail {
  name: string;
  introduce: string;
  price: number;
  imgUrl: string;
  main: boolean;
}

export interface RestaurantDetail {
  id: number;
  name: string;
  menuAverage: number;
  imgUrl: string[];
  streetAddress: string;
  openingHour: string[];
  category: string;
  menu: MenuItemDetail[];
  bookmarked: boolean;
}

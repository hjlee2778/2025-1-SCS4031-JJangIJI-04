export interface MenuItem {
  name: string;
  price: number;
  description?: string;
  imageUrl?: string;
}

export interface Restaurant {
  id: number;
  name: string;
  imageUrls: string[];
  averagePrice: number;
  address: string;
  categories: string[];
  menu: MenuItem[];
  bookmarked: boolean;
}

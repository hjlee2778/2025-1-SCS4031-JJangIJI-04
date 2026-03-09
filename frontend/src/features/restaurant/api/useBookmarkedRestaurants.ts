import { useQuery } from '@tanstack/react-query';
import api from '@/lib/axios';

interface RestaurantItem {
  id: number;
  name: string;
  menuAverage: number;
  imgUrl: string;
  streetAddress: string;
  openingHours: string;
  category: string;
  bookmarked: boolean;
}

export const useBookmarkedRestaurants = () => {
  return useQuery<RestaurantItem[]>({
    queryKey: ['bookmarked-restaurants'],
    queryFn: async () => {
      const res = await api.get<RestaurantItem[]>('/bookmarks/restaurants');
      return res.data;
    },
    staleTime: 1000 * 60 * 3,
  });
};

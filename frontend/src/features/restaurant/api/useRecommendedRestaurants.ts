import { useQuery } from '@tanstack/react-query';
import api from '@/lib/axios';

export interface RecommendedRestaurant {
  id: number;
  name: string;
  menuAverage: number;
  imgUrl: string;
  streetAddress: string;
  openingHours: string;
  category: string;
  bookmarked: boolean;
}

export const useRecommendedRestaurants = () => {
  return useQuery<RecommendedRestaurant[]>({
    queryKey: ['recommendedRestaurants'],
    queryFn: async () => {
      const res = await api.get('/recommendation/restaurants');
      return res.data;
    },
  });
};

import { useQuery } from '@tanstack/react-query';
import api from '@/lib/axios';
import type { RestaurantDetail } from '@/features/restaurant/types/restaurantDetail';

export const useRestaurantDetail = (restaurantId: number) => {
  return useQuery<RestaurantDetail>({
    queryKey: ['restaurantDetail', restaurantId],
    queryFn: async () => {
      const res = await api.get(`/restaurants/${restaurantId}`);
      return res.data;
    },
    enabled: !!restaurantId,
  });
};

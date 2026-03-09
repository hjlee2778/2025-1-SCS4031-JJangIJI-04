import { useMutation, useQueryClient } from '@tanstack/react-query';
import api from '@/lib/axios';

interface ToggleArgs {
  restaurantId: number;
  isBookmarked: boolean;
}

export const useToggleBookmark = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: async ({ restaurantId, isBookmarked }: ToggleArgs) => {
      if (isBookmarked) {
        await api.delete(`/bookmarks/restaurants/${restaurantId}`);
      } else {
        await api.post(`/bookmarks/restaurants/${restaurantId}`);
      }
    },
    onSuccess: (_, variables) => {
      queryClient.invalidateQueries({
        queryKey: ['restaurantDetail', variables.restaurantId],
      });
      queryClient.invalidateQueries({ queryKey: ['recommendedRestaurants'] });
      queryClient.invalidateQueries({ queryKey: ['bookmarked-restaurants'] });
    },
  });
};

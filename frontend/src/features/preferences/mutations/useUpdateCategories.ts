import { useMutation, useQueryClient } from '@tanstack/react-query';
import api from '@/lib/axios';

export const useUpdateCategories = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: async (categories: number[]) => {
      await api.put('/categories', { categories });
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['userInfo'] });
      queryClient.invalidateQueries({ queryKey: ['recommendedRestaurants'] });
    },
  });
};
import { useQuery } from '@tanstack/react-query';
import api from '@/lib/axios';

export const useRemainingBudgetByDate = (date: string) => {
  return useQuery({
    queryKey: ['remainingBudget', date],
    queryFn: async () => {
      const res = await api.get('/saving-goals/remaining', {
        params: { date },
      });
      return res.data;
    },
    retry: false,
  });
};
import { useQuery } from '@tanstack/react-query';
import api from '@/lib/axios';

interface RemainingBudgetResponse {
  remainingBudget: number;
  startDate?: string;
  endDate?: string;
}

export const useRemainingBudget = (date: string) => {
  return useQuery<RemainingBudgetResponse>({
    queryKey: ['remainingBudget', date],
    queryFn: async () => {
      try {
        const res = await api.get('/saving-goals/remaining', {
          params: { date },
        });
        console.log('절약 목표 조회 응답:', res.data);
        return res.data;
      } catch (error) {
        console.error('가용 예산 조회 실패:', error);
        throw error;
      }
    },
    refetchOnMount: true,
    staleTime: 0,
  });
};

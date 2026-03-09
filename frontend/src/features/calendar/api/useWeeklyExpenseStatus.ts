import { useQuery } from '@tanstack/react-query';
import api from '@/lib/axios';

export interface DailyExpenseStatus {
  date: string;
  totalExpense: number;
  status: 'GOOD' | 'NOT_BAD' | 'BAD';
}

export const useWeeklyExpenseStatus = (userId: number | null, startDate: string, endDate: string) => {
    return useQuery<DailyExpenseStatus[]>({
      queryKey: ['weeklyExpenseStatus', userId, startDate, endDate],
      queryFn: async () => {
        if (!userId) throw new Error('User ID is required');
        const res = await api.get(`/users/${userId}/expenses/range`, {
          params: { from: startDate, to: endDate },
        });
        return res.data.dailyExpenseStatus;
      },
      enabled: !!userId && !!startDate && !!endDate,
      retry: 1,
    });
  };
import { useQuery } from '@tanstack/react-query';
import api from '@/lib/axios';
import { startOfMonth, endOfMonth, format } from 'date-fns';
import type { DailyExpenseStatus } from '@/features/calendar/types/expense';

export const useMonthlyExpenseStatus = (userId: number, currentDate: Date) => {
  const from = format(startOfMonth(currentDate), 'yyyy-MM-dd');
  const to = format(endOfMonth(currentDate), 'yyyy-MM-dd');

  return useQuery<DailyExpenseStatus[]>({
    queryKey: ['monthlyExpenseStatus', userId, from, to],
    queryFn: async () => {
      const res = await api.get(`/users/${userId}/expenses/range`, {
        params: { from, to },
      });
      return res.data.dailyExpenseStatus;
    },
    enabled: !!userId,
  });
};

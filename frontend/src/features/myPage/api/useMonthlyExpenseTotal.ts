import { useQuery } from '@tanstack/react-query';
import api from '@/lib/axios';

export const useMonthlyExpenseTotal = (userId: number | null) => {
  const now = new Date();
  const year = now.getFullYear();
  const month = now.getMonth() + 1;

  const startDate = `${year}-${String(month).padStart(2, '0')}-01`;
  const endDate = new Date(year, month, 0).toISOString().split('T')[0]; 

  return useQuery<number>({
    queryKey: ['monthlyExpenseTotal', userId, startDate, endDate],
    queryFn: async () => {
      if (!userId) throw new Error('User ID is required');

      const res = await api.get(`/users/${userId}/expenses/range`, {
        params: { from: startDate, to: endDate },
      });

      const total = res.data.dailyExpenseStatus.reduce(
        (sum: number, cur: { totalExpense: number }) => sum + cur.totalExpense,
        0
      );

      return total;
    },
    enabled: !!userId,
  });
};
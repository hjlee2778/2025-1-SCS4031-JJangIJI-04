import { useMutation, useQueryClient } from '@tanstack/react-query';
import api from '@/lib/axios';

export const useDeleteExpense = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: async (expenseId: number) => {
      await api.delete(`/expenses/${expenseId}`);
    },
    onSuccess: () => {
      queryClient.invalidateQueries({
        predicate: (query) => {
          const key = query.queryKey[0];
          return [
            'dailyExpenses',
            'weeklyExpenseStatus',
            'monthlyExpenseStatus',
            'remainingBudget',
            'community-posts',
            'likedEmojiPosts',
            'monthlyExpenseTotal',
            'recommendedRestaurants',
          ].includes(key as string);
        },
      });
    },
    onError: (error) => {
      console.error('지출 삭제 실패:', error);
      alert('삭제에 실패했어요. 잠시 후 다시 시도해 주세요.');
    },
  });
};

import { useMutation } from '@tanstack/react-query';
import api from '@/lib/axios';

interface AddExpenseRequest {
  restaurantId?: number;
  restaurantName: string;
  menuName: string;
  expense: number;
  memo: string;
  expenseDate: string;
  rating: number;
}

export const useAddExpense = () => {
  return useMutation({
    mutationFn: async (data: AddExpenseRequest) => {
      const processedData = {
        ...data,
        expense: Math.floor(data.expense),
        rating: Math.round(data.rating),
      };
      
      console.log('지출 기록 요청 데이터:', processedData);
      
      const res = await api.post('/expenses', processedData);
      return res.data;
    },
  });
};
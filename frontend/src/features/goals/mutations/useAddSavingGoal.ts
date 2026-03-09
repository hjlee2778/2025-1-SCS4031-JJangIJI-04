import { useMutation, useQueryClient } from '@tanstack/react-query';
import { requestAddSavingGoal, SavingGoalRequest } from '@/features/goals/api/requestAddSavingGoal';
import { useNavigate } from 'react-router-dom';

export const useAddSavingGoal = () => {
  const navigate = useNavigate();
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: async (data: SavingGoalRequest) => {
      const result = await requestAddSavingGoal(data);
      return result;
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['remainingBudget'] });
      queryClient.invalidateQueries({ queryKey: ['dailyExpenses'] });
      queryClient.invalidateQueries({ queryKey: ['weeklyExpenseStatus'] });
      navigate('/main');
    },
    onError: (error: any) => {
      if (error.response?.status === 400) {
        alert('잘못된 요청입니다. 입력값을 확인해주세요.');
      } else if (error.response?.status === 401) {
        alert('로그인이 필요합니다.');
        navigate('/landing');
      } else {
        alert('절약 목표 등록에 실패했습니다. 다시 시도해주세요.');
      }
    },
  });
};



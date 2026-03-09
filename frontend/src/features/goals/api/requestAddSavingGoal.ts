import api from '@/lib/axios';

export interface SavingGoalRequest {
  budget: number;
  startDate: string;
  endDate: string;
}

export const requestAddSavingGoal = async (data: SavingGoalRequest) => {
  const response = await api.post(`/saving-goals`, data);
  return response.data;
};

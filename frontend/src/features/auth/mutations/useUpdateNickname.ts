import { useMutation, useQueryClient } from '@tanstack/react-query';
import api from '@/lib/axios';
import { useAuthStore } from '@/features/auth/store/useAuthStore';

export const useUpdateNickname = () => {
  const queryClient = useQueryClient();
  const setNickname = useAuthStore.getState().setNickname;

  return useMutation({
    mutationFn: async (nickname: string) => {
      await api.patch('/nickname', { nickname });
      return nickname;
    },
    onSuccess: (nickname) => {
      setNickname(nickname);
      queryClient.invalidateQueries({ queryKey: ['userInfo'] });
    },
  });
};

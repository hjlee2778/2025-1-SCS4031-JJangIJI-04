import { useMutation } from '@tanstack/react-query';
import api from '@/lib/axios';
import { useAuthStore } from '@/features/auth/store/useAuthStore';

export const useLogout = () => {
  const clearAuth = useAuthStore((state) => state.clearAuth);

  return useMutation({
    mutationFn: async () => {
      await api.post('/auth/logout');
    },
    onSuccess: () => {
      clearAuth(); 
    },
    onError: () => {
      alert('로그아웃에 실패했어요. 다시 시도해주세요.');
    },
  });
};
import { useQuery } from '@tanstack/react-query';
import { getUserInfo } from '@/features/auth/api/authApi';

export const useUserInfo = () => {
  return useQuery({
    queryKey: ['userInfo'],
    queryFn: getUserInfo,
  });
};

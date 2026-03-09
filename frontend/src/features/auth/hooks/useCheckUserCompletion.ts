import { useNavigate } from 'react-router-dom';
import { getUserInfo } from '@/features/auth/api/authApi';
import { useAuthStore } from '@/features/auth/store/useAuthStore';

export const useCheckUserCompletion = () => {
  const navigate = useNavigate();

  return async (forcedError?: boolean) => {
    if (forcedError) {
      useAuthStore.getState().clearAuth();
      navigate('/landing');
      return;
    }

    try {
      console.log('checkUserCompletion 시작');
      const userInfo = await getUserInfo();

      if (
        !Array.isArray(userInfo.categories) ||
        userInfo.categories.length === 0
      ) {
        navigate('/signup');
        return;
      }

      navigate('/main');
    } catch (error) {
      useAuthStore.getState().clearAuth();
      navigate('/landing');
    }
  };
};

import { useEffect } from 'react';
import { useAuthStore } from '@/features/auth/store/useAuthStore';
import { getUserInfo } from '../api/authApi';

export const useAuthInit = () => {
  const { setInitializing, clearAuth, setRefreshFailed, setUserId, setNickname, setProfileImage, setCategories } = useAuthStore();

  useEffect(() => {
    const initializeAuth = async () => {
      try {
        // 사용자 정보 요청
        const user = await getUserInfo();
        if (user) {
          setUserId(Number(user.id));
          setNickname(user.nickname);
          setProfileImage(user.imageUrl);
          setCategories(user.categories);
          setRefreshFailed(false);
        }
      } catch (error) {
        console.error('초기화 중 에러:', error);
        clearAuth();
        setRefreshFailed(true);
      } finally {
        setInitializing(false);
      }
    };

    initializeAuth();
  }, [setInitializing, clearAuth, setRefreshFailed, setUserId, setNickname, setProfileImage, setCategories]);
};
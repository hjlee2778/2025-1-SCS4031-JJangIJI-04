import { Navigate } from 'react-router-dom';
import { useAuthStore } from '@/features/auth/store/useAuthStore';
import { ReactNode } from 'react';

interface PrivateRouteProps {
  children: ReactNode;
}

export const PrivateRoute = ({ children }: PrivateRouteProps) => {
  const nickname = useAuthStore((state) => state.nickname);
  const isInitializing = useAuthStore((state) => state.isInitializing);

  //로컬에서 UI 테스트 할 때 return 부분 주석 처리
  if (isInitializing) return null;
  if (!nickname) return <Navigate to="/landing" replace />;
  return children;
};

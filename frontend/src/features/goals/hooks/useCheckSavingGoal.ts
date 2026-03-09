import { useEffect, useRef } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { useRemainingBudget } from '@/features/goals/api/useRemainingBudget';
import { AxiosError } from 'axios';
import { format } from 'date-fns';

export const useCheckSavingGoal = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const today = format(new Date(), 'yyyy-MM-dd');
  const { error, data, isSuccess, isLoading } = useRemainingBudget(today);
  const redirectAttempted = useRef(false);

  useEffect(() => {
    if (isLoading || redirectAttempted.current) return;

    const excludedPaths = ['/oauth/callback/kakao', '/signup', '/landing'];
    const pathname = location.pathname;
    if (excludedPaths.includes(pathname)) return;

    if (isSuccess && data?.remainingBudget) {
      // startDate와 endDate가 응답에 포함되는 경우
      if (data.startDate && data.endDate) {
        // 주석 해제 시: 목표 유효기간이 지난 경우 weeklygoal로 리디렉션
        /*
        const start = parseISO(data.startDate);
        const end = parseISO(data.endDate);
        const isGoalExpired = !isWithinInterval(today, { start, end });

        if (isGoalExpired) {
          redirectAttempted.current = true;
          navigate('/weeklygoal', { replace: true });
          return;
        }
        */
      }

      // ✅ 기본 로직: 목표가 있고, 현재 위치가 weeklygoal이면 main으로 이동
      if (pathname === '/weeklygoal') {
        redirectAttempted.current = true;
        navigate('/main', { replace: true });
      }
      return;
    }

    if (error) {
      const axiosError = error as AxiosError<{ exceptionCode: string }>;
      const isGoalMissing =
        axiosError.response?.data?.exceptionCode ===
        'EXPENSE_SAVING_GOAL_NOT_FOUND';

      if (
        isGoalMissing &&
        pathname !== '/weeklygoal' &&
        !excludedPaths.includes(pathname)
      ) {
        redirectAttempted.current = true;
        navigate('/weeklygoal', { replace: true });
      }
    }
  }, [isLoading, isSuccess, error, data, location.pathname, navigate]);

  useEffect(() => {
    redirectAttempted.current = false;
  }, [location.pathname]);
};

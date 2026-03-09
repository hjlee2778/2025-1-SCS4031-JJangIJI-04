import { createBrowserRouter } from 'react-router-dom';
import { Layout } from '@/shared/layouts/Layout';
import { PrivateRoute } from '@/app/routes/PrivateRoute';
import LandingPage from '@/pages/Landing/LandingPage';
import OAuthCallbackPage from '@/pages/OAuthCallbackPage/OAuthCallbackPage';
import SignupPage from '@/pages/SignupPage/SignupPage';
import { RestaurantsPage } from '@/pages/Restaurants/RestaurantsPage';
import { RestaurantDetailPage } from '@/pages/Restaurants/RestaurantDetailPage';
import MainPage from '@/pages/MainPage/MainPage';
import WeeklyGoalPage from '@/pages/WeeklyGoalPage/WeeklyGoalPage';
import { CommunityPage } from '@/pages/Community/CommunityPage';
import RecordPage from '@/pages/RecordPage/RecordPage';
import MyPage from '@/pages/MyPage/MyPage';
import GeneralExpensePageWrapper from '@/pages/GeneralExpensePageWrapper/GeneralExpensePageWrapper';

export const router = createBrowserRouter([
  // 카카오 콜백 페이지 별도
  {
    path: '/oauth/callback/kakao',
    element: <OAuthCallbackPage />,
  },

  //Footer 없는 공개 페이지
  {
    path: '/',
    element: <Layout hasFooter={false} />,
    children: [
      { index: true, element: <LandingPage /> },
      { path: 'landing', element: <LandingPage /> },
    ],
  },
  {
    path: 'signup',
    element: <Layout hasFooter={false} />,
    children: [
      {
        index: true,
        element: <SignupPage />,
      },
    ],
  },

  //Footer 없는 인증 필요 페이지
  {
    path: 'restaurants/:id',
    element: <Layout hasFooter={false} />,
    children: [
      {
        index: true,
        element: (
          <PrivateRoute>
            <RestaurantDetailPage />
          </PrivateRoute>
        ),
      },
    ],
  },
  {
    path: 'weeklygoal',
    element: <Layout hasFooter={false} />,
    children: [
      {
        index: true,
        element: (
          <PrivateRoute>
            <WeeklyGoalPage />
          </PrivateRoute>
        ),
      },
    ],
  },
  {
    path: 'record',
    element: <Layout hasFooter={false} />,
    children: [
      {
        index: true,
        element: (
          <PrivateRoute>
            <RecordPage />
          </PrivateRoute>
        ),
      },
    ],
  },
  {
    path: 'users/:userId/expenses',
    element: <Layout hasFooter={false} />,
    children: [
      {
        index: true,
        element: (
          <PrivateRoute>
            <GeneralExpensePageWrapper />
          </PrivateRoute>
        ),
      },
    ],
  },

  //Footer 있는 인증 필요 페이지
  {
    path: 'main',
    element: <Layout hasFooter={true} />,
    children: [
      {
        index: true,
        element: (
          <PrivateRoute>
            <MainPage />
          </PrivateRoute>
        ),
      },
    ],
  },
  {
    path: 'restaurants',
    element: <Layout hasFooter={true} />,
    children: [
      {
        index: true,
        element: (
          <PrivateRoute>
            <RestaurantsPage />
          </PrivateRoute>
        ),
      },
    ],
  },
  {
    path: 'community',
    element: <Layout hasFooter={true} />,
    children: [
      {
        index: true,
        element: (
          <PrivateRoute>
            <CommunityPage />
          </PrivateRoute>
        ),
      },
    ],
  },
  {
    path: 'mypage',
    element: <Layout hasFooter={true} />,
    children: [
      {
        index: true,
        element: (
          <PrivateRoute>
            <MyPage />
          </PrivateRoute>
        ),
      },
    ],
  },
]);

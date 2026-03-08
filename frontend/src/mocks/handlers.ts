import { http, HttpResponse } from 'msw';

export const handlers = [
  http.post('/auth/kakao', async ({ request }) => {
    const body = await request.json() as { code: string; redirect_uri?: string };
    
    // 모킹된 code 체크
    if (body.code === 'mock-kakao-code') {
      return HttpResponse.json({
        accessToken: 'mock-kakao-access-token',
        refreshToken: 'mock-kakao-refresh-token',
        nickname: '테스트 계정',
        image_url: 'https://example.com/image.png',
      });
    }
    
    return HttpResponse.json({
      accessToken: 'mock-kakao-access-token',
      refreshToken: 'mock-kakao-refresh-token',
      nickname: '테스트 계정',
      image_url: 'https://example.com/image.png',
    });
  }),

  http.post('/auth/refresh', () => {
    return HttpResponse.json({
      accessToken: 'mock-new-access-token',
      expiresIn: 3600,
    });
  }),

  // 로그아웃
  http.post('/auth/logout', () => {
    return HttpResponse.json({ message: 'Logged out' });
  }),

  // 사용자 정보 조회
  http.get('/users/me', () => {
    return HttpResponse.json({
      id: 'mock-user-id',
      email: 'test@kakao.com',
      nickname: '테스트 계정',
      imageUrl: 'https://example.com/image.png',
      loginType: 'kakao',
      role: 'user',
      isExpenseOpen: 'true',
      categories: [
        { categoryId: 1, name: '한식' },
        { categoryId: 2, name: '중식' },
        { categoryId: 3, name: '일식' }
      ]
    });
  }),
];

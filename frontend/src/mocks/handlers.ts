import { http, HttpResponse } from 'msw';

export const handlers = [
  http.post('/auth/kakao', () => {
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
  http.get('/auth/me', () => {
    return HttpResponse.json({
      nickname: '테스트 계정',
      image_url: 'https://example.com/image.png',
    });
  }), 
];

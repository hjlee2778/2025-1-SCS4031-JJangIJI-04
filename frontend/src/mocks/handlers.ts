import { http, HttpResponse } from 'msw';

export const handlers = [
  http.post('/auth/kakao', () => {
    return HttpResponse.json({
      accessToken: 'mock-kakao-access-token',
      nickname: '테스트 계정',
      image_url: 'https://example.com/image.png',
    });
  }),
];

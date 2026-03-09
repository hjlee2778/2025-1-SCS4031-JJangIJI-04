import { http, HttpResponse } from 'msw';

// 메모리에 상태 저장 (날짜별 지출 데이터)
const expensesDB: Record<string, any[]> = {
  '2026-03-08': [
    { id: 1, restaurant: '맛있는 한식당', menu: '김치찌개', expense: 8000, memo: '점심 식사', emojis: [] },
    { id: 2, restaurant: '카페', menu: '아메리카노', expense: 4500, memo: '오후 커피', emojis: [] }
  ],
  '2026-03-07': [
    { id: 3, restaurant: '일식당', menu: '돈까스', expense: 12000, memo: '저녁', emojis: [] }
  ],
  '2026-03-05': [
    { id: 4, restaurant: '분식집', menu: '떡볶이', expense: 6000, memo: '간식', emojis: [] }
  ],
};

// 주간 예산 설정
const weeklyBudget = 50000;

let expenseIdCounter = 100;

export const handlers = [
  http.post('/auth/kakao', async ({ request }) => {
    const body = await request.json() as { code: string; redirectUri?: string };
    
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

  http.post('/auth/logout', () => {
    return HttpResponse.json({ message: 'Logged out' });
  }),
  
  // 사용자 정보 조회
  http.get('/users/me', () => {
    return HttpResponse.json({
      id: 1,
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

  // 일별 지출 내역 (동적)
  http.get('/users/:userId/expenses', ({ request }) => {
    const url = new URL(request.url);
    const date = url.searchParams.get('date') || '';
    
    const expenses = expensesDB[date] || [];
    const totalExpense = expenses.reduce((sum, e) => sum + e.expense, 0);
    const remaining = weeklyBudget - totalExpense;
    
    return HttpResponse.json({
      savingGoalStatus: {
        budget: weeklyBudget,
        remainingBudget: remaining,
        remainingPercentage: (remaining / weeklyBudget) * 100,
        message: remaining > 0 ? '잘 지키고 있어요!' : '예산을 초과했어요!'
      },
      expenses
    });
  }),

  // 남은 예산 조회
  http.get('/saving-goals/remaining', ({ request }) => {
    const url = new URL(request.url);
    const date = url.searchParams.get('date') || '';
    
    const expenses = expensesDB[date] || [];
    const totalExpense = expenses.reduce((sum, e) => sum + e.expense, 0);
    
    return HttpResponse.json({
      remainingBudget: weeklyBudget - totalExpense,
      totalBudget: weeklyBudget
    });
  }),

  // 지출 기록 추가
  http.post('/users/:userId/expenses', async ({ request }) => {
    const body = await request.json() as any;
    const { date, restaurant, menu, expense, memo } = body;
    
    if (!expensesDB[date]) {
      expensesDB[date] = [];
    }
    
    const newExpense = {
      id: ++expenseIdCounter,
      restaurant,
      menu,
      expense,
      memo: memo || '',
      emojis: []
    };
    
    expensesDB[date].push(newExpense);
    
    return HttpResponse.json(newExpense, { status: 201 });
  }),

  // 지출 기록 삭제
  http.delete('/users/:userId/expenses/:expenseId', ({ params }) => {
    const { expenseId } = params;
    
    // 모든 날짜에서 해당 expenseId 찾아서 삭제
    for (const date in expensesDB) {
      expensesDB[date] = expensesDB[date].filter(e => e.id !== Number(expenseId));
    }
    
    return HttpResponse.json({ message: 'Deleted' });
  }),

  // 기간별 지출 현황 (달력용 - from, to 파라미터)
  http.get('/users/:userId/expenses/range', ({ request }) => {
    const url = new URL(request.url);
    const from = url.searchParams.get('from') || '';
    const to = url.searchParams.get('to') || '';
    
    // 일일 예산 기준 (주간 예산 / 7)
    const dailyBudget = weeklyBudget / 7;
    
    const dailyStatus = Object.keys(expensesDB)
      .filter(date => date >= from && date <= to)
      .map(date => {
        const expenses = expensesDB[date];
        const total = expenses.reduce((sum, e) => sum + e.expense, 0);
        
        // 지출 금액에 따라 상태 결정
        let status: 'GOOD' | 'NOT_BAD' | 'BAD';
        if (total === 0) {
          status = 'GOOD';
        } else if (total <= dailyBudget * 0.7) {
          status = 'GOOD';
        } else if (total <= dailyBudget) {
          status = 'NOT_BAD'; 
        } else {
          status = 'BAD';
        }
        
        return {
          date,
          totalExpense: total,
          status
        };
      });
    
    return HttpResponse.json({ dailyExpenseStatus: dailyStatus });
  }),

  // 주간 지출 현황
  http.get('/users/:userId/expenses/weekly', ({ request }) => {
    const url = new URL(request.url);
    const startDate = url.searchParams.get('startDate') || '';
    const endDate = url.searchParams.get('endDate') || '';
    
    const dailyStatus = Object.keys(expensesDB)
      .filter(date => date >= startDate && date <= endDate)
      .map(date => {
        const expenses = expensesDB[date];
        const total = expenses.reduce((sum, e) => sum + e.expense, 0);
        return {
          date,
          totalExpense: total
        };
      });
    
    return HttpResponse.json({ dailyExpenseStatus: dailyStatus });
  }),
];
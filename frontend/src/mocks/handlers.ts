import { http, HttpResponse } from 'msw';

// 메모리에 상태 저장 (날짜별 지출 데이터)
const expensesDB: Record<string, any[]> = {
  '2026-03-08': [
    { id: 1, restaurant: '맛있는 한식당', menu: '김치찌개', expense: 8000, memo: '점심 식사', emojis: [] },
    { id: 2, restaurant: '가성비 중식당', menu: '짜장면', expense: 6500, memo: '간단한 점심', emojis: [] }
  ],
  '2026-03-07': [
    { id: 3, restaurant: '일식 맛집', menu: '돈까스', expense: 12000, memo: '저녁', emojis: [] }
  ],
  '2026-03-05': [
    { id: 4, restaurant: '분식 천국', menu: '떡볶이', expense: 5500, memo: '간식', emojis: [] }
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
      imageUrl: '/icons/community/user-avatar.svg',
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

  // 커뮤니티 피드 (페이지네이션)
  http.get('/community/expenses', ({ request }) => {
    const url = new URL(request.url);
    const page = Number(url.searchParams.get('page')) || 0;
    const size = Number(url.searchParams.get('size')) || 10;
    
    // 모든 지출 데이터를 날짜 역순으로 정렬하여 피드 생성
    const allExpenses = Object.entries(expensesDB)
      .flatMap(([date, expenses]) => 
        expenses.map(exp => {
          const totalExpense = expenses.reduce((sum, e) => sum + e.expense, 0);
          return {
            expenseId: exp.id,
            userId: 1,
            nickname: '테스트 계정',
            imageUrl: '/icons/community/user-avatar.svg',
            restaurantId: exp.id,
            restaurant: exp.restaurant,
            menu: exp.menu,
            expense: exp.expense,
            createdAt: `${date}T12:00:00`, // ISO 형식으로 날짜 추가
            memo: exp.memo || '맛있게 먹었어요!',
            savingGoalId: 1,
            savingGoal: weeklyBudget,
            remainingBudget: weeklyBudget - totalExpense,
            emojis: exp.emojis || [],
          };
        })
      )
      .sort((a, b) => b.createdAt.localeCompare(a.createdAt)); // 최신순
    
    // 페이지네이션
    const start = page * size;
    const end = start + size;
    const paginatedData = allExpenses.slice(start, end);
    
    return HttpResponse.json(paginatedData);
  }),

  // 커뮤니티 이모지 토글
  http.post('/community/expenses/:expenseId/emojis/:emojiId', ({ params }) => {
    const { expenseId, emojiId } = params;
    
    // 지출 찾기 및 이모지 토글
    for (const date in expensesDB) {
      const expense = expensesDB[date].find(e => e.id === Number(expenseId));
      if (expense) {
        if (!expense.emojis) expense.emojis = [];
        
        const existingEmoji = expense.emojis.find((e: any) => e.emojiId === Number(emojiId));
        if (existingEmoji) {
          existingEmoji.count = (existingEmoji.count || 0) + 1;
        } else {
          expense.emojis.push({ emojiId: Number(emojiId), count: 1 });
        }
        
        return HttpResponse.json({ message: 'Success' });
      }
    }
    
    return HttpResponse.json({ message: 'Not found' }, { status: 404 });
  }),

  // 추천 레스토랑 목록
  http.get('/recommendation/restaurants', () => {
    return HttpResponse.json([
      {
        id: 1,
        name: '맛있는 한식당',
        menuAverage: 8500,
        imgUrl: '/images/basic-restaurant.svg',
        streetAddress: '서울 강남구 테헤란로 123',
        openingHours: '11:00-22:00',
        category: '한식',
        bookmarked: false,
      },
      {
        id: 2,
        name: '가성비 중식당',
        menuAverage: 7000,
        imgUrl: '/images/basic-restaurant.svg',
        streetAddress: '서울 서초구 서초대로 456',
        openingHours: '10:00-21:00',
        category: '중식',
        bookmarked: false,
      },
      {
        id: 3,
        name: '일식 맛집',
        menuAverage: 12000,
        imgUrl: '/images/basic-restaurant.svg',
        streetAddress: '서울 강남구 역삼동 789',
        openingHours: '11:30-22:00',
        category: '일식',
        bookmarked: true,
      },
      {
        id: 4,
        name: '분식 천국',
        menuAverage: 5000,
        imgUrl: '/images/basic-restaurant.svg',
        streetAddress: '서울 마포구 홍대입구 101',
        openingHours: '09:00-20:00',
        category: '분식',
        bookmarked: false,
      },
      {
        id: 5,
        name: '양식 레스토랑',
        menuAverage: 15000,
        imgUrl: '/images/basic-restaurant.svg',
        streetAddress: '서울 강남구 청담동 202',
        openingHours: '12:00-22:00',
        category: '양식',
        bookmarked: false,
      },
    ]);
  }),

  // 북마크 토글
  http.post('/bookmarks/restaurants/:restaurantId', ({ params }) => {
    const { restaurantId } = params;
    return HttpResponse.json({ 
      message: 'Bookmark toggled',
      restaurantId: Number(restaurantId)
    });
  }),

  // 식당 상세 정보
  http.get('/restaurants/:restaurantId', ({ params }) => {
    const { restaurantId } = params;
    const id = Number(restaurantId);
    
    // 더미 상세 데이터 (ID별로 다르게)
    const restaurantDetails: Record<number, any> = {
      1: {
        id: 1,
        name: '맛있는 한식당',
        menuAverage: 8500,
        imgUrl: ['/images/basic-restaurant.svg', '/images/basic-restaurant.svg'],
        streetAddress: '서울 강남구 테헤란로 123',
        openingHour: [
          '월 11:00 - 22:00',
          '화 11:00 - 22:00',
          '수 11:00 - 22:00',
          '목 11:00 - 22:00',
          '금 11:00 - 22:00',
          '토 12:00 - 21:00',
          '일 null'
        ],
        category: '한식',
        menu: [
          {
            name: '김치찌개',
            introduce: '얼큰하고 맛있는 김치찌개입니다',
            price: 8000,
            imgUrl: '/images/basic-restaurant.svg',
            main: true
          },
          {
            name: '된장찌개',
            introduce: '구수한 된장찌개',
            price: 8000,
            imgUrl: '/images/basic-restaurant.svg',
            main: false
          },
          {
            name: '제육볶음',
            introduce: '매콤한 제육볶음',
            price: 9000,
            imgUrl: '/images/basic-restaurant.svg',
            main: false
          }
        ],
        bookmarked: false
      },
      2: {
        id: 2,
        name: '가성비 중식당',
        menuAverage: 7000,
        imgUrl: ['/images/basic-restaurant.svg'],
        streetAddress: '서울 서초구 서초대로 456',
        openingHour: [
          '월 10:00 - 21:00',
          '화 10:00 - 21:00',
          '수 10:00 - 21:00',
          '목 10:00 - 21:00',
          '금 10:00 - 21:00',
          '토 10:00 - 21:00',
          '일 10:00 - 21:00'
        ],
        category: '중식',
        menu: [
          {
            name: '짜장면',
            introduce: '전통 짜장면',
            price: 6000,
            imgUrl: '/images/basic-restaurant.svg',
            main: true
          },
          {
            name: '짬뽕',
            introduce: '얼큰한 짬뽕',
            price: 7000,
            imgUrl: '/images/basic-restaurant.svg',
            main: false
          },
          {
            name: '탕수육',
            introduce: '바삭한 탕수육',
            price: 15000,
            imgUrl: '/images/basic-restaurant.svg',
            main: false
          }
        ],
        bookmarked: false
      },
      3: {
        id: 3,
        name: '일식 맛집',
        menuAverage: 12000,
        imgUrl: ['/images/basic-restaurant.svg'],
        streetAddress: '서울 강남구 역삼동 789',
        openingHour: [
          '월 11:30 - 22:00',
          '화 11:30 - 22:00',
          '수 11:30 - 22:00',
          '목 11:30 - 22:00',
          '금 11:30 - 23:00',
          '토 11:30 - 23:00',
          '일 11:30 - 22:00'
        ],
        category: '일식',
        menu: [
          {
            name: '돈까스',
            introduce: '바삭한 일본식 돈까스',
            price: 11000,
            imgUrl: '/images/basic-restaurant.svg',
            main: true
          },
          {
            name: '우동',
            introduce: '따뜻한 우동',
            price: 9000,
            imgUrl: '/images/basic-restaurant.svg',
            main: false
          },
          {
            name: '카츠동',
            introduce: '돈까스 덮밥',
            price: 10000,
            imgUrl: '/images/basic-restaurant.svg',
            main: false
          }
        ],
        bookmarked: true
      },
      4: {
        id: 4,
        name: '분식 천국',
        menuAverage: 5000,
        imgUrl: ['/images/basic-restaurant.svg'],
        streetAddress: '서울 마포구 홍대입구 101',
        openingHour: [
          '월 09:00 - 20:00',
          '화 09:00 - 20:00',
          '수 09:00 - 20:00',
          '목 09:00 - 20:00',
          '금 09:00 - 20:00',
          '토 09:00 - 20:00',
          '일 null'
        ],
        category: '분식',
        menu: [
          {
            name: '떡볶이',
            introduce: '매콤달콤 떡볶이',
            price: 4000,
            imgUrl: '/images/basic-restaurant.svg',
            main: true
          },
          {
            name: '튀김',
            introduce: '바삭한 튀김',
            price: 3000,
            imgUrl: '/images/basic-restaurant.svg',
            main: false
          },
          {
            name: '순대',
            introduce: '신선한 순대',
            price: 4000,
            imgUrl: '/images/basic-restaurant.svg',
            main: false
          }
        ],
        bookmarked: false
      },
      5: {
        id: 5,
        name: '양식 레스토랑',
        menuAverage: 15000,
        imgUrl: ['/images/basic-restaurant.svg'],
        streetAddress: '서울 강남구 청담동 202',
        openingHour: [
          '월 null',
          '화 12:00 - 22:00',
          '수 12:00 - 22:00',
          '목 12:00 - 22:00',
          '금 12:00 - 23:00',
          '토 12:00 - 23:00',
          '일 12:00 - 22:00'
        ],
        category: '양식',
        menu: [
          {
            name: '크림 파스타',
            introduce: '부드러운 크림 파스타',
            price: 14000,
            imgUrl: '/images/basic-restaurant.svg',
            main: true
          },
          {
            name: '토마토 파스타',
            introduce: '새콤달콤 토마토 파스타',
            price: 13000,
            imgUrl: '/images/basic-restaurant.svg',
            main: false
          },
          {
            name: '스테이크',
            introduce: '육즙 가득 스테이크',
            price: 25000,
            imgUrl: '/images/basic-restaurant.svg',
            main: false
          }
        ],
        bookmarked: false
      }
    };
    
    // 해당 ID의 상세 정보가 있으면 반환, 없으면 기본값
    const detail = restaurantDetails[id] || {
      id,
      name: `식당 ${id}`,
      menuAverage: 10000,
      imgUrl: ['/images/basic-restaurant.svg'],
      streetAddress: '서울시',
      openingHour: ['매일 11:00 - 22:00'],
      category: '한식',
      menu: [
        {
          name: '대표 메뉴',
          introduce: '맛있는 메뉴입니다',
          price: 10000,
          imgUrl: '/images/basic-restaurant.svg',
          main: true
        }
      ],
      bookmarked: false
    };
    
    return HttpResponse.json(detail);
  }),
];

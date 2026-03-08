import { Restaurant } from '@/features/restaurant/types/restaurant';

const mockRestaurants: Restaurant[] = [
  {
    id: '1',
    name: '음식점 A',
    imageUrls: ['/images/sample1.png', '/images/sample1.png'],
    averagePrice: 11000,
    location: '서울 강남구',
    categories: ['한식'],
    menu: [
      {
        name: '된장찌개',
        price: 8000,
        description: '구수한 된장찌개',
        imageUrl: '/images/sample1.png',
      },
      {
        name: '김치찌개',
        price: 8500,
        description: '얼큰한 김치찌개',
        imageUrl: '/images/sample1.png',
      },
    ],
    bookmarked: true,
  },
  {
    id: '2',
    name: '음식점 B',
    imageUrls: ['/images/sample1.png'],
    averagePrice: 15000,
    location: '서울 서초구',
    categories: ['일식'],
    menu: [
      {
        name: '돈까스',
        price: 12000,
        description: '바삭한 돈까스',
        imageUrl: '/images/sample1.png',
      },
    ],
    bookmarked: false,
  },
  {
    id: '3',
    name: '음식점 C',
    imageUrls: ['/images/sample1.png'],
    averagePrice: 12000,
    location: '서울 용산구',
    categories: ['중식'],
    menu: [
      {
        name: '짜장면',
        price: 7000,
        description: '전통 짜장면',
        imageUrl: '/images/sample1.png',
      },
      {
        name: '짬뽕',
        price: 8000,
        description: '얼큰한 짬뽕',
        imageUrl: '/images/sample1.png',
      },
    ],
    bookmarked: false,
  },
  {
    id: '4',
    name: '음식점 D',
    imageUrls: ['/images/sample1.png'],
    averagePrice: 8500,
    location: '서울 마포구',
    categories: ['분식'],
    menu: [
      {
        name: '떡볶이',
        price: 5000,
        description: '달콤 매콤 떡볶이',
        imageUrl: '/images/sample1.png',
      },
    ],
    bookmarked: true,
  },
  {
    id: '5',
    name: '음식점 E',
    imageUrls: ['/images/sample1.png'],
    averagePrice: 9800,
    location: '서울 송파구',
    categories: ['양식'],
    menu: [
      {
        name: '파스타',
        price: 13000,
        description: '크림 파스타',
        imageUrl: '/images/sample1.png',
      },
    ],
    bookmarked: false,
  },
  {
    id: '6',
    name: '음식점 F',
    imageUrls: ['/images/sample1.png'],
    averagePrice: 13000,
    location: '서울 강동구',
    categories: ['한식', '퓨전'],
    menu: [
      {
        name: '비빔밥',
        price: 10000,
        description: '신선한 비빔밥',
        imageUrl: '/images/sample1.png',
      },
    ],
    bookmarked: false,
  },
  {
    id: '7',
    name: '음식점 G',
    imageUrls: ['/images/sample1.png'],
    averagePrice: 10500,
    location: '서울 광진구',
    categories: ['중식'],
    menu: [
      {
        name: '탕수육',
        price: 15000,
        description: '바삭한 탕수육',
        imageUrl: '/images/sample1.png',
      },
    ],
    bookmarked: true,
  },
  {
    id: '8',
    name: '음식점 H',
    imageUrls: ['/images/sample1.png'],
    averagePrice: 17500,
    location: '서울 동작구',
    categories: ['일식'],
    menu: [
      {
        name: '스시 세트',
        price: 20000,
        description: '신선한 스시',
        imageUrl: '/images/sample1.png',
      },
    ],
    bookmarked: false,
  },
  {
    id: '9',
    name: '음식점 I',
    imageUrls: ['/images/sample1.png'],
    averagePrice: 8900,
    location: '서울 중구',
    categories: ['분식'],
    menu: [
      {
        name: '김밥',
        price: 3500,
        description: '신선한 김밥',
        imageUrl: '/images/sample1.png',
      },
    ],
    bookmarked: true,
  },
  {
    id: '10',
    name: '음식점 J',
    imageUrls: ['/images/sample1.png'],
    averagePrice: 9200,
    location: '서울 은평구',
    categories: ['한식'],
    menu: [
      {
        name: '김치찌개',
        price: 8500,
        description: '매콤한 김치찌개',
        imageUrl: '/images/sample1.png',
      },
    ],
    bookmarked: false,
  },
];

export const getRestaurants = async (): Promise<Restaurant[]> => {
  // 실제 API 연동 또는 목데이터

  return mockRestaurants;
};

export const getRestaurantById = async (id: string): Promise<Restaurant> => {
  // id로 단일 식당 정보 fetch
  const restaurant = mockRestaurants.find((r) => r.id === id);
  if (!restaurant) {
    throw new Error('Restaurant not found');
  }
  return restaurant;
};

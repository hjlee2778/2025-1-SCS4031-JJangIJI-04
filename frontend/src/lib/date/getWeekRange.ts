import { startOfWeek, endOfWeek, format } from 'date-fns';

export const getWeekRange = (date: Date) => {
  const start = startOfWeek(date, { weekStartsOn: 0 }); // 일요일 시작
  const end = endOfWeek(date, { weekStartsOn: 0 }); // 토요일 끝

  return {
    startDate: format(start, 'yyyy-MM-dd'),
    endDate: format(end, 'yyyy-MM-dd'),
  };
};

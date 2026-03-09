import styled from 'styled-components';
import { ExpenseCard } from '@/features/spendingStatus/ui/ExpenseCard';
import { ExpenseRecord } from '@/features/spendingStatus/api/useDailyExpenses';

interface DailyExpenseSectionProps {
  date: string; // e.g. "2025-05-19"
  records: ExpenseRecord[];
}

export const DailyExpenseSection = ({ date, records }: DailyExpenseSectionProps) => {
  const formattedDate = new Date(date).toLocaleDateString('ko-KR', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    weekday: 'long',
  });

  return (
    <SectionWrapper>
      <DateLabel>{formattedDate}</DateLabel>
      <CountText>한끼모아님의 외식비 지출 내역 {records.length}건</CountText>

      {records.map((record, index) => (
        <ExpenseCard key={index} {...record} />
      ))}
    </SectionWrapper>
  );
};

const SectionWrapper = styled.div`
  margin-top: 32px;
`;

const DateLabel = styled.h4`
  font-size: 14px;
  color: #999;
  margin-bottom: 4px;
`;

const CountText = styled.p`
  font-size: 14px;
  font-weight: bold;
  color: #333;
  margin-bottom: 12px;
`;
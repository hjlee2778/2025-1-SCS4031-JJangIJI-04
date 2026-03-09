import styled from 'styled-components';
import { format, parseISO, eachDayOfInterval } from 'date-fns';
import { ko } from 'date-fns/locale';
import type { DailyExpenseStatus } from '@/features/calendar/types/expense';
import { formatExpenseAmount } from '@/lib/number/formatExpenseAmount';
import { getWeekRange } from '@/lib/date/getWeekRange';
import GoodIcon from '@/assets/icons/good-icon.svg?react';
import NormalIcon from '@/assets/icons/normal-icon.svg?react';
import DangerousIcon from '@/assets/icons/dangerous-icon.svg?react';
import BasicIcon from '@/assets/icons/basic-icon.svg?react';

interface Props {
  dailyStatusList: DailyExpenseStatus[];
  onExpand: () => void;
  onDateSelect?: (date: string) => void;
  selectedDate?: string;
}

export const MiniCalendar = ({
  dailyStatusList,
  onExpand,
  onDateSelect,
  selectedDate,
}: Props) => {
  const referenceDate = selectedDate ? parseISO(selectedDate) : new Date();
  const { startDate, endDate } = getWeekRange(referenceDate);
  const weekDates = eachDayOfInterval({
    start: parseISO(startDate),
    end: parseISO(endDate),
  });

  const today = new Date();
  const statusMap = Object.fromEntries(dailyStatusList.map((d) => [d.date, d]));

  return (
    <Wrapper>
      <Content>
        {weekDates.map((date) => {
          const dateStr = format(date, 'yyyy-MM-dd');
          const isFuture = date > today;
          const isSelected = dateStr === selectedDate;

          const dayData = statusMap[dateStr];
          const Icon = getIconByStatus(isFuture ? undefined : dayData?.status);
          const amountText = dayData?.totalExpense
            ? formatExpenseAmount(dayData.totalExpense)
            : '-';

          return (
            <Day
              key={dateStr}
              onClick={() => !isFuture && onDateSelect?.(dateStr)}
              $isFuture={isFuture}
            >
              <Label>{format(date, 'E', { locale: ko })}</Label>
              <DateText>{format(date, 'd')}</DateText>
              <Face $isSelected={isSelected} $isFuture={isFuture}>
                <Icon />
              </Face>
              <Amount>{amountText}</Amount>
            </Day>
          );
        })}
      </Content>
      <HandleWrapper onClick={onExpand}>
        <Handle />
      </HandleWrapper>
    </Wrapper>
  );
};

function getIconByStatus(status?: 'GOOD' | 'NOT_BAD' | 'BAD') {
  switch (status) {
    case 'GOOD':
      return GoodIcon;
    case 'NOT_BAD':
      return NormalIcon;
    case 'BAD':
      return DangerousIcon;
    default:
      return BasicIcon;
  }
}

const Wrapper = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  padding: 12px 24px 14px;
  height: auto;
  min-height: 110px;
  background-color: #fff;
  max-width: 390px;
  margin: 0 auto;
  box-sizing: border-box;
`;

const Content = styled.div`
  display: flex;
  justify-content: space-between;
  width: 100%;
  flex: 1;
`;

const HandleWrapper = styled.div`
  display: flex;
  justify-content: center;
  padding-top: 20px;
`;

const Handle = styled.div`
  width: 40px;
  height: 3px;
  background-color: #808080;
  border-radius: 999px;
  cursor: pointer;
`;

const Day = styled.div<{ $isFuture: boolean }>`
  text-align: center;
  flex: 1;
  cursor: ${({ $isFuture }) => ($isFuture ? 'not-allowed' : 'pointer')};
`;

const Label = styled.div`
  font-size: 12px;
  color: #999;
`;

const DateText = styled.div`
  font-size: 11px;
  font-weight: 600;
  color: #202632;
  margin-top: 2px;
`;

const Face = styled.div.withConfig({
  shouldForwardProp: (prop) => !['$isSelected', '$isFuture'].includes(prop),
})<{ $isSelected: boolean; $isFuture: boolean }>`
  display: inline-flex;
  align-items: center;
  justify-content: center;
  margin: 2px 0 2px;
  width: 36px;
  height: 36px;
  border-radius: 50%;
  cursor: ${({ $isFuture }) => ($isFuture ? 'not-allowed' : 'pointer')};

  ${({ $isSelected }) =>
    $isSelected &&
    `
    border: 2.5px solid #FD6918;
  `}

  svg {
    width: 24px;
    height: 24px;
  }
`;

const Amount = styled.div`
  font-size: 10px;
  font-weight: 500;
  color: #202632;
  margin-top: 1px;
`;

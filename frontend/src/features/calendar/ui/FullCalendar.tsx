import styled from 'styled-components';
import {
  format,
  startOfMonth,
  endOfMonth,
  eachDayOfInterval,
  getDay,
  isFuture,
  subMonths,
  addMonths,
} from 'date-fns';
import type { DailyExpenseStatus } from '@/features/calendar/types/expense';

import GoodIcon from '@/assets/icons/good-icon.svg?react';
import NormalIcon from '@/assets/icons/normal-icon.svg?react';
import DangerousIcon from '@/assets/icons/dangerous-icon.svg?react';
import BasicIcon from '@/assets/icons/basic-icon.svg?react';
import BeforeIcon from '@/assets/icons/navigate-before.svg?react';
import AfterIcon from '@/assets/icons/navigate-after.svg?react';
import { formatExpenseAmount } from '@/lib/number/formatExpenseAmount';

interface Props {
  currentDate: Date;
  onDateChange: (newDate: Date) => void;
  dailyStatusList: DailyExpenseStatus[];
  onCollapse?: () => void;
  onDateSelect?: (dateStr: string) => void;
  selectedDate?: string;
}

export const FullCalendar = ({
  currentDate,
  onDateChange,
  dailyStatusList,
  onCollapse,
  onDateSelect,
  selectedDate,
}: Props) => {
  const start = startOfMonth(currentDate);
  const end = endOfMonth(currentDate);
  const days = eachDayOfInterval({ start, end });
  const firstDayIndex = getDay(start);

  const handlePrevMonth = () => onDateChange(subMonths(currentDate, 1));
  const handleNextMonth = () => onDateChange(addMonths(currentDate, 1));

  const statusMap = Object.fromEntries(dailyStatusList.map((d) => [d.date, d]));

  const getIconByStatus = (status?: 'GOOD' | 'NOT_BAD' | 'BAD') => {
    switch (status) {
      case 'GOOD':
        return <GoodIcon />;
      case 'NOT_BAD':
        return <NormalIcon />;
      case 'BAD':
        return <DangerousIcon />;
      default:
        return <BasicIcon />;
    }
  };

  return (
    <Wrapper>
      <Header>
        <IconButton onClick={handlePrevMonth}>
          <BeforeIcon />
        </IconButton>
        <MonthText>{format(currentDate, 'yyyy년 M월')}</MonthText>
        <IconButton onClick={handleNextMonth}>
          <AfterIcon />
        </IconButton>
      </Header>
      <Divider />
      <Weekdays>
        {['일', '월', '화', '수', '목', '금', '토'].map((d) => (
          <Weekday key={d}>{d}</Weekday>
        ))}
      </Weekdays>
      <Grid>
        {Array(firstDayIndex)
          .fill(null)
          .map((_, i) => (
            <Empty key={`empty-${i}`} />
          ))}
        {days.map((date) => {
          const dateStr = format(date, 'yyyy-MM-dd');
          const isSelected = dateStr === selectedDate;
          const isDateFuture = isFuture(date);

          const icon = getIconByStatus(
            isDateFuture ? undefined : statusMap[dateStr]?.status
          );
          const amount = statusMap[dateStr]?.totalExpense;
          const displayAmount =
            !isDateFuture && amount && amount > 0
              ? formatExpenseAmount(amount)
              : '-';

          return (
            <DayCell
              key={dateStr}
              onClick={() => !isDateFuture && onDateSelect?.(dateStr)}
              $isFuture={isDateFuture}
            >
              <div className="date">{format(date, 'd')}</div>
              <IconWrapper $isSelected={isSelected} $isFuture={isDateFuture}>
                {icon}
              </IconWrapper>
              <div className="amount">{displayAmount}</div>
            </DayCell>
          );
        })}
      </Grid>
      <HandleWrapper onClick={onCollapse}>
        <Handle />
      </HandleWrapper>
    </Wrapper>
  );
};

const Wrapper = styled.div`
  padding: 16px 16px 24px;
  max-width: 420px;
  margin: 0 auto;
  background-color: #fff;
  position: relative;
  z-index: 10;
  overflow: hidden;
  box-sizing: border-box;
`;

const Header = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 10px;
  padding: 8px 0;
`;

const IconButton = styled.button`
  background: none;
  border: none;
  padding: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;

  svg {
    width: 20px;
    height: 20px;
    fill: #808080;
  }
`;

const MonthText = styled.h2`
  font-size: 16px;
  font-weight: bold;
`;

const Divider = styled.div`
  width: calc(100% - 32px);
  height: 1px;
  background-color: #808080;
  margin: 10px auto 17px;
`;

const Weekdays = styled.div`
  width: calc(100% - 32px);
  margin: 0 auto 6px;
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  column-gap: 6px;
  font-size: 13px;
  color: #202632;
`;

const Weekday = styled.div`
  text-align: center;
  font-weight: 500;
`;

const Grid = styled.div`
  width: calc(100% - 32px);
  margin: 0 auto;
  display: grid;
  grid-template-columns: repeat(7, minmax(0, 1fr));
  grid-template-rows: repeat(6, 72px);
  row-gap: 10px;
  column-gap: 4px;
  box-sizing: border-box;
`;

const Empty = styled.div`
  height: 72px;
  min-width: 0;
`;

const DayCell = styled.div<{ $isFuture: boolean }>`
  height: 72px;
  min-width: 0;
  width: 100%;
  padding: 6px 0;
  background: transparent;
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  text-align: center;
  cursor: ${({ $isFuture }) => ($isFuture ? 'not-allowed' : 'pointer')};

  .date {
    width: 100%;
    font-size: 12px;
    font-weight: bold;
    margin-bottom: 4px;
  }

  .amount {
    width: 100%;
    font-size: 10px;
    font-weight: 500;
    color: #202632;
    margin-top: 1px;
  }
`;

const IconWrapper = styled.div.withConfig({
  shouldForwardProp: (prop) => !['$isSelected', '$isFuture'].includes(prop),
})<{ $isSelected?: boolean; $isFuture?: boolean }>`
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 36px;
  aspect-ratio: 1;
  border-radius: 50%;
  margin: 2px 0;
  cursor: inherit;
  flex-shrink: 0;

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

const HandleWrapper = styled.div`
  display: flex;
  justify-content: center;
  margin-top: 30px;
  margin-bottom: 5px;
`;

const Handle = styled.div`
  width: 40px;
  height: 3px;
  background-color: #aaa;
  border-radius: 999px;
  cursor: pointer;
`;

import styled from 'styled-components';
import { useNavigate } from 'react-router-dom';
import { format, parseISO } from 'date-fns';
import { ExpandableCalendar } from '@/features/calendar/ui/ExpandableCalendar';
import { GaugeChart } from '@/features/spendingStatus/ui/GaugeChart';
import { FullWidthDivider } from '@/shared/ui/Divider/FullWidthDivider';
import { ExpenseCard } from '@/features/spendingStatus/ui/ExpenseCard';
import { LoadingSpinner } from '@/shared/ui/LoadingSpinner/LoadingSpinner';
import {
  useDailyExpenses,
  ExpenseRecord,
} from '@/features/spendingStatus/api/useDailyExpenses';

interface Props {
  userId: number;
  nickname: string;
  selectedDate: string;
  setSelectedDate: (date: string) => void;
}

export const GeneralExpensePage = ({
  userId,
  nickname,
  selectedDate,
  setSelectedDate,
}: Props) => {
  const navigate = useNavigate();

  const { data: dailyData, isLoading: isLoadingDaily } = useDailyExpenses(
    userId,
    selectedDate
  );

  const records = dailyData?.expenses ?? [];
  const hasRecords = records.length > 0;

  const budget = dailyData?.savingGoalStatus?.budget ?? 0;
  const remaining = dailyData?.savingGoalStatus?.remainingBudget ?? 0;
  const spent = budget - remaining;

  if (isLoadingDaily) {
    return <LoadingSpinner message="지출 내역 불러오는 중..." />;
  }

  return (
    <Container>
      <TopBar>
        <BackButton onClick={() => navigate(-1)}>
          <img src="/icons/arrow-left.svg" alt="뒤로가기" />
        </BackButton>
      </TopBar>

      <ExpandableCalendar
        userId={userId}
        onDateSelect={setSelectedDate}
        selectedDate={selectedDate}
      />

      {budget > 0 ? (
        <>
          <GaugeChart total={budget} spent={spent} />
          <FullWidthDivider />
        </>
      ) : (
        <>
          <NoGoalBox>지출 목표 금액이 없습니다.</NoGoalBox>
          <FullWidthDivider />
        </>
      )}

      <CenteredTextBlock>
        <DateText>{format(parseISO(selectedDate), 'yyyy년 M월 d일')}</DateText>
        <TitleText>
          <span>{nickname}</span>님의 외식비 지출 내역 {records.length}건
        </TitleText>
      </CenteredTextBlock>

      {hasRecords ? (
        records.map((record: ExpenseRecord) => (
          <ExpenseCard key={record.id} {...record} />
        ))
      ) : (
        <NoDataText>지출 기록이 없습니다.</NoDataText>
      )}
    </Container>
  );
};

const Container = styled.div`
  background-color: #fff;
  min-height: 100vh;
  width: 100%;
  padding: 24px;
  padding-top: calc(env(safe-area-inset-top) + 16px);
  box-sizing: border-box;
`;

const TopBar = styled.div`
  display: flex;
  align-items: center;
  margin-bottom: 8px;
`;

const BackButton = styled.button`
  background: none;
  border: none;
  cursor: pointer;
  img {
    width: 20px;
    height: 20px;
  }
`;

const CenteredTextBlock = styled.div`
  text-align: center;
  margin-top: 32px;
`;

const DateText = styled.h2`
  font-size: 12px;
  font-weight: 700;
  margin-top: 32px;
  margin-bottom: 4px;
  color: #808080;
`;

const TitleText = styled.div`
  font-size: 14px;
  font-weight: 700;
  color: #202632;
  margin-bottom: 12px;

  span {
    color: #fd6918;
  }
`;

const NoDataText = styled.div`
  font-size: 13px;
  color: #808080;
  font-weight: 700;
  text-align: center;
  margin-top: 24px;
`;

const NoGoalBox = styled.div`
  margin: 16px 0;
  padding: 12px;
  background-color: #f8f8f8;
  border-left: 4px solid #ff6701;
  font-size: 14px;
  font-weight: 600;
  color: #444;
  text-align: center;
`;

import styled from 'styled-components';
import { useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { format, parseISO } from 'date-fns';
import { useAuthStore } from '@/features/auth/store/useAuthStore';
import { ExpandableCalendar } from '@/features/calendar/ui/ExpandableCalendar';
import { GaugeChart } from '@/features/spendingStatus/ui/GaugeChart';
import { FullWidthDivider } from '@/shared/ui/Divider/FullWidthDivider';
import { ExpenseCard } from '@/features/spendingStatus/ui/ExpenseCard';
import FileIcon from '@/assets/icons/file.svg?react';
import { useCheckSavingGoal } from '@/features/goals/hooks/useCheckSavingGoal';
import {
  useDailyExpenses,
  ExpenseRecord,
} from '@/features/spendingStatus/api/useDailyExpenses';
import { useRemainingBudgetByDate } from '@/features/goals/api/useRemainingBudgetByDate';
import { LoadingSpinner } from '@/shared/ui/LoadingSpinner/LoadingSpinner';

const MainPage = () => {
  useCheckSavingGoal();
  const userId = useAuthStore((s) => s.userId);
  const nickname = useAuthStore((s) => s.nickname ?? '한끼모아');
  const navigate = useNavigate();
  const location = useLocation();
  const [selectedDate, setSelectedDate] = useState(
    location.state?.date ?? format(new Date(), 'yyyy-MM-dd')
  );

  const { data: dailyData } = useDailyExpenses(userId, selectedDate);
  const { isError: isGoalMissing, isLoading: isGoalLoading } =
    useRemainingBudgetByDate(selectedDate);

  const records = dailyData?.expenses ?? [];
  const hasRecords = records.length > 0;

  const budget = dailyData?.savingGoalStatus?.budget ?? 0;
  const remaining = dailyData?.savingGoalStatus?.remainingBudget ?? 0;
  const spent = budget - remaining;

  if (isGoalLoading) {
    return <LoadingSpinner message="지출 목표 확인 중..." />;
  }

  return (
    <Container>
      <ExpandableCalendar
        userId={userId!}
        onDateSelect={setSelectedDate}
        selectedDate={selectedDate}
      />

      {!isGoalMissing ? (
        <>
          <GaugeChart total={budget} spent={spent} />
          <FullWidthDivider />
        </>
      ) : (
        <>
          <NoGoalBox>선택한 날짜에는 지출 목표 금액이 없어요!</NoGoalBox>
          <FullWidthDivider />
        </>
      )}

      <CenteredTextBlock>
        <DateText>{format(parseISO(selectedDate), 'yyyy년 M월 d일')}</DateText>
        <TitleText>
          {nickname}님의 외식비 지출 내역 {records.length}건
        </TitleText>

        {!hasRecords && (
          <>
            <NoDataBlock>
              <FileIconWrapper>
                <FileIcon />
              </FileIconWrapper>
              <NoDataText>아직 지출 기록이 없어요</NoDataText>
            </NoDataBlock>
            <AddButton
              onClick={() =>
                navigate('/record', { state: { date: selectedDate } })
              }
              disabled={isGoalMissing}
            >
              + 기록하기
            </AddButton>
          </>
        )}
      </CenteredTextBlock>

      {hasRecords && (
        <>
          {records.map((record: ExpenseRecord) => (
            <ExpenseCard key={record.id} {...record} />
          ))}
          <AddButton
            onClick={() =>
              navigate('/record', { state: { date: selectedDate } })
            }
            disabled={isGoalMissing}
          >
            + 기록하기
          </AddButton>
        </>
      )}
    </Container>
  );
};

export default MainPage;

const Container = styled.div`
  background-color: #fff;
  min-height: 100vh;
  width: 100%;
  padding: 24px;
  padding-top: var(--safe-area-top);
  box-sizing: border-box;
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
`;

const NoDataBlock = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-top: 24px;
  gap: 8px;
`;

const FileIconWrapper = styled.div`
  margin-top: 12px;
  svg {
    width: 50px;
    height: 50px;
    opacity: 0.4;
  }
`;

const NoDataText = styled.div`
  font-size: 13px;
  color: #808080;
  font-weight: 700;
`;

const AddButton = styled.button<{ disabled?: boolean }>`
  display: block;
  margin: 32px auto 24px;
  padding: 10px 20px;
  background-color: ${({ disabled }) => (disabled ? '#ccc' : '#fd6918')};
  color: #fff;
  font-weight: 600;
  font-size: 12px;
  border: none;
  border-radius: 999px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.12);
  cursor: ${({ disabled }) => (disabled ? 'not-allowed' : 'pointer')};
  transition:
    background-color 0.2s ease,
    transform 0.1s ease;

  &:hover {
    background-color: ${({ disabled }) => (disabled ? '#ccc' : '#e85c0e')};
    transform: ${({ disabled }) => (disabled ? 'none' : 'translateY(-1px)')};
  }

  &:active {
    transform: ${({ disabled }) => (disabled ? 'none' : 'scale(0.98)')};
  }
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

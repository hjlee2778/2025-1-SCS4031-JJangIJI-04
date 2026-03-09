import { useState } from 'react';
import styled from 'styled-components';
import { useAddSavingGoal } from '@/features/goals/mutations/useAddSavingGoal';
import { InputField } from '@/shared/ui/InputField';
import { format, endOfWeek, startOfWeek } from 'date-fns';

const WeeklyGoalPage = () => {
  const today = new Date();
  const startOfThisWeek = startOfWeek(today, { weekStartsOn: 0 }); 
  const endOfThisWeek = endOfWeek(today, { weekStartsOn: 0 });
  
  const startDate = format(startOfThisWeek, 'yyyy-MM-dd');
  const endDate = format(endOfThisWeek, 'yyyy-MM-dd');
  
  const [budget, setBudget] = useState('');
  const { mutate: addGoal } = useAddSavingGoal();

  const isFormValid = budget !== '' && Number(budget.replace(/,/g, '')) > 0;

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const raw = e.target.value.replace(/,/g, '');
    if (raw === '') {
      setBudget('');
      return;
    }
    if (!isNaN(Number(raw))) {
      const formatted = Number(raw).toLocaleString();
      setBudget(formatted);
    }
  };

  const handleSubmit = () => {
    const numericValue = Number(budget.replace(/,/g, ''));

    if (!budget || isNaN(numericValue) || numericValue <= 0) {
      alert('금액을 올바르게 입력해주세요.');
      return;
    }

    addGoal({
      budget: numericValue,
      startDate,
      endDate,
    });
  };

  // 남은 일수 계산
  const daysLeft = Math.ceil((endOfThisWeek.getTime() - today.getTime()) / (1000 * 60 * 60 * 24));

  return (
    <Container>
      <ContentWrapper>
        <Title>{`아직 이번주 외식비 절약 목표를\n설정하지 않으셨네요`}</Title>
        <SubTitle>이번 주 토요일까지 ({daysLeft}일 동안) 절약하고자 하는 목표 외식비 금액을 작성해주세요!</SubTitle>
        <InputField
          placeholder="금액을 입력해주세요"
          value={budget}
          onChange={handleChange}
          maxLength={20}
        />
        <WarningText>! 목표 금액은 수정할 수 없으므로 신중하게 작성해주세요</WarningText>
      </ContentWrapper>
      <SubmitButton onClick={handleSubmit} disabled={!isFormValid}>설정하기</SubmitButton>
    </Container>
  );
};

export default WeeklyGoalPage;

const Container = styled.div`
  padding: 24px;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
`;

const ContentWrapper = styled.div`
  display: flex;
  flex-direction: column;
`;

const Title = styled.h2`
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 20px;
  white-space: pre-line;
`;

const SubTitle = styled.p`
  font-size: 11px;
  font-weight: 600;
  color: #808080;
  margin-top: -12px;
  margin-bottom: 24px;
`;

const SubmitButton = styled.button<{ disabled?: boolean }>`
  width: 100%;
  padding: 14px;
  font-weight: bold;
  font-size: 16px;
  border: none;
  border-radius: 8px;
  margin-bottom: 24px;
  transition: transform 0.2s ease, box-shadow 0.2s ease;
  cursor: ${({ disabled }) => (disabled ? 'not-allowed' : 'pointer')};
  background-color: ${({ disabled }) => (disabled ? '#ccc' : '#FF6701')};
  color: #fff;

  &:hover {
    ${({ disabled }) =>
      !disabled &&
      `
      transform: scale(1.02);
      box-shadow: 0 6px 12px rgba(255, 122, 1, 0.3);
    `}
  }

  &:active {
    ${({ disabled }) =>
      !disabled &&
      `
      transform: scale(0.98);
      box-shadow: 0 2px 6px rgba(255, 122, 1, 0.2);
    `}
  }
`;

const WarningText = styled.p`
  font-size: 11px;
  color: #FF4B4B;
  margin-top: 8px;
  font-weight: 800;
`;

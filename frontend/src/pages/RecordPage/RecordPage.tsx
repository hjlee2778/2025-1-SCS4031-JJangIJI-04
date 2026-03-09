import { useState } from 'react';
import styled from 'styled-components';
import { InputField } from '@/shared/ui/InputField';
import { FullWidthDivider } from '@/shared/ui/Divider/FullWidthDivider';
import { useNavigate, useLocation } from 'react-router-dom';
import NavigateBeforeIcon from '@/assets/icons/navigate-before.svg?react';
import { StarRating } from '@/features/record/ui/StarRating';
import { useRemainingBudget } from '@/features/goals/api/useRemainingBudget';
import { useAddExpense } from '@/features/record/mutations/useAddExpense';
import { format } from 'date-fns';
import DatabaseIcon from '@/assets/icons/database.svg?react';
import ArrowIcon from '@/assets/icons/circle-point.svg?react';
import { RestaurantSearchInput } from '@/features/record/ui/RestaurantSearchInput';
import { Restaurant } from '@/features/restaurant/types/restaurant';

const RecordPage = () => {
  const [menuName, setMenuName] = useState('');
  const [restaurantName, setRestaurantName] = useState('');
  const [selectedRestaurant, setSelectedRestaurant] =
    useState<Restaurant | null>(null);
  const [amount, setAmount] = useState('');
  const [rating, setRating] = useState(0);
  const [memo, setMemo] = useState('');
  const navigate = useNavigate();
  const { mutate: addExpense } = useAddExpense();
  const location = useLocation();
  const selectedDateFromCalendar =
    (location.state?.date as string) ?? format(new Date(), 'yyyy-MM-dd');
  const { data: budgetData } = useRemainingBudget(selectedDateFromCalendar);

  const handleAmountChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const raw = e.target.value.replace(/,/g, '');
    if (raw === '') return setAmount('');
    if (!isNaN(Number(raw))) {
      const formatted = Number(raw).toLocaleString();
      setAmount(formatted);
    }
  };

  const numericAmount = Number(amount.replace(/,/g, '') || 0);
  const remainingAfterExpense =
    (budgetData?.remainingBudget ?? 0) - numericAmount;

  const isFormValid =
    restaurantName.trim() !== '' &&
    menuName.trim() !== '' &&
    amount.trim() !== '' &&
    !isNaN(numericAmount) &&
    numericAmount > 0 &&
    rating > 0;

  const handleRestaurantChange = (name: string, restaurant?: Restaurant) => {
    setRestaurantName(name);
    setSelectedRestaurant(restaurant || null);
  };

  const handleSubmit = () => {
    const payload = {
      restaurantId: selectedRestaurant?.id
        ? Number(selectedRestaurant.id)
        : undefined,
      restaurantName,
      menuName,
      expense: numericAmount,
      memo,
      expenseDate: selectedDateFromCalendar,
      rating,
    };

    addExpense(payload, {
      onSuccess: () => {
        alert('지출내역이 성공적으로 저장되었어요!');
        navigate('/main', { state: { date: selectedDateFromCalendar } });
      },
      onError: () => {
        alert('저장 중 오류가 발생했습니다.');
      },
    });
  };

  return (
    <Container>
      <BackButtonWrapper>
        <BackButton onClick={() => navigate('/main')}>
          <NavigateBeforeIcon />
        </BackButton>
      </BackButtonWrapper>

      <LabeledInputWrapper>
        <Label>
          어떤 식당을 방문하셨나요? <Asterisk>*</Asterisk>
        </Label>
        <RestaurantSearchInput
          value={restaurantName}
          onChange={handleRestaurantChange}
        />
      </LabeledInputWrapper>

      <LabeledInputWrapper>
        <Label>
          어떤 메뉴를 드셨나요? <Asterisk>*</Asterisk>
        </Label>
        <InputField
          placeholder="메뉴명을 입력해주세요"
          value={menuName}
          onChange={(e) => setMenuName(e.target.value)}
          maxLength={30}
        />
      </LabeledInputWrapper>

      <LabeledInputWrapper>
        <Label>
          식당의 만족도를 평가해주세요 <Asterisk>*</Asterisk>
        </Label>
        <StarRating value={rating} onChange={setRating} />
      </LabeledInputWrapper>

      <FullWidthDivider />

      <LabeledInputWrapper>
        <Label>
          외식비 총액을 입력해주세요 <Asterisk>*</Asterisk>
        </Label>
        <InputField
          placeholder="금액을 입력해주세요"
          value={amount}
          onChange={handleAmountChange}
          maxLength={15}
        />
      </LabeledInputWrapper>

      {amount && (
        <BudgetResultSection>
          <NoticeBox>
            <StyledIcon />
            이번주 가용 금액이 다음과 같이 남게 돼요
          </NoticeBox>
          <BudgetSummary>
            <BudgetBox>
              <BudgetLabel>현재 가용 금액</BudgetLabel>
              <BudgetValue>
                {(budgetData?.remainingBudget ?? 0).toLocaleString()}원
              </BudgetValue>
            </BudgetBox>
            <StyledArrow />
            <BudgetBox>
              <BudgetLabel>남은 가용 금액</BudgetLabel>
              <BudgetValue $negative={remainingAfterExpense < 0}>
                {remainingAfterExpense.toLocaleString()}원
              </BudgetValue>
            </BudgetBox>
          </BudgetSummary>
        </BudgetResultSection>
      )}

      <FullWidthDivider />

      <LabeledInputWrapper>
        <LabelWrapper>
          <Label>한 줄 메모를 남겨주세요</Label>
          <CharCount>{memo.length} / 100</CharCount>
        </LabelWrapper>
        <MemoTextarea
          placeholder="식사에 대한 평, 음식에 대한 팁 등 자유롭게 적어주세요!"
          value={memo}
          onChange={(e) => setMemo(e.target.value)}
          maxLength={100}
        />
      </LabeledInputWrapper>

      <SubmitButton disabled={!isFormValid} onClick={handleSubmit}>
        입력 완료
      </SubmitButton>
    </Container>
  );
};

export default RecordPage;

const Container = styled.div`
  padding: 24px;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
`;

const BackButtonWrapper = styled.div`
  display: flex;
  justify-content: flex-start;
  margin: -8px 0 16px -8px;
`;

const BackButton = styled.button`
  background: none;
  border: none;
  padding: 4px;
  display: flex;
  align-items: center;
  cursor: pointer;
  svg {
    width: 24px;
    height: 24px;
  }
`;

const LabeledInputWrapper = styled.div`
  display: flex;
  flex-direction: column;
  gap: 4px;
  margin-bottom: 24px;
`;

const Label = styled.label`
  font-size: 14px;
  font-weight: 500;
`;

const Asterisk = styled.span`
  color: red;
  margin-left: 2px;
`;

const BudgetResultSection = styled.div`
  margin-top: 10px;
  margin-bottom: 32px;
`;

const NoticeBox = styled.div`
  background-color: #ff6701;
  color: #fff;
  font-weight: 700;
  padding: 12px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  justify-content: center;
  margin-bottom: 16px;
`;

const StyledIcon = styled(DatabaseIcon)`
  width: 18px;
  height: 18px;
  flex-shrink: 0;
`;

const BudgetSummary = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 24px;
`;

const BudgetBox = styled.div`
  width: 130px;
  height: 72px;
  background-color: #fff;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.08);
  border-radius: 12px;
  padding: 12px 16px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  border: 1px solid #e0e0e0;
`;

const BudgetLabel = styled.div`
  font-size: 12px;
  color: #666;
  margin-bottom: 4px;
`;

const BudgetValue = styled.div<{ $negative?: boolean }>`
  font-size: 18px;
  font-weight: bold;
  color: ${({ $negative }) => ($negative ? '#E74C3C' : '#333')};
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
`;

const StyledArrow = styled(ArrowIcon)`
  width: 20px;
  height: 20px;
  color: #999;
`;

const MemoTextarea = styled.textarea`
  width: 100%;
  height: 120px;
  margin-top: 10px;
  padding: 10px 12px;
  font-size: 14px;
  border: 1px solid #ccc;
  border-radius: 8px;
  resize: none;
  outline: none;
  font-family: inherit;
  line-height: 1.4;
`;

const LabelWrapper = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
`;

const CharCount = styled.span`
  font-size: 12px;
  color: #999;
`;

const SubmitButton = styled.button<{ disabled?: boolean }>`
  width: 100%;
  height: 40px;
  background-color: ${({ disabled }) => (disabled ? '#ccc' : '#FF6701')};
  color: white;
  font-size: 16px;
  font-weight: bold;
  padding: 16px 0;
  border: none;
  border-radius: 12px;
  cursor: ${({ disabled }) => (disabled ? 'not-allowed' : 'pointer')};
  transition: background-color 0.2s;
  display: flex;
  align-items: center;
  justify-content: center;
  &:hover {
    background-color: ${({ disabled }) => (disabled ? '#ccc' : '#e55c00')};
  }
`;

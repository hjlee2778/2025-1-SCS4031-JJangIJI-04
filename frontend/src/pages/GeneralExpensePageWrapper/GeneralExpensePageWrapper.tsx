import { useParams, useLocation, useNavigate } from 'react-router-dom';
import { useEffect, useState } from 'react';
import { format } from 'date-fns';
import { GeneralExpensePage } from '@/features/spendingStatus/pages/GeneralExpensePage';

const GeneralExpensePageWrapper = () => {
  const { userId } = useParams();
  const location = useLocation();
  const navigate = useNavigate();

  const nickname = location.state?.nickname ?? '유저';
  const [selectedDate, setSelectedDate] = useState(
    location.state?.date ?? format(new Date(), 'yyyy-MM-dd')
  );

  useEffect(() => {
    if (!userId || isNaN(Number(userId))) {
      navigate('/');
      return;
    }
  }, [userId, navigate]);

  return (
    <GeneralExpensePage
      userId={Number(userId)}
      nickname={nickname}
      selectedDate={selectedDate}
      setSelectedDate={setSelectedDate}
    />
  );
};

export default GeneralExpensePageWrapper;

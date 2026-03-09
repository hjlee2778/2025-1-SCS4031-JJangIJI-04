import { motion, AnimatePresence } from 'framer-motion';
import { useState, useEffect } from 'react';
import styled from 'styled-components';
import { MiniCalendar } from '@/features/calendar/ui/MiniCalendar';
import { FullCalendar } from '@/features/calendar/ui/FullCalendar';
import { useMonthlyExpenseStatus } from '@/features/calendar/api/useMonthlyExpenseStatus';
import { parseISO } from 'date-fns';

const BOTTOM_SAFE_AREA = 72;
const MAX_CALENDAR_HEIGHT = `calc(100dvh - ${BOTTOM_SAFE_AREA}px)`;

export const ExpandableCalendar = ({
  userId,
  onDateSelect,
  selectedDate,
}: {
  userId: number;
  onDateSelect?: (date: string) => void;
  selectedDate?: string;
}) => {
  const [isExpanded, setIsExpanded] = useState(false);
  const [currentDate, setCurrentDate] = useState(new Date());

  useEffect(() => {
    if (selectedDate) {
      setCurrentDate(parseISO(selectedDate));
    }
  }, [selectedDate]);

  const { data: dailyStatusList = [] } = useMonthlyExpenseStatus(
    userId,
    currentDate
  );

  const handleExpand = () => {
    if (isExpanded && selectedDate) {
      setCurrentDate(parseISO(selectedDate));
    }
    setIsExpanded((prev) => !prev);
  };

  useEffect(() => {
    const mainElement = document.querySelector('main');
    if (isExpanded && mainElement) {
      mainElement.style.overflow = 'hidden';
    } else if (mainElement) {
      mainElement.style.overflow = 'auto';
    }
    return () => {
      if (mainElement) mainElement.style.overflow = 'auto';
    };
  }, [isExpanded]);

  return (
    <Container>
      <motion.div
        animate={{ height: isExpanded ? 'auto' : 150 }}
        initial={false}
        transition={{ duration: 0.5, ease: [0.4, 0, 0.2, 1] }}
        style={{
          overflow: 'hidden',
          maxHeight:
            isExpanded && window.innerWidth < 768
              ? MAX_CALENDAR_HEIGHT
              : '640px',
        }}
      >
        <Wrapper $isMini={!isExpanded}>
          {!isExpanded ? (
            <MiniCalendar
              dailyStatusList={dailyStatusList}
              onExpand={handleExpand}
              onDateSelect={onDateSelect}
              selectedDate={selectedDate}
            />
          ) : (
            <FullCalendar
              currentDate={currentDate}
              onDateChange={setCurrentDate}
              dailyStatusList={dailyStatusList}
              onCollapse={handleExpand}
              onDateSelect={onDateSelect}
              selectedDate={selectedDate}
            />
          )}
        </Wrapper>
      </motion.div>

      <AnimatePresence>
        {isExpanded && (
          <motion.div
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            exit={{ opacity: 0 }}
            transition={{ duration: 0.5 }}
          >
            <ModalBackground onClick={handleExpand} />
          </motion.div>
        )}
      </AnimatePresence>
    </Container>
  );
};

const Container = styled.div`
  width: 100vw;
  position: relative;
  left: 50%;
  right: 50%;
  margin-left: -50vw;
  margin-right: -50vw;
`;

const ModalBackground = styled.div`
  z-index: 1;

  @media (max-width: 767px) {
    position: fixed;
    top: ${MAX_CALENDAR_HEIGHT};
    left: 0;
    right: 0;
    bottom: 0;
    background-color: rgba(0, 0, 0, 0.3);
    backdrop-filter: blur(1.5px);
    -webkit-backdrop-filter: blur(1.5px);
    pointer-events: none;
    overscroll-behavior: none;
  }

  @media (min-width: 768px) {
    position: absolute;
    top: 420px;
    left: 0;
    right: 0;
    bottom: -100vh;
    background-color: rgba(0, 0, 0, 0.3);
    backdrop-filter: blur(1px);
    -webkit-backdrop-filter: blur(1px);
  }
`;

const Wrapper = styled.div<{ $isMini: boolean }>`
  position: relative;
  background-color: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
  z-index: 2;
  padding-bottom: ${({ $isMini }) => ($isMini ? '0px' : '30px')};
  overflow-y: auto;
  max-height: ${MAX_CALENDAR_HEIGHT};
  -webkit-overflow-scrolling: touch;

  @media (min-width: 768px) {
    max-height: 640px;
    overflow-y: visible;
  }
`;

import styled from 'styled-components';
import { useEffect, useRef, useState } from 'react';
import { EmojiPopover } from '@/features/community/ui/EmojiPopover';

interface EmojiAddButtonProps {
  onSelect: (emojiKey: number) => void;
}

export const EmojiAddButton = ({ onSelect }: EmojiAddButtonProps) => {
  const [open, setOpen] = useState(false);
  const ref = useRef<HTMLDivElement>(null);

  const handleSelect = (key: number) => {
    onSelect(key);
    setOpen(false);
  };

  useEffect(() => {
    const handleClickOutside = (e: MouseEvent) => {
      if (ref.current && !ref.current.contains(e.target as Node)) {
        setOpen(false);
      }
    };
    if (open) {
      document.addEventListener('mousedown', handleClickOutside);
    }
    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, [open]);

  return (
    <Container ref={ref}>
      <Button onClick={() => setOpen((prev) => !prev)}>
        <img src="/icons/community/emojis/addEmoji.svg" alt="Add emoji" />
      </Button>
      {open && <EmojiPopover onSelect={handleSelect} />}
    </Container>
  );
};

const Container = styled.div`
  position: relative;
`;

const Button = styled.button`
  display: flex;
  align-items: center;
  gap: 2px;
  border: 0.5px solid #808080;
  border-radius: 16px;
  padding: 1px 5px;
  font-size: 12px;
  background-color: #fff;
  cursor: pointer;

  img {
    width: 14px;
    height: 14px;
  }
`;

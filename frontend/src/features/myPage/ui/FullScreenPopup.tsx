import { useEffect, useState } from 'react';
import { createPortal } from 'react-dom';
import styled from 'styled-components';
import { CategorySelector } from '@/features/preferences/ui/CategorySelector';
import { InputField } from '@/shared/ui/InputField';
import { FullWidthDivider } from '@/shared/ui/Divider/FullWidthDivider';
import { useUpdateNickname } from '@/features/auth/mutations/useUpdateNickname';
import { useUpdateCategories } from '@/features/preferences/mutations/useUpdateCategories';
import { useUserInfo } from '@/features/auth/api/useUserInfo';
import { useNavigate } from 'react-router-dom';
import { useLogout } from '@/features/auth/mutations/useLogout';

interface FullScreenPopupProps {
  visible: boolean;
  onClose: () => void;
  type: 'nickname' | 'category' | 'settings';
}

const settingData = [
  { label: '공지사항', link: '/' },
  { label: '고객센터', link: '/' },
  {
    label: '라이선스 정보',
    link: 'https://disco-wallflower-918.notion.site/OpenMoji-206b0d86bbbb8033b64cd5d110d225fa?source=copy_link',
  },
];

export const FullScreenPopup = ({
  visible,
  onClose,
  type,
}: FullScreenPopupProps) => {
  const [nickname, setNickname] = useState('');
  const [nicknameError, setNicknameError] = useState('');
  const isNicknameValid = nicknameError === '' && nickname.trim() !== '';
  const { mutate: updateNickname, isPending } = useUpdateNickname();
  const { data: userInfo } = useUserInfo();
  const [categories, setCategories] = useState<number[]>(
    userInfo?.categories.map((c) => c.categoryId) ?? []
  );
  const { mutate: updateCategories, isPending: isUpdating } = useUpdateCategories();
  const navigate = useNavigate();
  const { mutate: logout } = useLogout();

  useEffect(() => {
    if (visible) document.body.style.overflow = 'hidden';
    else document.body.style.overflow = '';
    return () => {
      document.body.style.overflow = '';
    };
  }, [visible]);

  if (!visible) return null;

  const handleNicknameChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value;
    setNickname(value);

    if (value.trim() === '') {
      setNicknameError('닉네임을 입력해주세요.');
    } else if (/\s/.test(value)) {
      setNicknameError('공백은 사용할 수 없습니다.');
    } else if ([...value].length > 10) {
      setNicknameError('10자 이하로 입력해주세요.');
    } else {
      setNicknameError('');
    }
  };

  return createPortal(
    <Overlay>
      <Popup>
        <Header>
          <BackButton onClick={onClose}>
            <img src="/icons/arrow-left.svg" alt="back" />
          </BackButton>
          {type === 'settings' && <HeaderTitle>설정</HeaderTitle>}
        </Header>

        <Body>
          {type === 'nickname' && (
            <>
              <NicknameForm
                value={nickname}
                onChange={handleNicknameChange}
                error={nicknameError}
              />
              <BottomButton
                disabled={!isNicknameValid || isPending}
                onClick={() => {
                  updateNickname(nickname, {
                    onSuccess: () => {
                      onClose();
                    },
                    onError: () => {
                      alert('닉네임 변경에 실패했어요. 다시 시도해주세요.');
                    },
                  });
                }}
              >
                변경하기
              </BottomButton>
            </>
          )}

          {type === 'category' && (
            <>
              <Title>선호하는 음식 카테고리를 선택해 주세요</Title>
              <CategorySelector selected={categories} onChange={setCategories} />
              <BottomButton
                disabled={categories.length === 0 || isUpdating}
                onClick={() => {
                  updateCategories(categories, {
                    onSuccess: () => {
                      onClose();
                    },
                    onError: () => {
                      alert('카테고리 변경에 실패했어요. 다시 시도해주세요.');
                    },
                  });
                }}
              >
                변경하기
              </BottomButton>
            </>
          )}

          {type === 'settings' && <SettingsList onLogout={() => {
            logout(undefined, {
              onSuccess: () => navigate('/'),
              onError: () => alert('로그아웃에 실패했어요. 다시 시도해주세요.'),
            });
          }} />}
        </Body>
      </Popup>
    </Overlay>,
    document.body
  );
};

const NicknameForm = ({
  value,
  onChange,
  error,
}: {
  value: string;
  onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
  error: string;
}) => (
  <NicknameFormContainer>
    <Instruction>변경하실 닉네임을 입력해주세요</Instruction>
    <InputField
      value={value}
      onChange={onChange}
      placeholder="최대 10자까지 가능하며, 공백은 허용되지 않습니다"
      maxLength={10}
      error={error}
      isValid={error === ''}
    />
  </NicknameFormContainer>
);

const SettingsList = ({ onLogout }: { onLogout: () => void }) => (
  <SettingsContainer>
    {settingData.map((item) => (
      <a
        key={item.label}
        href={item.link}
        target="_blank"
        rel="noopener noreferrer"
        style={{ textDecoration: 'none', color: 'inherit' }}
      >
        <SettingItem>
          <span>{item.label}</span>
          <img src="/icons/arrow-right.svg" alt="arrow" />
        </SettingItem>
      </a>
    ))}
    <FullWidthDivider />
    <SettingItem className="logout" onClick={onLogout}>
      로그아웃
    </SettingItem>
  </SettingsContainer>
);

const Overlay = styled.div`
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 9999;
  background-color: white;
  display: flex;
  justify-content: center;
  align-items: flex-start;
  padding-top: env(safe-area-inset-top, 10px);
`;

const Popup = styled.div`
  width: 100%;
  max-width: 480px;
  height: 100%;
  background-color: #fff;
  position: relative;
  padding: 12px;
  display: flex;
  flex-direction: column;
`;

const Header = styled.div`
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  height: 56px;
  padding-top: 12px;
`;

const BackButton = styled.button`
  position: absolute;
  top: 50%;
  left: 0;
  transform: translateY(-50%);
  background: none;
  border: none;
  cursor: pointer;
  padding-top: 12px;
`;

const HeaderTitle = styled.h1`
  font-size: 16px;
  font-weight: bold;
  margin: 0;
`;

const Title = styled.h2`
  font-size: 18px;
  font-weight: 600;
`;

const Body = styled.div`
  flex: 1;
  display: flex;
  flex-direction: column;
  padding: 24px;
  padding-top: 60px;
  min-height: 100vh;
  box-sizing: border-box;
`;

const BottomButton = styled.button<{ disabled?: boolean }>`
  margin-top: auto;
  margin-bottom: 24px;
  width: 100%;
  padding: 14px;
  font-weight: bold;
  font-size: 16px;
  border: none;
  border-radius: 8px;
  background-color: ${({ disabled }) => (disabled ? '#ccc' : '#ff6701')};
  color: #fff;
  cursor: ${({ disabled }) => (disabled ? 'not-allowed' : 'pointer')};
`;

const NicknameFormContainer = styled.div`
  display: flex;
  flex-direction: column;
  gap: 16px;
`;

const Instruction = styled.div`
  font-size: 16px;
  font-weight: 600;
`;

const SettingsContainer = styled.div`
  display: flex;
  flex-direction: column;
  gap: 24px;
  margin-top: 16px;

  .logout {
    color: #e57373;
    margin-top: 24px;
  }

  .withdraw {
    color: #e57373;
  }
`;

const SettingItem = styled.div`
  font-size: 16px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  cursor: pointer;
`;

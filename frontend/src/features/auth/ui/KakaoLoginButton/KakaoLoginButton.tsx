import styled from 'styled-components';

export const KakaoLoginButton = () => {
  const REST_API_KEY = import.meta.env.VITE_KAKAO_REST_API_KEY;
  const REDIRECT_URI = import.meta.env.VITE_KAKAO_REDIRECT_URI;
  const REDIRECT_URI_WWW = import.meta.env.VITE_KAKAO_REDIRECT_URI_WWW;

  const KAKAO_AUTH_URL = `https://kauth.kakao.com/oauth/authorize?client_id=${REST_API_KEY}&redirect_uri=${window.location.hostname.startsWith('www.') ? REDIRECT_URI_WWW : REDIRECT_URI}&response_type=code`;

  const handleLogin = () => {

    //로컬에서는 모킹된 콜백으로 이동
    if (import.meta.env.DEV || import.meta.env.VITE_ENABLE_MSW === 'true') {
      window.location.href = '/oauth/callback/kakao?code=mock-kakao-code';
      return;
    }
    // 프로덕션에서는 실제 카카오 로그인 페이지로 이동
    window.location.href = KAKAO_AUTH_URL;
  };

  return (
    <ButtonWrapper onClick={handleLogin}>
      <ButtonImage
        src="/icons/kakao-login-button.svg"
        alt="카카오 계정으로 로그인"
      />
    </ButtonWrapper>
  );
};

const ButtonWrapper = styled.button`
  width: 100%;
  max-width: 300px;
  border: none;
  padding: 0;
  background: none;
  cursor: pointer;

  &:hover {
    opacity: 0.9;
  }

  &:active {
    transform: scale(0.98);
  }
`;

const ButtonImage = styled.img`
  width: 100%;
  height: auto;
  display: block;
`;

import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import { GlobalStyle } from '@/app/styles/global';
import { App } from '@/app/App';

async function enableMocking() {
  // DEV 모드거나 VITE_ENABLE_MSW 환경변수가 true인 경우 모킹 활성화
  if (import.meta.env.DEV || import.meta.env.VITE_ENABLE_MSW === 'true') {
    const { worker } = await import('./mocks/browser');
    await worker.start({ onUnhandledRequest: 'bypass' });
  }
}

enableMocking().then(() => {
  const root = createRoot(document.getElementById('root')!);
  root.render(
    <StrictMode>
      <GlobalStyle />
      <App />
    </StrictMode>
  );
});

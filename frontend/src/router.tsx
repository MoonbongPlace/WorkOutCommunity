import { createBrowserRouter } from 'react-router-dom'
import RootLayout from './layouts/RootLayout'
import ProtectedRoute from './components/ProtectedRoute'
import LandingPage from './pages/LandingPage'
import LoginPage from './pages/LoginPage'
import SignupPage from './pages/SignupPage'
import PostsPage from './pages/PostsPage'
import PostDetailPage from './pages/PostDetailPage'
import PostNewPage from './pages/PostNewPage'
import PostEditPage from './pages/PostEditPage'
import MePage from './pages/MePage'
import WorkoutLogsPage from './pages/WorkoutLogsPage'
import WorkoutLogDetailPage from './pages/WorkoutLogDetailPage'
import WorkoutLogNewPage from './pages/WorkoutLogNewPage'
import NotificationsPage from './pages/NotificationsPage'
import ChatPage from './pages/ChatPage'

const router = createBrowserRouter([
  // 공개 라우트
  { path: '/',       element: <LandingPage /> },
  { path: '/login',  element: <LoginPage /> },
  { path: '/signup', element: <SignupPage /> },

  // 인증 필요 라우트
  {
    element: <ProtectedRoute />,
    children: [
      {
        element: <RootLayout />,
        children: [
          { path: '/posts',                  element: <PostsPage /> },
          { path: '/posts/new',              element: <PostNewPage /> },
          { path: '/posts/:postId',          element: <PostDetailPage /> },
          { path: '/posts/:postId/edit',     element: <PostEditPage /> },
          { path: '/me',                     element: <MePage /> },
          { path: '/workout-logs',           element: <WorkoutLogsPage /> },
          { path: '/workout-logs/new',       element: <WorkoutLogNewPage /> },
          { path: '/workout-logs/:logId',    element: <WorkoutLogDetailPage /> },
          { path: '/notifications',          element: <NotificationsPage /> },
          { path: '/chat',                   element: <ChatPage /> },
        ],
      },
    ],
  },
])

export default router

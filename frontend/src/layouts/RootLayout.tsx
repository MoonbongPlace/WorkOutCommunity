import { Outlet } from 'react-router-dom'
import Navbar from '../components/nav/Navbar'
import AiChatWidget from '../components/ui/AiChatWidget'

export default function RootLayout() {
  return (
    <div className="min-h-full flex flex-col">
      <Navbar />
      <main className="flex-1 max-w-5xl w-full mx-auto px-4 py-8">
        <Outlet />
      </main>
      <AiChatWidget />
    </div>
  )
}

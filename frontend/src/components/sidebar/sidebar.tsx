"use client"
import { BookOpen, List, Plus, LogOut, LogIn } from "lucide-react"
import "../../assets/css/sidebar/sidebar.css"

interface SidebarProps {
  isLoggedIn: boolean
  onLogin: () => void
  onLogout: () => void
  onViewChange: (view: "list" | "add" | "edit") => void
  currentView: "list" | "add" | "edit"
}

export default function Sidebar({ isLoggedIn, onLogin, onLogout, onViewChange, currentView }: SidebarProps) {
  return (
    <aside className="sidebar">
      <div className="sidebar-header">
        <h2><BookOpen size={24} /> Quản Lý Sách</h2>
      </div>

      {isLoggedIn && (
        <nav className="sidebar-nav">
          <button className={`nav-btn ${currentView === "list" ? "active" : ""}`} onClick={() => onViewChange("list")}>
            <List size={18} /> Danh Sách Sách
          </button>
          <button className={`nav-btn ${currentView === "add" ? "active" : ""}`} onClick={() => onViewChange("add")}>
            <Plus size={18} /> Thêm Sách
          </button>
        </nav>
      )}

      <div className="sidebar-footer">
        {isLoggedIn ? (
          <button className="auth-btn logout-btn" onClick={onLogout}>
            <LogOut size={18} /> Đăng Xuất
          </button>
        ) : (
          <button className="auth-btn login-btn" onClick={onLogin}>
            <LogIn size={18} /> Đăng Nhập
          </button>
        )}
      </div>
    </aside>
  )
}

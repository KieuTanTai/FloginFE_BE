"use client";

import type React from "react";
import { useState } from "react";
import { LogIn, User, Lock, AlertCircle } from "lucide-react";
import "../../assets/css/auth/login-form.css";
import { accountService } from "../../services/accountService";
import type { Account } from "../../models/Account";

interface LoginFormProps {
  onLoginSuccess: (account: Account) => void;
  onCancel?: () => void;
}

export default function LoginForm({
  onLoginSuccess,
  onCancel,
}: LoginFormProps) {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);

    if (!username.trim() || !password.trim()) {
      setError("Vui lòng nhập đầy đủ thông tin");
      return;
    }

    setIsLoading(true);

    try {
      const account = await accountService.login({ username, password });
      onLoginSuccess(account);
    } catch (err: any) {
      setError(err.message || "Đăng nhập thất bại");
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="login-container">
      <div className="login-card">
        <div className="login-header">
          <div className="full-width flex-col">
            <h1>Đăng Nhập</h1>
            <div className="flex-vertical">
                <LogIn size={20} className="login-icon" />
                <p>Quản Lý Sách</p>
            </div>
          </div>
        </div>

        <form onSubmit={handleSubmit} className="login-form">
          {error && (
            <div className="error-message">
              <AlertCircle size={18} />
              <span>{error}</span>
            </div>
          )}

          <div className="form-group">
            <label htmlFor="username">
              <User size={18} />
              Tên đăng nhập
            </label>
            <input
              type="text"
              id="username"
              name="username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              placeholder="Nhập tên đăng nhập"
              disabled={isLoading}
              autoComplete="username"
              autoFocus
            />
          </div>

          <div className="form-group">
            <label htmlFor="password">
              <Lock size={18} />
              Mật khẩu
            </label>
            <input
              type="password"
              id="password"
              name="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="Nhập mật khẩu"
              disabled={isLoading}
              autoComplete="current-password"
            />
          </div>

          <div className="form-actions">
            <button type="submit" className="login-btn" disabled={isLoading}>
              {isLoading ? (
                <>
                  <span className="spinner"></span>
                  Đang đăng nhập...
                </>
              ) : (
                <>
                  <LogIn size={18} />
                  Đăng Nhập
                </>
              )}
            </button>
            {onCancel && (
              <button
                type="button"
                className="cancel-btn"
                onClick={onCancel}
                disabled={isLoading}
              >
                Hủy
              </button>
            )}
          </div>
        </form>
      </div>
    </div>
  );
}

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
      setError("Please enter all required information");
      return;
    }

    setIsLoading(true);

    try {
      const account = await accountService.login({ username, password });
      onLoginSuccess(account);
    } catch (err: any) {
      setError(err.message || "Login failed");
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="login-container">
      <div className="login-card">
        <div className="login-header">
          <div className="full-width flex-col">
            <h1>Login</h1>
            <div className="flex-vertical">
                <LogIn size={20} className="login-icon" />
                <p>Book Management</p>
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
              Username
            </label>
            <input
              type="text"
              id="username"
              name="username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              placeholder="Enter username"
              disabled={isLoading}
              autoComplete="username"
              autoFocus
            />
          </div>

          <div className="form-group">
            <label htmlFor="password">
              <Lock size={18} />
              Password
            </label>
            <input
              type="password"
              id="password"
              name="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="Enter password"
              disabled={isLoading}
              autoComplete="current-password"
            />
          </div>

          <div className="form-actions">
            <button id="login-btn" type="submit" className="login-btn" disabled={isLoading}>
              {isLoading ? (
                <>
                  <span className="spinner"></span>
                  Logging in...
                </>
              ) : (
                <>
                  <LogIn size={18} />
                  Login
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
                Cancel
              </button>
            )}
          </div>
        </form>
      </div>
    </div>
  );
}

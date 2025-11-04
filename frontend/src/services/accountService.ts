import api from "./api";
import type { Account, LoginRequest } from "../models/Account";

export const accountService = {
  /**
   * Login with username and password
   */
  login: async (credentials: LoginRequest): Promise<Account> => {
    try {
      const response = await api.post<Account>("/accounts/login", credentials);
      return response.data;
    } catch (error: any) {
      if (error.response?.data?.error) {
        throw new Error(error.response.data.error);
      }
      throw new Error("Login failed. Please try again.");
    }
  },

  /**
   * Create new account
   */
  createAccount: async (account: Account): Promise<Account> => {
    try {
      const response = await api.post<Account>("/accounts", account);
      return response.data;
    } catch (error: any) {
      if (error.response?.data?.message) {
        throw new Error(error.response.data.message);
      }
      throw new Error("Failed to create account. Please try again.");
    }
  },

  /**
   * Get account by ID
   */
  getAccountById: async (id: number): Promise<Account> => {
    const response = await api.get<Account>(`/accounts/${id}`);
    return response.data;
  },

  /**
   * Get all accounts
   */
  getAllAccounts: async (): Promise<Account[]> => {
    const response = await api.get<Account[]>("/accounts");
    return response.data;
  },

  /**
   * Update account
   */
  updateAccount: async (id: number, account: Account): Promise<Account> => {
    const response = await api.put<Account>(`/accounts/${id}`, account);
    return response.data;
  },

  /**
   * Delete account
   */
  deleteAccount: async (id: number): Promise<void> => {
    await api.delete(`/accounts/${id}`);
  },
};

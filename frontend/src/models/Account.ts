/**
 * Account entity matching backend AccountDTO
 */
export interface Account {
  id?: number;
  username: string;
  password?: string; // Only used for create/update, not returned from backend
  createdDate?: string; // ISO date string
}

/**
 * Login request payload
 */
export interface LoginRequest {
  username: string;
  password: string;
}

/**
 * Create an empty Account with default values
 */
export const createEmptyAccount = (): Account => ({
  username: "",
  password: "",
});

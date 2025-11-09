import { clsx, type ClassValue } from 'clsx'
import { twMerge } from 'tailwind-merge'

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs))
}

/**
 * Validation utilities for form inputs
 */
export const validation = {
  /**
   * Validate product name (3-100 characters, not empty)
   */
  validateProductName: (name: string): { valid: boolean; error?: string } => {
    if (!name || name.trim().length === 0) {
      return { valid: false, error: 'Tên sản phẩm không được để trống' }
    }
    const wordCount = name.trim().split(/\s+/).length
    if (wordCount < 3) {
      return { valid: false, error: 'Tên sản phẩm phải có ít nhất 3 từ' }
    }
    if (wordCount > 100) {
      return { valid: false, error: 'Tên sản phẩm không được vượt quá 100 từ' }
    }
    return { valid: true }
  },

  /**
   * Validate price (0 - 999,999,999)
   */
  validatePrice: (price: number): { valid: boolean; error?: string } => {
    if (price < 0) {
      return { valid: false, error: 'Giá phải lớn hơn hoặc bằng 0' }
    }
    if (price > 999999999) {
      return { valid: false, error: 'Giá không được vượt quá 999,999,999' }
    }
    return { valid: true }
  },

  /**
   * Validate quantity (0 - 99,999)
   */
  validateQuantity: (quantity: number): { valid: boolean; error?: string } => {
    if (quantity < 0) {
      return { valid: false, error: 'Số lượng phải lớn hơn hoặc bằng 0' }
    }
    if (quantity > 99999) {
      return { valid: false, error: 'Số lượng không được vượt quá 99,999' }
    }
    return { valid: true }
  },

  /**
   * Validate description (max 500 characters)
   */
  validateDescription: (description: string): { valid: boolean; error?: string } => {
    if (description && description.length > 500) {
      return { valid: false, error: 'Mô tả không được vượt quá 500 ký tự' }
    }
    return { valid: true }
  },

  /**
   * Validate category (must be selected)
   */
  validateCategory: (categoryId?: number): { valid: boolean; error?: string } => {
    if (!categoryId) {
      return { valid: false, error: 'Vui lòng chọn thể loại' }
    }
    return { valid: true }
  },

  /**
   * Validate username (not empty, 3-50 characters)
   */
  validateUsername: (username: string): { valid: boolean; error?: string } => {
    if (!username || username.trim().length === 0) {
      return { valid: false, error: 'Tên người dùng không được để trống' }
    }
    if (username.length < 3) {
      return { valid: false, error: 'Tên người dùng phải có ít nhất 3 ký tự' }
    }
    if (username.length > 50) {
      return { valid: false, error: 'Tên người dùng không được vượt quá 50 ký tự' }
    }
    return { valid: true }
  },

  /**
   * Validate password (not empty, min 6 characters)
   */
  validatePassword: (password: string): { valid: boolean; error?: string } => {
    if (!password || password.trim().length === 0) {
      return { valid: false, error: 'Mật khẩu không được để trống' }
    }
    if (password.length < 6) {
      return { valid: false, error: 'Mật khẩu phải có ít nhất 6 ký tự' }
    }
    return { valid: true }
  },
}

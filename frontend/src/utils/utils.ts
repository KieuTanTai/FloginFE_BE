import { clsx, type ClassValue } from 'clsx'
import { twMerge } from 'tailwind-merge'

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs))
}

export const validation = {
  validateProductName: (name: string): { valid: boolean; error?: string } => {
    if (!name || name.trim().length === 0) {
      return { valid: false, error: 'Tên sản phẩm không được để trống' }
    }
    if (name.length < 3) {
      return { valid: false, error: 'Tên sản phẩm phải có ít nhất 3 ký tự' }
    }
    if (name.length > 100) {
      return { valid: false, error: 'Tên sản phẩm không được vượt quá 100 ký tự' }
    }
    return { valid: true }
  },

  validatePrice: (price: number): { valid: boolean; error?: string } => {
    if (price < 0) {
      return { valid: false, error: 'Giá phải lớn hơn hoặc bằng 0' }
    }
    if (price > 999999999) {
      return { valid: false, error: 'Giá không được vượt quá 999,999,999' }
    }
    return { valid: true }
  },

  validateQuantity: (quantity: number): { valid: boolean; error?: string } => {
    if (quantity < 0) {
      return { valid: false, error: 'Số lượng phải lớn hơn hoặc bằng 0' }
    }
    if (quantity > 99999) {
      return { valid: false, error: 'Số lượng không được vượt quá 99,999' }
    }
    return { valid: true }
  },

  validateDescription: (description: string): { valid: boolean; error?: string } => {
    if (description && description.length > 500) {
      return { valid: false, error: 'Mô tả không được vượt quá 500 ký tự' }
    }
    return { valid: true }
  },

  validateCategory: (categoryId?: number): { valid: boolean; error?: string } => {
    if (!categoryId) {
      return { valid: false, error: 'Vui lòng chọn thể loại' }
    }
    return { valid: true }
  },

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
    if (!/^[a-zA-Z0-9._-]+$/.test(username)) {
      return { valid: false, error: 'Tên người dùng chỉ được chứa chữ cái, số, dấu chấm, gạch ngang và gạch dưới' }
    }
    return { valid: true }
  },

  validatePassword: (password: string): { valid: boolean; error?: string } => {
    if (!password || password.trim().length === 0) {
      return { valid: false, error: 'Mật khẩu không được để trống' }
    }
    if (password.length < 6) {
      return { valid: false, error: 'Mật khẩu phải có ít nhất 6 ký tự' }
    }
    if (password.length > 100) {
      return { valid: false, error: 'Mật khẩu không được vượt quá 100 ký tự' }
    }
    if (!/[a-zA-Z]/.test(password) || !/[0-9]/.test(password)) {
      return { valid: false, error: 'Mật khẩu phải chứa cả chữ cái và số' }
    }
    return { valid: true }
  },
}
